package lab2.task4.orderPreparation;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import lab2.task4.common.Order;
import lab2.task4.common.OrderItem;
import lab2.task4.common.OrderTask;
import lab2.task4.server.ClientHandler;
import lab2.task4.server.Stock;

public class OrderManager {

    private final BlockingQueue<OrderTask> queue = new LinkedBlockingQueue<>();
    private final int cores = Runtime.getRuntime().availableProcessors();
    private final Stock stock;

    public OrderManager(Stock stock) {
        this.stock = stock;
    }

    public void startWorkers() {
        System.out.println("Starting " + cores + " workers");
        for (int i = 0; i < cores; i++) {
            Thread thread = new Thread(new OrderWorker(queue, this), "worker-" + (i + 1));
            thread.start();
        }
    }

    public synchronized OrderTask submit(List<OrderItem> items, ClientHandler clientHandler) {
        Order order = new Order();
        for (OrderItem item : items) {
            order.addItemOrder(item);
        }
        OrderTask task = new OrderTask(order, clientHandler);
        queue.add(task);
        return task;
    }

    public void cancel(OrderTask task) {
        Order order = task.getOrder();
        Order.State before = order.cancel();
        if (before == null) {
            return;
        }
        queue.remove(task);
        stock.giveBack(order.getItems());
        System.out.println("[" + task.getClientHandler().getEndpoint() + "] order " + order.getId()
                + " cancelled (was " + before + "), products returned to stock");
    }

    public void orderReady(OrderTask orderTask) {
        System.out.println("[" + orderTask.getClientHandler().getEndpoint() + "] order "
                + orderTask.getOrder().getId() + " ready");
        orderTask.getClientHandler().sendMessage("READY|" + orderTask.getOrder().getId());
    }
}
