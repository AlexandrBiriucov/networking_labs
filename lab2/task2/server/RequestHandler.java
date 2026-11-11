package lab2.task2.server;

import java.util.ArrayList;
import java.util.List;

import lab2.task2.common.*;
import lab2.task2.orderPreparation.OrderManager;

public class RequestHandler {

    private final Stock stock;
    private OrderManager orderManager;
    private ClientHandler clientHandler;

    public RequestHandler(Stock stock, OrderManager orderManager, ClientHandler clientHandler) {
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

        else if (requestCommand.equals("ORDER")) {
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

            String refusal = stock.tryRemoveOrder(items);
            if (refusal != null) {
                System.out.println("[" + endpoint + "] order refused: " + request + " -> " + refusal);
                return refusal;
            }

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
