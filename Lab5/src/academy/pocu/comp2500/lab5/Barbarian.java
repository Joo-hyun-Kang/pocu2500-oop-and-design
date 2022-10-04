package academy.pocu.comp2500.lab5;

public class Barbarian {
    private String name;
    private int hp;
    private int attack;
    private int defense;

    public Barbarian(String name, int hp, int attack, int defense) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
    }

    public int getHp() {
        return hp;
    }

    public void attack(Barbarian target) {
        double damage = (int)((this.attack - target.defense) / 2.0);
        damage = damage < 1 ? 1 : damage;

        target.hp -= damage;
        target.hp = Math.max(target.hp, 0);
    }

    public boolean isAlive() {
        return this.hp > 0;
    }
}