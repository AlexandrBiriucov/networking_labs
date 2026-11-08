package lab2.task1.common;

import lab2.task1.server.ClientHandler;

public class OrderTask {
    
    private Order order;
    private ClientHandler clientHandler;

    public OrderTask(Order order,ClientHandler clientHandler){
        this.order=order;
        this.clientHandler=clientHandler;


    }



    public Order getOrder(){
        return this.order;
    }


    public ClientHandler getClientHandler(){
        return this.clientHandler;

    }



    







}
