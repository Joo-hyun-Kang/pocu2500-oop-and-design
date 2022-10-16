package academy.pocu.comp2500.lab6;

public class ThreeCourseMeal extends Meal {
    private static final int PRICE = 25;

//    private int price = PRICE;
//    private Appetizer appetizer;

//    private Dessert dessert;

    public ThreeCourseMeal() {
        super(PRICE);
    }

//    public int getPrice() {
//        return this.price;
//    }

    public boolean isValid() {
        return this.appetizers.size() != 0 && this.mainCourses.size() != 0 && this.desserts.size() != 0;
    }

//    public Appetizer getAppetizer() {
//        assert (this.appetizer != null) : "call isValid() first!";
//        return this.appetizer;
//    }



//    public Dessert getDessert() {
//        assert (this.dessert != null) : "call isValid() first!";
//        return this.dessert;
//    }

    public void setMainCourse(MainCourse mainCourse) {
        this.mainCourses.add(mainCourse);
    }

    public void setAppetizer(Appetizer appetizer) {
        this.appetizers.add(appetizer);
    }

    public void setDessert(Dessert dessert) {
        this.desserts.add(dessert);
    }
}
