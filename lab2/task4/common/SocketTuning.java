package lab2.task4.common;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketOption;

import jdk.net.ExtendedSocketOptions;

public final class SocketTuning {

    private SocketTuning() {
    }

    public static void apply(Socket socket) {
        try {
            socket.setKeepAlive(true);
        } catch (IOException e) {
            return;
        }
        set(socket, ExtendedSocketOptions.TCP_KEEPIDLE, 30);
        set(socket, ExtendedSocketOptions.TCP_KEEPINTERVAL, 10);
        set(socket, ExtendedSocketOptions.TCP_KEEPCOUNT, 3);
    }

    private static void set(Socket socket, SocketOption<Integer> option, int value) {
        try {
            if (socket.supportedOptions().contains(option)) {
                socket.setOption(option, value);
            }
        } catch (IOException | UnsupportedOperationException e) {
        }
    }
}
