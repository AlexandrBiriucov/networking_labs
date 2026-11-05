import java.util.List;

import lab2.task1.common.Product;

public class StrVakueTest {

    

private static final List<Product> PRODUCTS = List.of(
        new Product("coffee", 4),
        new Product("tea",3),
        new Product("croissant", 2)
    );


    public static String getProducts(){

        StringBuilder stringBuilder = new StringBuilder();
        for(Product p : PRODUCTS){
            stringBuilder.append(" Name: " + String.valueOf(p.getName()));
            stringBuilder.append("  Preparation time: "+String.valueOf(p.getPreparationTime())+ " | ");
            // stringBuilder.append("\n");
        }

        return stringBuilder.toString();        
        // return String.valueOf(PRODUCTS); 

    }
    



    public static void main(String[] args){
        
       System.out.println( getProducts());
       String newStr = getProducts();
       System.out.println(newStr.equals("coffee4tea3croissant2"));

    }


}
