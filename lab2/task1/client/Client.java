package lab2.task1.client;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.lang.Thread;
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


    public void testOrder() throws IOException{

        for (int i = 0; i < 5; i++) {
        // System.out.println(placeOrder("ORDER|tea|5"));
        placeOrder("ORDER|tea|3");
    }


    }


    public void run(){

        while(true){
            System.out.println("enter a command(for example MENU; ORDER|{productname}|{quantity})");
            String command=sc.nextLine();
            String[] commandArr= command.split("\\|");
             String requestType = commandArr[0];
            switch (requestType) {
                case "MENU":
                    try{
                    // String response =requestMenu(); 
                    // System.out.println(response);
                    requestMenu();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
                case "ORDER":
                    try{
                        if ((commandArr.length < 3 || (commandArr.length - 1) % 2 != 0)){
                            System.out.println("Invalid ORDER format. Use ORDER|product|quantity");
                            break;
                        }
                        placeOrder(command);
                        // String response = placeOrder(command);
                        // System.out.println(response);
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






    public void startListener() {
    Thread listenerThread = new Thread(() -> {
        try {
            String message;

            while ((message = in.readLine()) != null) {
                System.out.println("Server: " + message);
            }

        } catch (IOException e) {
            System.out.println("Connection closed.");
        }
    });

    listenerThread.start();
}

    public void sendMessage(String msg) throws IOException{
        out.println(msg);
        // String resp = in.readLine();
        // return resp;
    

    }

        public void requestMenu() throws IOException{
            sendMessage("MENU");
             
        }


        public void placeOrder(String command) throws IOException{
            //command will send ORDER|Coffee|4    something like that
            sendMessage(command);
            
        }


    public void StopConnection() throws IOException{
        in.close();
        out.close();
        clientSocket.close();
    
    }





}
