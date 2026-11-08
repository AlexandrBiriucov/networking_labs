package lab2.task1.server;


import java.net.*;
import java.util.Scanner;

import lab2.task1.orderPreparation.OrderManager;

import java.io.*;

public class ClientHandler {
    
// private BufferedReader in;
// private PrintWriter out;

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Stock stock;
    private OrderManager orderManager;
    public ClientHandler(Socket clientSocket,Stock stock,OrderManager orderManager) throws IOException{
        this.orderManager=orderManager;
        this.clientSocket= clientSocket;
        this.stock=stock;
         this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);


    }

    public void handleClient() throws IOException {
    try {
        while (true) {
            String request = in.readLine();

            if (request == null) {
                break;
            }

            System.out.println("server gets a request " + request);

            RequestHandler requestHandler =
                new RequestHandler(stock, orderManager,this);

            String response = requestHandler.handleRequest(request);
            out.println(response);
        }
    } catch (SocketException e) {
        System.out.println("Client disconnected: "
                + clientSocket.getRemoteSocketAddress());
    } finally {
        clientSocket.close();
    }
}


    //for the client order ready response
    public void sendMessage(String message) {
        out.println(message);
    }





}
