package lab2.task1;

import java.io.IOException;
import java.util.Scanner;
public class Main {
    
    public static void main(String[] args){
        
        Client client = new Client();
        try {
        client.startConnection("127.0.0.1", 6666);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        try{
            String message = client.sendMessage("hello server");
            System.out.println(message);
            
        }

        catch(IOException e){
            e.printStackTrace();
        }


        try{

            client.StopConnection();
            
        }
        catch(IOException e){
            e.printStackTrace();
        }






    }

}
