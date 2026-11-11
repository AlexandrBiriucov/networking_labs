package lab2.task2.orderPreparation;

import java.util.concurrent.BlockingQueue;

import lab2.task2.common.OrderTask;

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
                OrderTask task = queue.take();
                System.out.println("[" + task.getClientHandler().getEndpoint() + "] order "
                        + task.getOrder().getId() + " started by " + Thread.currentThread().getName());
                Thread.sleep(task.getOrder().getTotalPreparationTime() * 1000L);
                manager.orderReady(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
