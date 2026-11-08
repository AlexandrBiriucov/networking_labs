package lab2.task1.common;


import java.util.ArrayList;
import java.util.List;
public class Order {
    
    private static int nextId =0;
    private int totalPreparationTime;
    private  List<OrderItem> items; 
    private final int id;

    public Order(){
        // this.nextId++;
        this.items = new ArrayList<>();
        this.id = ++nextId;
        // this.totalPreparationTime=quantity * preparationTime;
    }


    public int getId(){
        return this.id;
        

    }


    public void addItemOrder(OrderItem item){
        items.add(item);
    }






    public int getTotalPreparationTime(){
        int T =0;
        for (OrderItem orderItem : items ){
            T  += orderItem.getQuantity()*orderItem.getProduct().getPreparationTime();    
        }
        return T;
    }
}
