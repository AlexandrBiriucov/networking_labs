package lab2.task1.server;


import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ClientHandler {
    
// private BufferedReader in;
// private PrintWriter out;

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    public ClientHandler(Socket clientSocket) throws IOException{

        this.clientSocket= clientSocket;
        
         this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);


    }

    public void handleClient() throws IOException{

        while (true){
            String request = in.readLine();
            if (request ==null){
                break;
            }                    
        System.out.println("server gets a request" +  " " +request);
        RequestHandler requestHandler = new RequestHandler();
         String requestedString = requestHandler.handleRequest(request);
         
         out.println(requestedString);
        

            

        }



    }





}
