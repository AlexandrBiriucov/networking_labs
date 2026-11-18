package lab2.task4.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class LineReader {

    private final InputStream in;
    private final byte[] buffer;
    private int length = 0;
    private boolean discarding = false;

    public LineReader(InputStream in, int maxLine) {
        this.in = new BufferedInputStream(in);
        this.buffer = new byte[maxLine];
    }

    public String readLine() throws IOException, BadInputException {
        while (true) {
            int b = in.read();
            if (b < 0) {
                return null;
            }

            if (b == '\n') {
                if (discarding) {
                    discarding = false;
                    continue;
                }
                int n = length;
                length = 0;
                if (n > 0 && buffer[n - 1] == '\r') {
                    n--;
                }
                for (int i = 0; i < n; i++) {
                    if (buffer[i] < 0x20 || buffer[i] > 0x7E) {
                        throw new BadInputException("ERROR|INVALID_CHARACTERS");
                    }
                }
                return new String(buffer, 0, n, StandardCharsets.US_ASCII);
            }

            if (discarding) {
                continue;
            }

            if (length == buffer.length) {
                length = 0;
                discarding = true;
                throw new BadInputException("ERROR|MESSAGE_TOO_LONG");
            }
            buffer[length++] = (byte) b;
        }
    }
}
