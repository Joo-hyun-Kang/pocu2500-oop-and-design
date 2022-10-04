package academy.pocu.comp2500.lab5.app;

import academy.pocu.comp2500.lab5.Barbarian;

public class Program {

    public static void main(String[] args) {
        {
            Barbarian barbarian1 = new Barbarian("j", 100, 10, 5);
            Barbarian barbarian2 = new Barbarian("z", 5, 10, 5);

            barbarian1.attack(barbarian2);
            System.out.println(barbarian2.getHp());
        }

        {
            Barbarian barbarian0 = new Barbarian("Dragonborn Whiterun", 100, 250, 10);
            Barbarian barbarian1 = new Barbarian("Ulfric Stormcloak", 100, 70, 50);

            System.out.println(barbarian1.isAlive());

            barbarian0.attack(barbarian1);

            System.out.println(barbarian1.isAlive());
        }

    }
}
