package academy.pocu.comp2500.lab6;

import java.util.ArrayList;

public class Meal {
    protected int price;
    protected ArrayList<Dessert> desserts = new ArrayList<>();
    protected ArrayList<Appetizer> appetizers = new ArrayList<>();
    protected MainCourse mainCourse;

    public Meal(int price) {
        this.price = price;
    }

    public int getPrice() {
        return this.price;
    }

    public ArrayList<Dessert> getDesserts() {
        return desserts;
    }

    public ArrayList<Appetizer> getAppetizers() {
        return this.appetizers;
    }

    public MainCourse getMainCourse() {
        assert (this.mainCourse != null) : "call isValid() first!";
        return this.mainCourse;
    }
}
