package lab2.task2.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner sc = new Scanner(System.in);

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        System.out.println("connected from local port " + clientSocket.getLocalPort());
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void run() {
        System.out.println("enter a command (MENU; ORDER|{productname}|{quantity}; QUIT)");
        while (sc.hasNextLine()) {
            String command = sc.nextLine().trim();
            if (command.isEmpty()) {
                continue;
            }
            if (command.equalsIgnoreCase("QUIT")) {
                break;
            }
            String requestType = command.split("\\|")[0];
            switch (requestType) {
                case "MENU":
                    sendMessage("MENU");
                    break;
                case "ORDER":
                    String[] parts = command.split("\\|");
                    if (parts.length < 3 || (parts.length - 1) % 2 != 0) {
                        System.out.println("Invalid ORDER format. Use ORDER|product|quantity");
                        break;
                    }
                    sendMessage(command);
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }
        }
        try {
            StopConnection();
        } catch (IOException e) {
            e.printStackTrace();
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
            }
            System.out.println("Connection closed.");
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void StopConnection() throws IOException {
        clientSocket.close();
    }
}
