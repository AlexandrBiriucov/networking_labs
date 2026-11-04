package lab2.task1;
import java.net.*;
import java.util.Scanner;
import java.io.*;


public class Server {
    
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    
     public void start (int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        System.out.println("Server : connection aquired" + clientSocket.getLocalPort());
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String greeting = in.readLine();
            if ("hello server".equals(greeting)) {
                out.println("hello client");
            }
            else {
                out.println("unrecognised greeting");
            }
    }


     public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }


    public static void main(String[] args) {
        System.out.println("enter the port number for example 6666");
        Scanner sc = new Scanner(System.in);
        int port = sc.nextInt();
        sc.nextLine();
        // sc.nextLine();
        System.out.println(port);
        // System.out.println("Hello World");
        Server server = new Server();

        try {
        server.start(port);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        

        try{
            server.stop();
        }

        catch (IOException e){
            e.printStackTrace();
        }

        


    }








}
