package lab2.task1.orderPreparation;

import lab2.task1.common.Order;
import lab2.task1.common.OrderTask;

import java.lang.Thread;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
public class OrderWorker implements Runnable {

    private final BlockingQueue<OrderTask> queue;
    private final OrderManager manager;

    public OrderWorker(BlockingQueue<OrderTask> queue, OrderManager manager) {
        this.queue = queue;
        this.manager = manager;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                //take the orderr form the queue
                OrderTask order = queue.take();

                Thread.sleep(order.getOrder().getTotalPreparationTime() * 1000L);

                manager.orderReady(order);

                // manager.notifyClient(order);


            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
