package academy.pocu.comp2500.lab5;

public class Pet {
    String name;
    int attack;

    public Pet(String name, int attack) {
        this.name = name;
        this.attack = attack;
    }

    public String getName() {
        return name;
    }

    public int getAttack() {
        return attack;
    }
}
