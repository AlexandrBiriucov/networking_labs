

package lab2.task1.server;


import lab2.task1.common.*;


public class RequestHandler{







    public String handleRequest(String request){

        if (request.equals("MENU")){
            System.out.println("sending back menu");
            return Menu.getProducts();
        }
    
        // else if (request.startsWith("ORDER")) {
        // return processOrder(request);
        // }

        return "ERROR|UNKNOWN_REQUEST";



    }






}