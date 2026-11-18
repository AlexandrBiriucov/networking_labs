package lab2.task4.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import lab2.task4.common.SocketTuning;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner sc = new Scanner(System.in);
    private volatile boolean closing = false;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        SocketTuning.apply(clientSocket);
        System.out.println("connected from local port " + clientSocket.getLocalPort());
        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
    }

    public void registerAsProvider() {
        sendMessage("PROVIDER");
    }

    public void run() {
        System.out.println("enter a command (MENU; ORDER|{productname}|{quantity}; "
                + "DELIVER|{productname}|{quantity}|... (provider only); QUIT)");
        while (sc.hasNextLine()) {
            String command = sc.nextLine().trim();
            if (command.isEmpty()) {
                continue;
            }
            if (command.equalsIgnoreCase("QUIT")) {
                break;
            }
            sendMessage(command);
        }
        StopConnection();
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
            if (!closing) {
                System.out.println("Server disconnected.");
                System.exit(0);
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void sendMessage(String msg) {
        out.print(msg + "\n");
        out.flush();
    }

    public void StopConnection() {
        closing = true;
        try {
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}
