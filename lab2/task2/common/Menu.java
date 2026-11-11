package lab2.task2.common;

import java.util.List;

public class Menu {

    private static final List<Product> PRODUCTS = List.of(
        new Product("coffee", 4),
        new Product("tea", 3),
        new Product("croissant", 2)
    );

    public static String getProducts() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Product p : PRODUCTS) {
            stringBuilder.append("Name: " + p.getName());
            stringBuilder.append("  Preparation time: " + p.getPreparationTime() + " | ");
        }
        return stringBuilder.toString();
    }

    public static List<Product> getProductList() {
        return PRODUCTS;
    }
}
