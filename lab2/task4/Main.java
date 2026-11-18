package lab2.task4;

import java.io.IOException;

import lab2.task4.client.Client;
import lab2.task4.server.Server;

public class Main {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (NumberFormatException e) {
            System.out.println("usage: server [port] | client [host] [port] | provider [host] [port]");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void run(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("usage: server [port] | client [host] [port] | provider [host] [port]");
            return;
        }

        if (args[0].equals("server")) {
            int port = args.length > 1 ? Integer.parseInt(args[1]) : 6666;
            Server server = new Server();
            server.start(port);
        } else if (args[0].equals("client")) {
            String address = args.length > 1 ? args[1] : "127.0.0.1";
            int port = args.length > 2 ? Integer.parseInt(args[2]) : 6666;
            Client client = new Client();
            client.startConnection(address, port);
            client.startListener();
            client.run();
        } else if (args[0].equals("provider")) {
            String address = args.length > 1 ? args[1] : "127.0.0.1";
            int port = args.length > 2 ? Integer.parseInt(args[2]) : 6666;
            Client client = new Client();
            client.startConnection(address, port);
            client.startListener();
            client.registerAsProvider();
            client.run();
        } else {
            System.out.println("usage: server [port] | client [host] [port] | provider [host] [port]");
        }
    }
}
