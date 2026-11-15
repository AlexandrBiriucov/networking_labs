package lab2.task3.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lab2.task3.common.*;
import lab2.task3.orderPreparation.OrderManager;

public class RequestHandler {

    private final Stock stock;
    private OrderManager orderManager;
    private ClientHandler clientHandler;
    private final ProviderRegistry providers;

    public RequestHandler(Stock stock, OrderManager orderManager, ClientHandler clientHandler,
                          ProviderRegistry providers) {
        this.providers = providers;
        this.stock = stock;
        this.orderManager = orderManager;
        this.clientHandler = clientHandler;
    }

    public String handleRequest(String request) {
        String endpoint = clientHandler.getEndpoint();
        String[] splitStr = request.trim().split("\\|");
        String requestCommand = splitStr[0];

        if (requestCommand.equals("MENU")) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Product product : Menu.getProductList()) {
                stringBuilder.append(String.format(
                    "%s | %d sec | %d available ; ",
                    product.getName(),
                    product.getPreparationTime(),
                    stock.getQuantity(product.getName())
                ));
            }
            System.out.println("[" + endpoint + "] menu sent");
            return stringBuilder.toString();
        }

        else if (requestCommand.equals("PROVIDER")) {
            if (!providers.register(clientHandler, stock)) {
                System.out.println("[" + endpoint + "] error: provider already connected");
                return "ERROR|PROVIDER_ALREADY_CONNECTED";
            }
            System.out.println("[" + endpoint + "] registered as provider");
            return "PROVIDER|OK";
        }

        else if (requestCommand.equals("DELIVER")) {
            if (!providers.isProvider(clientHandler)) {
                System.out.println("[" + endpoint + "] error: delivery refused, not the provider: " + request);
                return "ERROR|ONLY_PROVIDER_CAN_DELIVER";
            }
            if (splitStr.length < 3 || (splitStr.length - 1) % 2 != 0) {
                System.out.println("[" + endpoint + "] error: invalid delivery format: " + request);
                return "Invalid delivery format. Use DELIVER|product|quantity|product|quantity...";
            }
            Map<String, Integer> delivery = new LinkedHashMap<>();
            for (int i = 1; i < splitStr.length; i += 2) {
                Product found = null;
                for (Product product : Menu.getProductList()) {
                    if (product.getName().equalsIgnoreCase(splitStr[i])) {
                        found = product;
                        break;
                    }
                }
                if (found == null) {
                    System.out.println("[" + endpoint + "] error: unknown product in delivery: " + splitStr[i]);
                    return "Unknown product: " + splitStr[i];
                }
                int quantity;
                try {
                    quantity = Integer.parseInt(splitStr[i + 1].trim());
                } catch (NumberFormatException e) {
                    System.out.println("[" + endpoint + "] error: invalid delivery quantity: " + request);
                    return "Invalid quantity for " + splitStr[i];
                }
                if (quantity <= 0) {
                    System.out.println("[" + endpoint + "] error: non-positive delivery quantity: " + request);
                    return "Quantity must be greater than zero";
                }
                delivery.merge(found.getName(), quantity, Integer::sum);
            }
            stock.deliver(delivery);
            System.out.println("[" + endpoint + "] delivery: " + delivery + " stock=" + stock.getStock());
            return "DELIVERED|" + delivery;
        }

        else if (requestCommand.equals("ORDER")) {
            if (providers.isProvider(clientHandler)) {
                System.out.println("[" + endpoint + "] error: provider cannot order");
                return "ERROR|PROVIDER_CANNOT_ORDER";
            }
            if (splitStr.length < 3 || (splitStr.length - 1) % 2 != 0) {
                System.out.println("[" + endpoint + "] error: invalid order format: " + request);
                return "Invalid order format. Use ORDER|product|quantity";
            }

            List<OrderItem> items = new ArrayList<>();

            for (int i = 1; i < splitStr.length; i += 2) {
                String itemName = splitStr[i];
                int itemQuantity;

                try {
                    itemQuantity = Integer.parseInt(splitStr[i + 1]);
                } catch (NumberFormatException e) {
                    System.out.println("[" + endpoint + "] error: invalid quantity: " + request);
                    return "Invalid quantity for " + itemName;
                }

                if (itemQuantity <= 0) {
                    System.out.println("[" + endpoint + "] error: non-positive quantity: " + request);
                    return "Quantity must be greater than zero";
                }

                Product foundProduct = null;
                for (Product product : Menu.getProductList()) {
                    if (product.getName().equalsIgnoreCase(itemName)) {
                        foundProduct = product;
                        break;
                    }
                }

                if (foundProduct == null) {
                    System.out.println("[" + endpoint + "] error: unknown product: " + itemName);
                    return "Unknown product: " + itemName;
                }

                items.add(new OrderItem(foundProduct, itemQuantity));
            }

            Stock.RemoveResult result = stock.tryRemoveOrder(items);
            if (result.refusal != null) {
                System.out.println("[" + endpoint + "] order refused: " + request + " -> " + result.refusal);
                return result.refusal;
            }
            // The stock lock is already released here: we never write to a socket while holding it.
            providers.alertLow(result.newlyLow);

            Order order = orderManager.submit(items, clientHandler);
            int preparationTime = order.getTotalPreparationTime();
            System.out.println("[" + endpoint + "] order " + order.getId() + " accepted: " + request
                    + " time=" + preparationTime + "s");
            return "ACCEPTED|" + order.getId() + "|" + preparationTime;
        }

        System.out.println("[" + endpoint + "] error: unknown request: " + request);
        return "ERROR|UNKNOWN_REQUEST";
    }
}
