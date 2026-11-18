package lab2.task4.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import lab2.task4.common.OrderTask;
import lab2.task4.common.SocketTuning;
import lab2.task4.orderPreparation.OrderManager;

public class ClientHandler implements Runnable {

    private static final int MAX_LINE = 256;
    private static final int OUTBOX_LIMIT = 1000;

    private Socket clientSocket;
    private Stock stock;
    private OrderManager orderManager;
    private final ProviderRegistry providers;
    private final String endpoint;
    private final BlockingQueue<String> outbox = new LinkedBlockingQueue<>(OUTBOX_LIMIT);
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final List<OrderTask> orders = new ArrayList<>();
    private final Thread writer;

    public ClientHandler(Socket clientSocket, Stock stock, OrderManager orderManager,
                         ProviderRegistry providers) {
        this.providers = providers;
        this.orderManager = orderManager;
        this.clientSocket = clientSocket;
        this.stock = stock;
        this.endpoint = String.valueOf(clientSocket.getRemoteSocketAddress());
        SocketTuning.apply(clientSocket);
        this.writer = new Thread(this::writeLoop, "writer-" + clientSocket.getPort());
        this.writer.setDaemon(true);
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void track(OrderTask task) {
        orders.add(task);
    }

    @Override
    public void run() {
        try {
            handleClient();
        } catch (IOException e) {
            System.out.println("[" + endpoint + "] connection lost: " + e.getMessage());
        }
    }

    public void handleClient() throws IOException {
        System.out.println("[" + endpoint + "] connected");
        writer.start();
        try {
            RequestHandler requestHandler = new RequestHandler(stock, orderManager, this, providers);
            LineReader reader = new LineReader(clientSocket.getInputStream(), MAX_LINE);
            while (true) {
                String request;
                try {
                    request = reader.readLine();
                } catch (BadInputException e) {
                    System.out.println("[" + endpoint + "] error: " + e.getMessage());
                    sendMessage(e.getMessage());
                    continue;
                }
                if (request == null) {
                    System.out.println("[" + endpoint + "] closed the connection");
                    break;
                }
                System.out.println("[" + endpoint + "] request: " + request);
                try {
                    sendMessage(requestHandler.handleRequest(request));
                } catch (RuntimeException e) {
                    System.out.println("[" + endpoint + "] error: " + e);
                    sendMessage("ERROR|INTERNAL");
                }
            }
        } finally {
            cleanup();
        }
    }

    public void sendMessage(String message) {
        if (closed.get()) {
            return;
        }
        if (!outbox.offer(message)) {
            System.out.println("[" + endpoint + "] error: client does not read its messages, closing");
            close();
        }
    }

    private void cleanup() {
        if (providers.isProvider(this)) {
            providers.unregister(this);
            System.out.println("[" + endpoint + "] provider disconnected");
        }
        for (int i = orders.size() - 1; i >= 0; i--) {
            orderManager.cancel(orders.get(i));
        }
        close();
        System.out.println("[" + endpoint + "] disconnected");
    }

    private void close() {
        if (closed.compareAndSet(false, true)) {
            try {
                clientSocket.close();
            } catch (IOException e) {
            }
            writer.interrupt();
        }
    }

    private void writeLoop() {
        try {
            OutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
            while (true) {
                String message = outbox.take();
                out.write((message + "\n").getBytes(StandardCharsets.UTF_8));
                out.flush();
            }
        } catch (InterruptedException e) {
        } catch (IOException e) {
            System.out.println("[" + endpoint + "] error: write failed: " + e.getMessage());
            close();
        }
    }
}
