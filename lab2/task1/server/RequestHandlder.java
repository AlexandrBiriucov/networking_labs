

package lab2.task1.server;


public class RequestHandlder{



    public void handleRequest(String request){

        if (request.equals("MENU")){
            return getMenu();
        }
    
        else if (request.startsWith("ORDER")) {
        return processOrder(request);
        }

        return "ERROR|UNKNOWN_REQUEST";



    }






}