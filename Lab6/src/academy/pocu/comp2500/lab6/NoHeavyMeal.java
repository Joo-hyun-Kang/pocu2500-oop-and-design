package academy.pocu.comp2500.lab6;

import java.util.ArrayList;

public class NoHeavyMeal extends Meal {
    private static final int PRICE = 15;

    public NoHeavyMeal() {
        super(PRICE);
    }

//    public int getPrice() {
//        return this.price;
//    }

    public boolean isValid() {
        return this.appetizers.size() == 2 && this.desserts.size() != 0;
    }

//    public Dessert getDessert() {
//        assert (this.dessert != null) : "call isValid() first!";
//        return this.dessert;
//    }

    public void setAppetizers(Appetizer appetizer1, Appetizer appetizer2) {
        this.appetizers.clear();

        this.appetizers.add(appetizer1);
        this.appetizers.add(appetizer2);
    }

    public void setDessert(Dessert dessert) {
        this.desserts.add(dessert);
    }
}
