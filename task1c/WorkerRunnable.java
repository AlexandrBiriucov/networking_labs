package task1c;

import java.util.concurrent.BlockingQueue;

public class WorkerRunnable implements Runnable {

    private BlockingQueue<int[]> queue;


        private long positiveCount = 0;
    private long negativeCount = 0;
    private long zeroCount = 0;

    public WorkerRunnable(BlockingQueue<int[]> queue) {
        this.queue = queue;
    }

   @Override
public void run() {
    try {
        while (true) {
            int[] batch = queue.take();

            if (batch.length == 0) {
                break;
            }
            for (int i=0;i<batch.length;i++){

                if (batch[i]>0){
                    positiveCount++;
                }
                else if (batch[i]<0){
                    negativeCount++;
                }

                else{
                    zeroCount++;
                }
            }
            // Count the numbers in this batch here
        }
    }
    catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }   
}


    public long getPositiveCount() {
    return positiveCount;
}

public long getNegativeCount() {
    return negativeCount;
}

public long getZeroCount() {
    return zeroCount;
}



}