package lab2.task2.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {

    private static final AtomicInteger nextId = new AtomicInteger(0);
    private final List<OrderItem> items;
    private final int id;

    public Order() {
        this.items = new ArrayList<>();
        this.id = nextId.incrementAndGet();
    }

    public int getId() {
        return this.id;
    }

    public void addItemOrder(OrderItem item) {
        items.add(item);
    }

    public int getTotalPreparationTime() {
        int total = 0;
        for (OrderItem orderItem : items) {
            total += orderItem.getQuantity() * orderItem.getProduct().getPreparationTime();
        }
        return total;
    }
}
