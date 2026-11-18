package lab2.task4.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lab2.task4.orderPreparation.OrderManager;

public class Server {

    private ServerSocket serverSocket;
    private Stock stock;

    public void start(int port) throws IOException {
        System.out.println("###SERVER###");
        serverSocket = new ServerSocket(port);
        System.out.println("Server working on a port : " + serverSocket.getLocalPort());

        stock = new Stock();
        System.out.println("Stock was created " + stock.getStock());

        OrderManager orderManager = new OrderManager(stock);
        orderManager.startWorkers();
        ProviderRegistry providers = new ProviderRegistry();

        while (true) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("error: accept failed: " + e.getMessage());
                pause();
                continue;
            }
            try {
                ClientHandler handler = new ClientHandler(clientSocket, stock, orderManager, providers);
                new Thread(handler, "client-" + clientSocket.getPort()).start();
            } catch (RuntimeException e) {
                System.out.println("[" + clientSocket.getRemoteSocketAddress() + "] error: " + e);
                clientSocket.close();
            }
        }
    }

    private void pause() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
}
