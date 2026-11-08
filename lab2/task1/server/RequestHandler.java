

package lab2.task1.server;


import java.util.Arrays;

import lab2.task1.common.*;
import lab2.task1.orderPreparation.OrderManager;


public class RequestHandler{

    private final Stock stock;
    private OrderManager orderManager;
    private ClientHandler clientHandler;
    public RequestHandler(Stock stock,OrderManager orderManager,ClientHandler clientHandler){
        this.stock = stock;
        this.orderManager= orderManager;
        this.clientHandler= clientHandler;
    }



    public String handleRequest(String request){
        System.out.println(">>> REQUEST HANDLER CALLED <<<");
        System.out.println("Raw request: [" + request + "]");
        String[] splitStr = request.split("\\|");
        String requestCommand = splitStr[0];
        

        if (requestCommand.equals("MENU")){
            StringBuilder stringBuilder = new StringBuilder();
            for (Product product : Menu.getProductList()){
                stringBuilder.append(String.format(
                "%s | %d sec | %d available ; ",
                product.getName(),
                product.getPreparationTime(),
                stock.getQuantity(product.getName())
             ));

            }
            System.out.println("sending back menu");
            
            return stringBuilder.toString();
        }
    

        else if (requestCommand.equals("ORDER")) {
        System.out.println("Received: [" + request + "]");
        System.out.println("Length: " + splitStr.length);
        System.out.println(Arrays.toString(splitStr));
    // 1. Validate the request format
            if (splitStr.length < 3 || (splitStr.length - 1) % 2 != 0) {
                return "Invalid order format. Use ORDER|product|quantity";
            }

            Order order = new Order();

            // 2. first pas: validate everything and build the Order
            for (int i = 1; i < splitStr.length; i += 2) {

                String itemName = splitStr[i];
                int itemQuantity;

                // Parse quantity
                try {
                    itemQuantity = Integer.parseInt(splitStr[i + 1]);
                } catch (NumberFormatException e) {
                    return "Invalid quantity for " + itemName;
                }

                if (itemQuantity <= 0) {
                    return "Quantity must be greater than zero";
                }

                // Find product in the menu
                Product foundProduct = null;

                for (Product product : Menu.getProductList()) {
                    if (product.getName().equalsIgnoreCase(itemName)) {
                        foundProduct = product;
                        break;
                    }
                }

                if (foundProduct == null) {
                    return "Unknown product: " + itemName;
                }

                // Check available stock
                int available = stock.getQuantity(foundProduct.getName());

                if (available < itemQuantity) {
                    return "Not enough " + itemName + ". Available: " + available;
                }

                // Add item to the order
                OrderItem orderItem = new OrderItem(foundProduct, itemQuantity);
                order.addItemOrder(orderItem);
            }

            // 3 second pass: reserve all requested products
            for (int i = 1; i < splitStr.length; i += 2) {

                String itemName = splitStr[i];
                int itemQuantity = Integer.parseInt(splitStr[i + 1]);

                stock.tryRemoveItem(itemName, itemQuantity);
            }

            // 4. Calculate preparation time and respond
            int preparationTime = order.getTotalPreparationTime();


            //add the order to the queue
            
            orderManager.addOrder(order,clientHandler);

            return "ACCEPTED|" + order.getId() + "|" + preparationTime;
        }




        // else if (request.startsWith("ORDER")) {
        // return processOrder(request);
        // }

        return "ERROR|UNKNOWN_REQUEST";



    }






}