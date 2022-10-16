package academy.pocu.comp2500.lab6.app;

import academy.pocu.comp2500.lab6.HousePizza;
import academy.pocu.comp2500.lab6.MeatLoverPizza;
import academy.pocu.comp2500.lab6.Topping;

import java.util.ArrayList;

public class Program {

    public static void main(String[] args) {
	    // write your code here

        HousePizza housePizza = new HousePizza();
        //int price = housePizza.getPrice();


        MeatLoverPizza meatLoverPizza = new MeatLoverPizza();
        boolean isAdded = meatLoverPizza.addGreenPeppers(); // true
        boolean isRemoved = meatLoverPizza.removeGreenPeppers(); // true
        isAdded = meatLoverPizza.addRedOnions(); // true
        boolean isValid = meatLoverPizza.isValid(); // true
        int price = meatLoverPizza.getPrice(); // 21
        ArrayList<Topping> toppings = meatLoverPizza.getToppings();
    }
}
