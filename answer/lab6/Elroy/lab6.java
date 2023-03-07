=========================================================
// Menu.java
package academy.pocu.comp2500.lab6;

public class Menu {
    private final int price;
    protected boolean isValid;

    public boolean isValid() {
        return isValid;
    }

    protected Menu(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
=========================================================
// Pizza.java
package academy.pocu.comp2500.lab6;

import java.util.ArrayList;

public class Pizza extends Menu {
    private final int maxMeatCount;
    private final int maxVegetableCount;
    private final int maxCheeseCount;
    private final ArrayList<Topping> toppings = new ArrayList<>();

    private int meatCount = 0;
    private int cheeseCount = 0;
    private int vegetableCount = 0;

    protected Pizza(int price, int maxMeatCount, int maxVegetableCount, int maxCheeseCount) {
        super(price);
        this.maxMeatCount = maxMeatCount;
        this.maxVegetableCount = maxVegetableCount;
        this.maxCheeseCount = maxCheeseCount;
    }

    private void checkValid() {
        this.isValid = (meatCount == maxMeatCount && vegetableCount == maxVegetableCount && cheeseCount == maxCheeseCount);
    }

    public ArrayList<Topping> getToppings() {
        return this.toppings;
    }

    protected boolean addPizzaTopping(Topping topping) {
        if (!canAdd(topping)) {
            return false;
        }

        switch (topping.toppingType) {
            case MEAT:
                ++this.meatCount;
                break;

            case VEGETABLE:
                ++this.vegetableCount;
                break;

            case CHEESE:
                ++this.cheeseCount;
                break;

            default:
                assert (false) : "Unknown Type";
                break;
        }

        this.toppings.add(topping);
        checkValid();
        return true;
    }

    protected boolean removePizzaTopping(Topping topping) {
        if (!this.toppings.contains(topping)) {
            return false;
        }

        switch (topping.toppingType) {
            case MEAT:
                --this.meatCount;
                break;

            case VEGETABLE:
                --this.vegetableCount;
                break;

            case CHEESE:
                --this.cheeseCount;
                break;

            default:
                assert (false) : "Unknown Type";
                break;
        }

        this.toppings.remove(topping);
        checkValid();
        return true;
    }

    private boolean canAdd(Topping topping) {
        switch (topping.toppingType) {
            case MEAT:
                return (meatCount < maxMeatCount);

            case VEGETABLE:
                return (vegetableCount < maxVegetableCount);

            case CHEESE:
                return (cheeseCount < maxCheeseCount);

            default:
                assert (false) : "Unknown Type";
                return false;
        }
    }
}
=========================================================
// Course.java
package academy.pocu.comp2500.lab6;

import java.util.ArrayList;

public class Course extends Menu {
    private final int maxAppetizerCount;
    private final int maxMainCourseCount;
    private final int maxDessertCount;

    private ArrayList<Appetizer> appetizers = new ArrayList<>();
    private ArrayList<MainCourse> mainCourses = new ArrayList<>();
    private ArrayList<Dessert> desserts = new ArrayList<>();

    protected Course(int price, int maxAppetizerCount, int maxMainCourseCount, int maxDessertCount) {
        super(price);
        this.maxAppetizerCount = maxAppetizerCount;
        this.maxMainCourseCount = maxMainCourseCount;
        this.maxDessertCount = maxDessertCount;
    }

    private void checkValid() {
        this.isValid = (appetizers.size() == maxAppetizerCount && mainCourses.size() == maxMainCourseCount && desserts.size() == maxDessertCount);
    }

    public ArrayList<Appetizer> getAppetizers() {
        return appetizers;
    }

    public ArrayList<MainCourse> getMainCourses() {
        return mainCourses;
    }

    public ArrayList<Dessert> getDesserts() {
        return desserts;
    }

    protected void setAppetizers(ArrayList<Appetizer> appetizers) {
        this.appetizers = appetizers;
        checkValid();
    }

    protected void setMainCourses(ArrayList<MainCourse> mainCourses) {
        this.mainCourses = mainCourses;
        checkValid();
    }

    protected void setDesserts(ArrayList<Dessert> desserts) {
        this.desserts = desserts;
        checkValid();
    }
}
=========================================================
// FreeSoulPizza.java
package academy.pocu.comp2500.lab6;

public class FreeSoulPizza extends Pizza {
    public FreeSoulPizza() {
        super(25, 2, 2, 1);
    }

    public boolean addTopping(Topping topping) {
        return super.addPizzaTopping(topping);
    }

    public boolean removeTopping(Topping topping) {
        return super.removePizzaTopping(topping);
    }
}
=========================================================
// HousePizza.java
package academy.pocu.comp2500.lab6;

public class HousePizza extends Pizza {
    public HousePizza() {
        super(20, 2, 3, 1);
        addPizzaTopping(Topping.BLACK_OLIVES);
        addPizzaTopping(Topping.RED_ONIONS);
        addPizzaTopping(Topping.GREEN_PEPPERS);
        addPizzaTopping(Topping.MOZZARELLA_CHEESE);
    }

    public boolean addBacon() {
        return addPizzaTopping(Topping.BACON);
    }

    public boolean removeBacon() {
        return removePizzaTopping(Topping.BACON);
    }

    public boolean addPeperoni() {
        return addPizzaTopping(Topping.PEPERONI);
    }

    public boolean removePeperoni() {
        return removePizzaTopping(Topping.PEPERONI);
    }

    public boolean addSausages() {
        return addPizzaTopping(Topping.SAUSAGES);
    }

    public boolean removeSausages() {
        return removePizzaTopping(Topping.SAUSAGES);
    }
}
=========================================================
// MeatLoverPizza.java
package academy.pocu.comp2500.lab6;

public class MeatLoverPizza extends Pizza {
    public MeatLoverPizza() {
        super(21, 4, 1, 1);
        addPizzaTopping(Topping.BACON);
        addPizzaTopping(Topping.PEPERONI);
        addPizzaTopping(Topping.HAM);
        addPizzaTopping(Topping.SAUSAGES);
        addPizzaTopping(Topping.CHEDDAR_CHEESE);
    }

    public boolean addBlackOlives() {
        return addPizzaTopping(Topping.BLACK_OLIVES);
    }

    public boolean removeBlackOlives() {
        return removePizzaTopping(Topping.BLACK_OLIVES);
    }

    public boolean addRedOnions() {
        return addPizzaTopping(Topping.RED_ONIONS);
    }

    public boolean removeRedOnions() {
        return removePizzaTopping(Topping.RED_ONIONS);
    }

    public boolean addGreenPeppers() {
        return addPizzaTopping(Topping.GREEN_PEPPERS);
    }

    public boolean removeGreenPeppers() {
        return removePizzaTopping(Topping.GREEN_PEPPERS);
    }
}
=========================================================
// VeggiePizza.java
package academy.pocu.comp2500.lab6;

public class VeggiePizza extends Pizza {
    public VeggiePizza() {
        super(17, 0, 3, 2);
        addPizzaTopping(Topping.BLACK_OLIVES);
        addPizzaTopping(Topping.RED_ONIONS);
        addPizzaTopping(Topping.GREEN_PEPPERS);
    }

    public boolean addMozzarellaCheese() {
        return addPizzaTopping(Topping.MOZZARELLA_CHEESE);
    }

    public boolean removeMozzarellaCheese() {
        return removePizzaTopping(Topping.MOZZARELLA_CHEESE);
    }

    public boolean addCheddarCheese() {
        return addPizzaTopping(Topping.CHEDDAR_CHEESE);
    }

    public boolean removeCheddarCheese() {
        return removePizzaTopping(Topping.CHEDDAR_CHEESE);
    }

    public boolean addFetaCheese() {
        return addPizzaTopping(Topping.FETA_CHEESE);
    }

    public boolean removeFetaCheese() {
        return removePizzaTopping(Topping.FETA_CHEESE);
    }
}
=========================================================
// DeathByDesserts.java
package academy.pocu.comp2500.lab6;

import java.util.ArrayList;
import java.util.List;

public class DeathByDesserts extends Course {
    public DeathByDesserts() {
        super(20, 0, 0, 4);
    }

    public void setDesserts(Dessert dessert1, Dessert dessert2, Dessert dessert3, Dessert dessert4) {
        setDesserts(new ArrayList<>(List.of(dessert1, dessert2, dessert3, dessert4)));
    }
}
=========================================================
// NoHeavyMeal.java
package academy.pocu.comp2500.lab6;

import java.util.ArrayList;
import java.util.List;

public class NoHeavyMeal extends Course {
    public NoHeavyMeal() {
        super(15, 2, 0, 1);
    }

    public void setAppetizers(Appetizer appetizer1, Appetizer appetizer2) {
        setAppetizers(new ArrayList<>(List.of(appetizer1, appetizer2)));
    }

    public void setDessert(Dessert dessert) {
        setDesserts(new ArrayList<>(List.of(dessert)));
    }
}
=========================================================
// ThreeCourseMeal.java
package academy.pocu.comp2500.lab6;

import java.util.ArrayList;
import java.util.List;

public class ThreeCourseMeal extends Course {
    public ThreeCourseMeal() {
        super(25, 1, 1, 1);
    }

    public void setMainCourse(MainCourse mainCourse) {
        setMainCourses(new ArrayList<>(List.of(mainCourse)));
    }

    public void setAppetizer(Appetizer appetizer) {
        setAppetizers(new ArrayList<>(List.of(appetizer)));
    }

    public void setDessert(Dessert dessert) {
        setDesserts(new ArrayList<>(List.of(dessert)));
    }
}
=========================================================
// Appetizer.java
package academy.pocu.comp2500.lab6;

public enum Appetizer {
    CALAMARI,
    GYOZA,
    SPINACH_DIP,
    NACHOS
}
=========================================================
// Dessert.java
package academy.pocu.comp2500.lab6;

public enum Dessert {
    MANGO_PUDDING,
    GREEN_TEA_ICE_CREAM,
    CHOCOLATE_MOUSSE,
    APPLE_PIE,
    ITALIAN_DONUTS
}
=========================================================
// MainCourse.java
package academy.pocu.comp2500.lab6;

public enum MainCourse {
    JAMBALAYA_FETTUCCINE,
    FISH_AND_CHIPS,
    AHI_TUNA_POKE,
    BOMBAY_BUTTER_CHICKEN,
    FILET_MIGNON,
    TERIYAKI_CHICKEN_RICE_BOWL
}
=========================================================
// Topping.java
package academy.pocu.comp2500.lab6;

public enum Topping {
    CHICKEN(ToppingType.MEAT),
    PEPERONI(ToppingType.MEAT),
    SAUSAGES(ToppingType.MEAT),
    HAM(ToppingType.MEAT),
    BACON(ToppingType.MEAT),
    BLACK_OLIVES(ToppingType.VEGETABLE),
    RED_ONIONS(ToppingType.VEGETABLE),
    GREEN_PEPPERS(ToppingType.VEGETABLE),
    MOZZARELLA_CHEESE(ToppingType.CHEESE),
    CHEDDAR_CHEESE(ToppingType.CHEESE),
    FETA_CHEESE(ToppingType.CHEESE);

    enum ToppingType {
        MEAT,
        VEGETABLE,
        CHEESE;
    }
    
    final ToppingType toppingType;

    Topping(ToppingType toppingType) {
        this.toppingType = toppingType;
    }
}