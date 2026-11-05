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
    public ClientHandler(Socket clienSocket) throws IOException{

        this.clientSocket=clientSocket;
        
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clienSocket.getOutputStream(), true);


    }

    public void handleClient() throws IOException{

        while (true){
            String request = in.readLine();
            if (in ==null){
                break;
            }                    
        
        RequestHandlder requestHandler = new RequestHandlder();
         requestHandler.handleRequest(request);   

            

        }



    }





}
