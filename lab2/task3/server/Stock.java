package lab2.task3.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lab2.task3.common.Menu;
import lab2.task3.common.OrderItem;
import lab2.task3.common.Product;

public class Stock {

    /** A product whose quantity is below this value triggers an alert to the provider. */
    public static final int LOW_THRESHOLD = 5;

    /** A product that has just fallen below the threshold. */
    public static class LowStock {
        public final String product;
        public final int quantity;

        public LowStock(String product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }

    /** Outcome of an order: either a refusal text, or the list of products that became low. */
    public static class RemoveResult {
        public final String refusal;
        public final List<LowStock> newlyLow;

        public RemoveResult(String refusal, List<LowStock> newlyLow) {
            this.refusal = refusal;
            this.newlyLow = newlyLow;
        }
    }

    private final Map<String, Integer> StockMap = new HashMap<>();

    public Stock() {
        createRandomStock();
    }

    public synchronized String getStock() {
        return StockMap.toString();
    }

    public synchronized Integer getQuantity(String productName) {
        return StockMap.get(productName);
    }

    /**
     * Checks the whole order and takes it from the stock in one atomic step.
     * The "did it fall below the threshold" decision is taken inside the same
     * lock, so exactly one order sees the crossing 5 -> 4.
     */
    public synchronized RemoveResult tryRemoveOrder(List<OrderItem> items) {
        Map<String, Integer> totals = new HashMap<>();
        for (OrderItem item : items) {
            totals.merge(item.getProduct().getName(), item.getQuantity(), Integer::sum);
        }

        for (Map.Entry<String, Integer> entry : totals.entrySet()) {
            int available = StockMap.get(entry.getKey());
            if (available < entry.getValue()) {
                return new RemoveResult("Not enough " + entry.getKey() + ". Available: " + available, null);
            }
        }

        List<LowStock> newlyLow = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : totals.entrySet()) {
            int before = StockMap.get(entry.getKey());
            int after = before - entry.getValue();
            StockMap.put(entry.getKey(), after);
            if (before >= LOW_THRESHOLD && after < LOW_THRESHOLD) {
                newlyLow.add(new LowStock(entry.getKey(), after));
            }
        }
        return new RemoveResult(null, newlyLow);
    }

    /** Adds the delivered products to the stock at once (atomic for the whole delivery). */
    public synchronized void deliver(Map<String, Integer> delivery) {
        for (Map.Entry<String, Integer> entry : delivery.entrySet()) {
            StockMap.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }

    /** All the products that are currently below the threshold (used when the provider connects). */
    public synchronized List<LowStock> getLowProducts() {
        List<LowStock> low = new ArrayList<>();
        for (Product product : Menu.getProductList()) {
            int quantity = StockMap.get(product.getName());
            if (quantity < LOW_THRESHOLD) {
                low.add(new LowStock(product.getName(), quantity));
            }
        }
        return low;
    }

    private synchronized void setQuantity(String productName, int amount) {
        StockMap.put(productName, amount);
    }

    public void createRandomStock() {
        int min = 10;
        int max = 50;
        for (Product product : Menu.getProductList()) {
            int randomNumberOfProducts = (int) (((max - min) + 1) * Math.random()) + min;
            setQuantity(product.getName(), randomNumberOfProducts);
        }
    }
}
