package lab2.task1.common;

import java.util.ArrayList;
import java.util.List;



public class Menu {

    private static final List<Product> PRODUCTS = List.of(
        new Product("coffee", 4),
        new Product("tea",3),
        new Product("croissant", 2)
    );


    public static String getProducts(){

        StringBuilder stringBuilder = new StringBuilder();
        for(Product p : PRODUCTS){
            stringBuilder.append("Name: " + String.valueOf(p.getName()));
            stringBuilder.append("  Preparation time: "+String.valueOf(p.getPreparationTime())+ " | ");
            // stringBuilder.append("\n");
        }

        return stringBuilder.toString();        
        // return String.valueOf(PRODUCTS); 

    }

        


         
        // return String.valueOf(PRODUCTS); 

    }
    



 


    // public Menu(){
    //     this.ProductList = new ArrayList<>();
        


    // }



