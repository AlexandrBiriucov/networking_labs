package lab2.task2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lab2.task2.orderPreparation.OrderManager;

public class Server {

    private ServerSocket serverSocket;
    private Stock stock;

    public void start(int port) throws IOException {
        System.out.println("###SERVER###");
        serverSocket = new ServerSocket(port);
        System.out.println("Server working on a port : " + serverSocket.getLocalPort());

        stock = new Stock();
        System.out.println("Stock was created " + stock.getStock());

        OrderManager orderManager = new OrderManager();
        orderManager.startWorkers();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            try {
                ClientHandler handler = new ClientHandler(clientSocket, stock, orderManager);
                new Thread(handler, "client-" + clientSocket.getPort()).start();
            } catch (IOException e) {
                System.out.println("[" + clientSocket.getRemoteSocketAddress() + "] error: " + e.getMessage());
                clientSocket.close();
            }
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
}
