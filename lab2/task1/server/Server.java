package lab2.task1.server;
import java.net.*;
import java.util.Scanner;

import lab2.task1.orderPreparation.OrderManager;

import java.io.*;


public class Server {
    
    private ServerSocket serverSocket;
    // private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Stock stock;

    public void start (int port) throws IOException {
        System.out.println("###SERVER###"); 
        serverSocket = new ServerSocket(port);
        System.out.println("Server working on a port : "+ serverSocket.getLocalPort());
        
        stock = new Stock(); 
        System.out.println("Stock was created " + stock.getStock());


        OrderManager orderManager = new OrderManager();
        orderManager.startWorkers();

        while (true){

            
            Socket clientSocket = serverSocket.accept();
            System.out.println("connection was made" + clientSocket.getLocalSocketAddress());
            // ClientHandler clientHandler = new ClientHandler(clientSocket);
            try {
            ClientHandler handler = new ClientHandler(clientSocket,stock,orderManager);
            handler.handleClient();
                

            } 
            catch (IOException e) {
            e.printStackTrace();
                }
            



        }

        // System.out.println("Server : connection aquired" + clientSocket.getLocalPort());
        // out = new PrintWriter(clientSocket.getOutputStream(), true);
        // in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));



        

            

        }


     public void stop() throws IOException {
        in.close();
        out.close();
        // clientSocket.close();
        serverSocket.close();
    }


    // public static void main(String[] args) {
    //     System.out.println("enter the port number for example 6666");
    //     Scanner sc = new Scanner(System.in);
    //     int port = sc.nextInt();
    //     sc.nextLine();
    //     // sc.nextLine();
    //     System.out.println(port);
    //     // System.out.println("Hello World");
    //     Server server = new Server();

    //     try {
    //     server.start(port);
    //     }
    //     catch (IOException e){
    //         e.printStackTrace();
    //     }
        

    //     try{
    //         server.stop();
    //     }

    //     catch (IOException e){
    //         e.printStackTrace();
    //     }

        


    // }








}
