package academy.pocu.comp2500.lab5.app;

import academy.pocu.comp2500.lab5.*;

public class Program {

    public static void main(String[] args) {
        {
            System.out.println("barbarian first test");
            Barbarian barbarian1 = new Barbarian("j", 100, 10, 5);
            Barbarian barbarian2 = new Barbarian("z", 5, 10, 5);

            barbarian1.attack(barbarian2);
            System.out.println(barbarian2.getHp());
        }

        {
            System.out.println("barbarian second test");
            Barbarian barbarian0 = new Barbarian("Dragonborn Whiterun", 100, 250, 10);
            Barbarian barbarian1 = new Barbarian("Ulfric Stormcloak", 100, 70, 50);

            System.out.println(barbarian1.isAlive());

            barbarian0.attack(barbarian1);

            System.out.println(barbarian1.isAlive());
        }

        {
            System.out.println("gladiator");
            Gladiator g1 = new Gladiator("henny", 100, 10, 4);

            Move wheelwind = new Move("wheelwind", 100, 5);

            boolean res1 = g1.addMove(wheelwind);
            boolean res2 = g1.addMove(wheelwind);

            System.out.println(res1);
            System.out.println(res2);

        }

        {
            Gladiator gladiator0 = new Gladiator("Dragonborn Whiterun", 100, 250, 10);
            Gladiator gladiator1 = new Gladiator("Ulfric Stormcloak", 1000, 300, 77);
            Move move = new Move("Hadoken", 120, 20);

            assert gladiator0.addMove(move); // true

            gladiator0.attack("Hadoken", gladiator1);

            System.out.println(gladiator1.getHp());
            assert gladiator1.getHp() == 806; // 806
        }

        {
            Gladiator gladiator0 = new Gladiator("Dragonborn Whiterun", 100, 250, 10);
            Gladiator gladiator1 = new Gladiator("Ulfric Stormcloak", 1000, 300, 77);
            Move move = new Move("Hadoken", 120, 20);

            gladiator0.addMove(move);

            gladiator0.attack("Hadoken", gladiator1);

            assert gladiator1.getHp() == 806; // 806

            gladiator1.rest();

            assert gladiator1.getHp() == 816; // 816
        }

        {
            Barbarian barbarian0 = new Barbarian("Dragonborn Whiterun", 250, 210, 60);
            Barbarian barbarian1 = new Barbarian("Ulfric Stormcloak", 200, 70, 10);

            barbarian0.attack(barbarian1);

            assert barbarian1.getHp() == 100;
            assert barbarian1.isAlive();

            barbarian0.attack(barbarian1);

            assert barbarian1.getHp() == 0;
            assert !barbarian1.isAlive();

            Gladiator gladiator0 = new Gladiator("Parthunax", 250, 210, 10);
            Gladiator gladiator1 = new Gladiator("Zoro", 100, 150, 65);
            Move move0 = new Move("Gomu Gomu no pistol", 50, 10);
            Move move1 = new Move("Thunderbolt", 90, 15);
            Move move2 = new Move("Ice Beam", 90, 10);
            Move move3 = new Move("Surf", 90, 15);

            assert gladiator0.addMove(move0);
            assert gladiator0.addMove(move1);
            assert gladiator0.addMove(move2);
            assert gladiator0.addMove(move3);

            assert gladiator0.removeMove("Surf");

            gladiator0.attack("Gomu Gomu no pistol", barbarian0);

            assert barbarian0.getHp() == 163;
            assert barbarian0.isAlive();

            gladiator0.attack("Gomu Gomu no pistol", gladiator1);

            assert gladiator1.getHp() == 20;

            gladiator1.rest();

            assert gladiator1.getHp() == 30;

            gladiator1.rest();
            assert gladiator1.getHp() == 40;

            gladiator1.rest();
            assert gladiator1.getHp() == 50;

            gladiator1.rest();
            assert gladiator1.getHp() == 60;

            gladiator1.rest();
            assert gladiator1.getHp() == 70;

            gladiator1.rest();
            assert gladiator1.getHp() == 80;

            gladiator1.rest();
            assert gladiator1.getHp() == 90;

            gladiator1.rest();
            assert gladiator1.getHp() == 100;

            gladiator1.rest();
            assert gladiator1.getHp() == 100;

            Knight knight0 = new Knight("Naruto", 252, 310, 99);
            Knight knight1 = new Knight("Sasuke", 250, 290, 111);
            Pet pet0 = new Pet("Giant Toad", 180);

            knight0.setPet(pet0);

            knight0.attackTogether(gladiator0);

            assert gladiator0.getHp() == 10;

            knight0.attackTogether(knight1);

            assert knight1.getHp() == 61;

        }

    }
}
