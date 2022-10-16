package academy.pocu.comp2500.lab6;

import java.util.ArrayList;

public class Pizza extends Menu {
    protected int price;
    protected ArrayList<Topping> toppings = new ArrayList<>();
    protected int meatCount;
    protected int cheeseCount;
    protected int veggieCount;
    protected boolean isVeggieAdded;
    protected boolean isCheeseAdded;

    public Pizza(int price) {
        super(price);
    }

//    public boolean isValid() {
//        return this.meatCount == MAX_MEAT_COUNT;
//    }

    public ArrayList<Topping> getToppings() {
        return this.toppings;
    }
}
