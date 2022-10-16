package academy.pocu.comp2500.lab6;

import java.util.ArrayList;

public class DeathByDesserts {
    private static final int PRICE = 20;

    private int price = PRICE;
    private boolean isValid;
    private ArrayList<Dessert> desserts = new ArrayList<>();

    public int getPrice() {
        return this.price;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public ArrayList<Dessert> getDesserts() {
        return this.desserts;
    }

    public void setDesserts(Dessert dessert1, Dessert dessert2, Dessert dessert3, Dessert dessert4) {
        this.desserts.clear();

        this.desserts.add(dessert1);
        this.desserts.add(dessert2);
        this.desserts.add(dessert3);
        this.desserts.add(dessert4);
        this.isValid = true;
    }

}
