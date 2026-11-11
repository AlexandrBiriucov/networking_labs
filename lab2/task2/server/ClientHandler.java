package lab2.task2.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import lab2.task2.orderPreparation.OrderManager;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Stock stock;
    private OrderManager orderManager;
    private final String endpoint;
    private final Object writeLock = new Object();

    public ClientHandler(Socket clientSocket, Stock stock, OrderManager orderManager) throws IOException {
        this.orderManager = orderManager;
        this.clientSocket = clientSocket;
        this.stock = stock;
        this.endpoint = String.valueOf(clientSocket.getRemoteSocketAddress());
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public void run() {
        try {
            handleClient();
        } catch (IOException e) {
            System.out.println("[" + endpoint + "] error: " + e.getMessage());
        }
    }

    public void handleClient() throws IOException {
        System.out.println("[" + endpoint + "] connected");
        try {
            RequestHandler requestHandler = new RequestHandler(stock, orderManager, this);
            String request;
            while ((request = in.readLine()) != null) {
                System.out.println("[" + endpoint + "] request: " + request);
                String response = requestHandler.handleRequest(request);
                sendMessage(response);
            }
            System.out.println("[" + endpoint + "] disconnected");
        } finally {
            clientSocket.close();
        }
    }

    public void sendMessage(String message) {
        synchronized (writeLock) {
            out.println(message);
        }
    }
}
