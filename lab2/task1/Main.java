package lab2.task1;

import java.io.IOException;

import lab2.task1.client.Client;
import lab2.task1.server.Server;

public class Main {
    


    public static void main(String[] args) throws IOException{

        String address = "127.0.0.1";
        int port = 6666;
        System.out.println();
        if (args[0].equals("server")){
            Server server = new  Server();
            server.start(port);
            
        }

        else if (args[0].equals("client")){
            Client client = new Client();
            client.startConnection(address , port);
            client.run();
            


        }


    }


}
