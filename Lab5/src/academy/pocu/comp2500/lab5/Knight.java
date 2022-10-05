package academy.pocu.comp2500.lab5;

public class Knight extends Barbarian {
    private Pet pet;

    public Knight(String name, int hp, int attack, int defense) {
        super(name, hp, attack, defense);
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void attackTogether(Barbarian target) {
        if (super.name.equals(target.name) || !isAlive() || pet == null) {
            return ;
        }

        double damage = (int) ((super.attack + pet.attack - target.defense) / 2.0);
        damage = Math.max(damage, 1);

        target.hp -= damage;
        target.hp = Math.max(target.hp, 0);
    }
}
