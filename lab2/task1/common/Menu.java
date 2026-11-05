package lab2.task1.common;

import java.util.ArrayList;
import java.util.List;



public class Menu {

    private static final List<Product> PRODUCTS = List.of(
        new Product("coffee", 4),
        new Product("tea",3),
        new Product("croissant", 2)
    );


    public  List<Product> getProducts(){
        return PRODUCTS; 

    }
    






    // public Menu(){
    //     this.ProductList = new ArrayList<>();
        


    // }



}
