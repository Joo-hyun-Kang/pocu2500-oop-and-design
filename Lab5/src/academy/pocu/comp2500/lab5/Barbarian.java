package academy.pocu.comp2500.lab5;

public class Barbarian {
    protected String name;
    protected int hp;
    protected int attack;
    protected int defense;
    protected final int MAX_HP;

    public Barbarian(String name, int hp, int attack, int defense) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.MAX_HP = hp;
    }

    public int getHp() {
        return hp;
    }

    public void attack(Barbarian target) {
        if (this.name.equals(target.name) || !isAlive()) {
            return;
        }
        double damage = (int) ((this.attack - target.defense) / 2.0);
        damage = damage < 1 ? 1 : damage;

        target.hp -= damage;
        target.hp = Math.max(target.hp, 0);
    }

    public boolean isAlive() {
        return this.hp > 0;
    }
}
