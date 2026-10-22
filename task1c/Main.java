package task1c;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        int[] workerCounts = {1, 2, 4, 8, 16, 32, 64};

        for (int numberOfWorkers : workerCounts) {

            BlockingQueue<int[]> queue = new ArrayBlockingQueue<>(100);

            List<WorkerRunnable> workerTasks = new ArrayList<>();
            List<Thread> workerThreads = new ArrayList<>();

            // Create workers
            for (int i = 0; i < numberOfWorkers; i++) {
                WorkerRunnable worker = new WorkerRunnable(queue);
                Thread thread = new Thread(worker);

                workerTasks.add(worker);
                workerThreads.add(thread);
            }

            // Create reader
            Thread readerThread = new Thread(
                    new ReaderRunnable(queue, numberOfWorkers)
            );

            // Start measuring time
            long startTime = System.nanoTime();

            // Start workers first
            for (Thread thread : workerThreads) {
                thread.start();
            }

            // Start reader
            readerThread.start();

            // Wait for reader to finish
            readerThread.join();

            // Wait for every worker to finish
            for (Thread thread : workerThreads) {
                thread.join();
            }

            long endTime = System.nanoTime();

            // Collect results
            long positiveCount = 0;
            long negativeCount = 0;
            long zeroCount = 0;

            for (WorkerRunnable worker : workerTasks) {
                positiveCount += worker.getPositiveCount();
                negativeCount += worker.getNegativeCount();
                zeroCount += worker.getZeroCount();
            }

            double elapsedSeconds =
                    (endTime - startTime) / 1_000_000_000.0;

            System.out.println("\nWorkers: " + numberOfWorkers);
            System.out.println("Positive numbers: " + positiveCount);
            System.out.println("Negative numbers: " + negativeCount);
            System.out.println("Zeros: " + zeroCount);
            System.out.printf("Execution time: %.3f seconds%n",
                    elapsedSeconds);
        }
    }
}
