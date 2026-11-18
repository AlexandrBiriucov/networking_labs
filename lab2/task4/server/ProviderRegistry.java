package lab2.task4.server;

import java.util.List;

/**
 * Knows which connection is the provider (at most one at a time) and sends
 * low-stock alerts to it. All state is guarded by this object's lock.
 */
public class ProviderRegistry {

    private ClientHandler provider;

    /** Returns false if another provider is already connected. */
    public synchronized boolean register(ClientHandler handler, Stock stock) {
        if (provider != null && provider != handler) {
            return false;
        }
        provider = handler;
        // Alerts that happened while no provider was connected: the stock itself
        // tells us which products are still low, with their current quantity.
        List<Stock.LowStock> low = stock.getLowProducts();
        for (Stock.LowStock l : low) {
            sendAlert(handler, l);
        }
        return true;
    }

    public synchronized void unregister(ClientHandler handler) {
        if (provider == handler) {
            provider = null;
        }
    }

    public synchronized boolean isProvider(ClientHandler handler) {
        return provider == handler;
    }

    /**
     * Called by the thread that has just changed the stock (the client thread
     * whose order made the product low). If nobody is connected, nothing is sent:
     * the alert will be produced by register() when the provider connects.
     */
    public synchronized void alertLow(List<Stock.LowStock> low) {
        if (provider == null || low == null) {
            return;
        }
        for (Stock.LowStock l : low) {
            sendAlert(provider, l);
        }
    }

    private void sendAlert(ClientHandler handler, Stock.LowStock l) {
        System.out.println("[" + handler.getEndpoint() + "] alert sent to provider: " + l.product
                + " low (" + l.quantity + ")");
        handler.sendMessage("ALERT|LOW_STOCK|" + l.product + "|" + l.quantity);
    }
}
