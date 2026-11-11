package lab2.task2.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lab2.task2.common.Menu;
import lab2.task2.common.OrderItem;
import lab2.task2.common.Product;

public class Stock {

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

    public synchronized boolean tryRemoveItem(String item, int amount) {
        if (item == null || !StockMap.containsKey(item) || amount <= 0) {
            return false;
        }
        int current = StockMap.get(item);
        if (current < amount) {
            return false;
        }
        StockMap.put(item, current - amount);
        return true;
    }

    public synchronized String tryRemoveOrder(List<OrderItem> items) {
        Map<String, Integer> totals = new HashMap<>();
        for (OrderItem item : items) {
            totals.merge(item.getProduct().getName(), item.getQuantity(), Integer::sum);
        }

        for (Map.Entry<String, Integer> entry : totals.entrySet()) {
            int available = StockMap.get(entry.getKey());
            if (available < entry.getValue()) {
                return "Not enough " + entry.getKey() + ". Available: " + available;
            }
        }

        for (Map.Entry<String, Integer> entry : totals.entrySet()) {
            StockMap.put(entry.getKey(), StockMap.get(entry.getKey()) - entry.getValue());
        }
        return null;
    }

    public synchronized void addItem(String productName, int amount) {
        StockMap.put(productName, amount);
    }

    public void createRandomStock() {
        int min = 10;
        int max = 50;
        for (Product product : Menu.getProductList()) {
            int randomNumberOfProducts = (int) (((max - min) + 1) * Math.random()) + min;
            addItem(product.getName(), randomNumberOfProducts);
        }
    }
}
