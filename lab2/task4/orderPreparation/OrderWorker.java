package lab2.task4.orderPreparation;

import java.util.concurrent.BlockingQueue;

import lab2.task4.common.Order;
import lab2.task4.common.OrderTask;

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
                prepare(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (RuntimeException e) {
                System.out.println("[" + Thread.currentThread().getName() + "] error: " + e);
            }
        }
    }

    private void prepare(OrderTask task) throws InterruptedException {
        Order order = task.getOrder();
        String endpoint = task.getClientHandler().getEndpoint();
        String worker = Thread.currentThread().getName();

        if (!order.tryStart()) {
            return;
        }
        System.out.println("[" + endpoint + "] order " + order.getId() + " started by " + worker);

        boolean cancelled = order.awaitCancel(order.getTotalPreparationTime());
        if (!cancelled && order.tryFinish()) {
            manager.orderReady(task);
        } else {
            System.out.println("[" + endpoint + "] order " + order.getId() + " preparation stopped by " + worker);
        }
    }
}
