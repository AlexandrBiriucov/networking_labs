package lab2.task2.common;

public class Product {

    private String name;
    private int preparationTime;

    public Product(String name, int preparationTime) {
        this.name = name;
        this.preparationTime = preparationTime;
    }

    public String getName() {
        return this.name;
    }

    public int getPreparationTime() {
        return this.preparationTime;
    }
}
