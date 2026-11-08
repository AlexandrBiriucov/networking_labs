package lab2.task1.server;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lab2.task1.common.Menu;
import lab2.task1.common.Product;
public class Stock {
 
    //Client can order form it 
    //Server needs to know how many are there products left in the stock 
    // stock needs to be syncronized for the multithreading 
    private final Map<String,Integer> StockMap = new HashMap<>();

    // private Product coffee= new Product("coffe" , 4);
    // private Product tea = new Product("tea", 3);
    // // private croissant = new 



    //contructor to create the stock when server starts
    public Stock(){
        createRandomStock();
    }

    //to check the current stock
    public synchronized String getStock(){
        return StockMap.toString();

        
    }

    public Integer getQuantity(String productName){

        return StockMap.get(productName);

    }

    //to remove after the order
    public synchronized boolean tryRemoveItem(String item, int amount){

        if (item==null){
            return  false;
        }

        if (!StockMap.containsKey(item)) {
            return false;
        }

        if (amount <= 0) {
            return false;
        }

        if (StockMap.get(item) < amount){
            return false;
        }

        int newAmount = Integer.valueOf(StockMap.get(item));
        newAmount-=amount;
        StockMap.put(item,newAmount);

        return true;    

    }


    //idk 
    public synchronized void addItem(String productName, int amount){
        StockMap.put(productName,amount);

    }
    
    // public synchronized void remove()

    public void createRandomStock(){
        int min=10;
        int max=50;
        
        // int randomNumberOfProducts = (int)(((max-min)+1) * Math.random())+min;
        // for (int i=0;i<3;i++){
            
        
        // }
        
        for (Product product : Menu.getProductList()){
            int randomNumberOfProducts = (int)(((max-min)+1) * Math.random())+min;
            addItem(product.getName(),randomNumberOfProducts);
        }


    }







}
