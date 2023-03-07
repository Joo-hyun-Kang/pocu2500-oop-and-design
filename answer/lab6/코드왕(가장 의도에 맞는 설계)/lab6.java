public class Menu {
    protected int price;
    protected boolean isValid;

    protected Menu(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public boolean isValid() {
        return isValid;
    }
}

public class Pizza extends Menu {
    protected ArrayList<Topping> toppings = new ArrayList<>();

    protected Pizza(int price) {
        super(price);
    }

    public ArrayList<Topping> getToppings() {
        return toppings;
    }
}

public class HousePizza extends Pizza {
    private static final int PRICE = 20;
    private static final int MAX_MEAT_COUNT = 2;
    private int meatCount;

    public HousePizza() {
        super(PRICE);
        this.toppings.add(Topping.BLACK_OLIVES);
        this.toppings.add(Topping.RED_ONIONS);
        this.toppings.add(Topping.GREEN_PEPPERS);
        this.toppings.add(Topping.MOZZARELLA_CHEESE);
    }

    private void updateValidity() {
        isValid = this.meatCount == MAX_MEAT_COUNT;
    }

    public boolean addBacon() {
        if (isValid()) {
            return false;
        }

        this.toppings.add(Topping.BACON);
        ++this.meatCount;

        updateValidity();

        return true;
    }

    public boolean removeBacon() {
        boolean isRemoved = this.toppings.remove(Topping.BACON);

        if (isRemoved) {
            --this.meatCount;
        }

        updateValidity();

        return isRemoved;
    }

    public boolean addPeperoni() {
        if (isValid()) {
            return false;
        }

        this.toppings.add(Topping.PEPERONI);
        ++this.meatCount;

        updateValidity();

        return true;
    }

    public boolean removePeperoni() {
        boolean isRemoved = this.toppings.remove(Topping.PEPERONI);

        if (isRemoved) {
            --this.meatCount;
        }

        updateValidity();

        return isRemoved;
    }

    public boolean addSausages() {
        if (isValid()) {
            return false;
        }

        this.toppings.add(Topping.SAUSAGES);
        ++this.meatCount;

        updateValidity();

        return true;
    }

    public boolean removeSausages() {
        boolean isRemoved = this.toppings.remove(Topping.SAUSAGES);

        if (isRemoved) {
            --this.meatCount;
        }

        updateValidity();

        return isRemoved;
    }
}

public class MeatLoverPizza extends Pizza {
    private static final int PRICE = 21;

    public MeatLoverPizza() {
        super(PRICE);
        this.toppings.add(Topping.BACON);
        this.toppings.add(Topping.PEPERONI);
        this.toppings.add(Topping.HAM);
        this.toppings.add(Topping.SAUSAGES);
        this.toppings.add(Topping.CHEDDAR_CHEESE);
    }

    public boolean addBlackOlives() {
        if (isValid()) {
            return false;
        }

        this.toppings.add(Topping.BLACK_OLIVES);
        this.isValid = true;
        return true;
    }

    public boolean removeBlackOlives() {
        boolean isRemoved = this.toppings.remove(Topping.BLACK_OLIVES);

        if (isRemoved) {
            this.isValid = false;
        }

        return isRemoved;
    }

    public boolean addRedOnions() {
        if (isValid()) {
            return false;
        }

        this.toppings.add(Topping.RED_ONIONS);
        this.isValid = true;
        return true;
    }

    public boolean removeRedOnions() {
        boolean isRemoved = this.toppings.remove(Topping.RED_ONIONS);

        if (isRemoved) {
            this.isValid = false;
        }

        return isRemoved;
    }

    public boolean addGreenPeppers() {
        if (isValid()) {
            return false;
        }

        this.toppings.add(Topping.GREEN_PEPPERS);
        this.isValid = true;
        return true;
    }

    public boolean removeGreenPeppers() {
        boolean isRemoved = this.toppings.remove(Topping.GREEN_PEPPERS);

        if (isRemoved) {
            this.isValid = false;
        }

        return isRemoved;
    }
}

public class VeggiePizza extends Pizza {
    private static final int PRICE = 17;
    private static final int MAX_CHEESE_COUNT = 2;

    private int cheeseCount;

    public VeggiePizza() {
        super(PRICE);
        this.toppings.add(Topping.BLACK_OLIVES);
        this.toppings.add(Topping.RED_ONIONS);
        this.toppings.add(Topping.GREEN_PEPPERS);
    }


    private void updateValidity() {
        isValid = this.cheeseCount == MAX_CHEESE_COUNT;
    }

    public boolean addMozzarellaCheese() {
        if (isValid()) {
            return false;
        }

        this.toppings.add(Topping.MOZZARELLA_CHEESE);
        ++this.cheeseCount;

        updateValidity();

        return true;
    }

    public boolean removeMozzarellaCheese() {
        boolean isRemoved = this.toppings.remove(Topping.MOZZARELLA_CHEESE);

        if (isRemoved) {
            --this.cheeseCount;
        }

        updateValidity();

        return isRemoved;
    }

    public boolean addCheddarCheese() {
        if (isValid()) {
            return false;
        }

        this.toppings.add(Topping.CHEDDAR_CHEESE);
        ++this.cheeseCount;

        updateValidity();

        return true;
    }

    public boolean removeCheddarCheese() {
        boolean isRemoved = this.toppings.remove(Topping.CHEDDAR_CHEESE);

        if (isRemoved) {
            --this.cheeseCount;
        }

        updateValidity();

        return isRemoved;
    }

    public boolean addFetaCheese() {
        if (isValid()) {
            return false;
        }

        this.toppings.add(Topping.FETA_CHEESE);
        ++this.cheeseCount;

        updateValidity();

        return true;
    }

    public boolean removeFetaCheese() {
        boolean isRemoved = this.toppings.remove(Topping.FETA_CHEESE);

        if (isRemoved) {
            --this.cheeseCount;
        }

        updateValidity();

        return isRemoved;
    }
}


public class ComboMeal extends Menu {
    protected ArrayList<Dessert> desserts = new ArrayList<>();
    protected ArrayList<Appetizer> appetizers = new ArrayList<>();
    protected ArrayList<MainCourse> mainCourses = new ArrayList<>();


    protected ComboMeal(int price) {
        super(price);
    }

    public ArrayList<Appetizer> getAppetizers() {
        return this.appetizers;
    }

    public ArrayList<MainCourse> getMainCourses() {
        return this.mainCourses;
    }

    public ArrayList<Dessert> getDesserts() {
        return desserts;
    }

}

public class ThreeCourseMeal extends ComboMeal {
    private static final int PRICE = 25;

    public ThreeCourseMeal() {
        super(PRICE);
    }

    private void updateValidity() {
        isValid = this.appetizers.size() == 1 && this.mainCourses.size() == 1 && this.desserts.size() == 1;
    }

    public void setMainCourse(MainCourse mainCourse) {
        this.mainCourses.clear();
        this.mainCourses.add(mainCourse);

        updateValidity();
    }

    public void setAppetizer(Appetizer appetizer) {
        this.appetizers.clear();
        this.appetizers.add(appetizer);

        updateValidity();
    }

    public void setDessert(Dessert dessert) {
        this.desserts.clear();
        this.desserts.add(dessert);

        updateValidity();
    }
}

public class NoHeavyMeal extends ComboMeal {
    private static final int PRICE = 15;
    public NoHeavyMeal() {
        super(PRICE);
    }

    private void updateValidity() {
        isValid = this.appetizers.size() == 2 && this.desserts.size() == 1 && this.mainCourses.size() == 0;
    }    

    public void setAppetizers(Appetizer appetizer1, Appetizer appetizer2) {
        this.appetizers.clear();

        this.appetizers.add(appetizer1);
        this.appetizers.add(appetizer2);

        updateValidity();
    }

    public void setDessert(Dessert dessert) {
        this.desserts.clear();
        this.desserts.add(dessert);

        updateValidity();
    }
}

public class DeathByDesserts extends ComboMeal {
    private static final int PRICE = 20;


    public DeathByDesserts() {
        super(PRICE);
    }

    private void updateValidity() {
        isValid = this.appetizers.size() == 0 && this.mainCourses.size() == 0 && this.desserts.size() == 4;
    }

    public void setDesserts(Dessert dessert1, Dessert dessert2, Dessert dessert3, Dessert dessert4) {
        this.desserts.clear();

        this.desserts.add(dessert1);
        this.desserts.add(dessert2);
        this.desserts.add(dessert3);
        this.desserts.add(dessert4);

        updateValidity();
    }
}

public enum Appetizer {
    CALAMARI,
    GYOZA,
    SPINACH_DIP,
    NACHOS
}

public enum Dessert {
    MANGO_PUDDING,
    GREEN_TEA_ICE_CREAM,
    CHOCOLATE_MOUSSE,
    APPLE_PIE,
    ITALIAN_DONUTS
}

public enum MainCourse {
    JAMBALAYA_FETTUCCINE,
    FISH_AND_CHIPS,
    AHI_TUNA_POKE,
    BOMBAY_BUTTER_CHICKEN,
    FILET_MIGNON,
    TERIYAKI_CHICKEN_RICE_BOWL
}

public enum Topping {
    CHICKEN,
    PEPERONI,
    SAUSAGES,
    HAM,
    BACON,
    BLACK_OLIVES,
    RED_ONIONS,
    GREEN_PEPPERS,
    MOZZARELLA_CHEESE,
    CHEDDAR_CHEESE,
    FETA_CHEESE
}