package lab2.task2.orderPreparation;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import lab2.task2.common.Order;
import lab2.task2.common.OrderItem;
import lab2.task2.common.OrderTask;
import lab2.task2.server.ClientHandler;

public class OrderManager {

    private final BlockingQueue<OrderTask> queue = new LinkedBlockingQueue<>();
    private final int cores = Runtime.getRuntime().availableProcessors();

    public void startWorkers() {
        System.out.println("Starting " + cores + " workers");
        for (int i = 0; i < cores; i++) {
            Thread thread = new Thread(new OrderWorker(queue, this), "worker-" + (i + 1));
            thread.start();
        }
    }

    public synchronized Order submit(List<OrderItem> items, ClientHandler clientHandler) {
        Order order = new Order();
        for (OrderItem item : items) {
            order.addItemOrder(item);
        }
        queue.add(new OrderTask(order, clientHandler));
        return order;
    }

    public void orderReady(OrderTask orderTask) {
        System.out.println("[" + orderTask.getClientHandler().getEndpoint() + "] order "
                + orderTask.getOrder().getId() + " ready");
        orderTask.getClientHandler().sendMessage("READY|" + orderTask.getOrder().getId());
    }
}
