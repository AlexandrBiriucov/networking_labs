package lab2.task4.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {

    public enum State { WAITING, PREPARING, READY, CANCELLED }

    private static final AtomicInteger nextId = new AtomicInteger(0);

    private final List<OrderItem> items = new ArrayList<>();
    private final int id;
    private final CountDownLatch cancelSignal = new CountDownLatch(1);
    private State state = State.WAITING;

    public Order() {
        this.id = nextId.incrementAndGet();
    }

    public int getId() {
        return this.id;
    }

    public void addItemOrder(OrderItem item) {
        items.add(item);
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public int getTotalPreparationTime() {
        int total = 0;
        for (OrderItem orderItem : items) {
            total += orderItem.getQuantity() * orderItem.getProduct().getPreparationTime();
        }
        return total;
    }

    public synchronized boolean tryStart() {
        if (state != State.WAITING) {
            return false;
        }
        state = State.PREPARING;
        return true;
    }

    public synchronized boolean tryFinish() {
        if (state != State.PREPARING) {
            return false;
        }
        state = State.READY;
        return true;
    }

    public synchronized State cancel() {
        if (state != State.WAITING && state != State.PREPARING) {
            return null;
        }
        State before = state;
        state = State.CANCELLED;
        cancelSignal.countDown();
        return before;
    }

    public boolean awaitCancel(long seconds) throws InterruptedException {
        return cancelSignal.await(seconds, TimeUnit.SECONDS);
    }
}
