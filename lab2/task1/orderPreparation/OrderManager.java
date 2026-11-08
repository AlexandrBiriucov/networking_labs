package lab2.task1.orderPreparation;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lab2.task1.common.Order;
import lab2.task1.common.OrderTask;
import lab2.task1.server.ClientHandler;



public class OrderManager {
    //blocking queue the orders;
    private final BlockingQueue<OrderTask> queue = new LinkedBlockingQueue<>();
//get the number of avaliable logical proccesors
    private final int cores = Runtime.getRuntime().availableProcessors();
    // private final int cores = 3;

    public void startWorkers() {
        for (int i = 0; i < cores; i++) {
            Thread thread = new Thread(new OrderWorker(queue, this));
            thread.start();
        }
    }

    public void addOrder(Order order, ClientHandler clientHandler) {
        try {
            OrderTask orderTask = new OrderTask(order, clientHandler);
            queue.put(orderTask);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

   public void orderReady(OrderTask orderTask) {
    System.out.println("Order " + orderTask.getOrder().getId() + " is ready!");

    orderTask.getClientHandler().sendMessage(
        "READY|" + orderTask.getOrder().getId()
    );
}

    // public String notifyClient(Order order){
    //     String msgReturn = "Your order is ready" ;
    //     return msgReturn;
    // }


}