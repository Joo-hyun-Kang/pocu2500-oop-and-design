package academy.pocu.comp2500.lab6;

import java.util.ArrayList;

public class Meal extends Menu {
    protected ArrayList<Dessert> desserts = new ArrayList<>();
    protected ArrayList<Appetizer> appetizers = new ArrayList<>();
    protected MainCourse mainCourse;

    public Meal(int price) {
        super(price);
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
