package lab2.task1.client;
import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner sc = new Scanner(System.in);

    public void startConnection(String ip, int port)  throws IOException{
        clientSocket = new Socket(ip, port);
        System.out.println(clientSocket.getLocalPort());
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }





    public void run(){

        while(true){
            System.out.println("enter a command(for example MENU)");
            String command=sc.nextLine();
            switch (command) {
                case "MENU":
                    try{
                    String response =requestMenu();
                    System.out.println(response);
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
            
                default:
                    break;
            }

        }



    }





    public String sendMessage(String msg) throws IOException{
        out.println(msg);
        String resp = in.readLine();
        return resp;
        

    }

        public String requestMenu() throws IOException{
            return sendMessage("MENU");
             
        }




    public void StopConnection() throws IOException{
        in.close();
        out.close();
        clientSocket.close();
    
    }





}
