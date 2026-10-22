package task1c;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class ReaderRunnable implements Runnable {
    private BlockingQueue<int[]> queue;
    private final int numberOfWorkers;

    public ReaderRunnable(BlockingQueue<int[]> queue, int numberOfWorkers) {
    this.queue = queue;
    this.numberOfWorkers = numberOfWorkers;
}   

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader("numbers.txt"))) {
            while (true) {
                int[] batch = Reader.readBatches(br);

                if (batch.length == 0) {
                    break;
                }

                queue.put(batch);
            }

            for (int i = 0; i < numberOfWorkers; i++) {
        queue.put(new int[0]);
            }
    }
            catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}