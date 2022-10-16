package academy.pocu.comp2500.lab6;

import java.util.ArrayList;

public class Meal extends Menu {
    protected ArrayList<Dessert> desserts = new ArrayList<>();
    protected ArrayList<Appetizer> appetizers = new ArrayList<>();
    protected ArrayList<MainCourse> mainCourses = new ArrayList<>();

    protected Meal(int price) {
        super(price);
    }

    public ArrayList<Dessert> getDesserts() {
        return desserts;
    }

    public ArrayList<Appetizer> getAppetizers() {
        return this.appetizers;
    }

    public ArrayList<MainCourse> getMainCourse() {
        return this.mainCourses;
    }
}
