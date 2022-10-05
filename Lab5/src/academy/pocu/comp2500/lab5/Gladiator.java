package academy.pocu.comp2500.lab5;

import java.util.ArrayList;

public class Gladiator extends Barbarian {
    private ArrayList<Move> moves;

    public Gladiator(String name, int hp, int attack, int defense) {
        super(name, hp, attack, defense);
        moves = new ArrayList<>();
    }

    public boolean addMove(Move move) {
        if (getMoveOrNull(move.getName()) != null || moves.size() >= 4) {
            return false;
        }
        moves.add(move);

        return true;
    }

    public boolean removeMove(String moveName) {
        Move move = getMoveOrNull(moveName);
        if (move != null) {
            moves.remove(move);
            return true;
        }
        return false;
    }

    public void attack(String moveName, Barbarian target) {
        if (super.name.equals(target.name) || !isAlive()) {
            return;
        }

        Move move = getMoveOrNull(moveName);
        if (move == null || move.getPowerPoint() < 1) {
            return;
        }

        double damage = (int) (((double) super.attack / target.defense * move.getPower()) / 2.0);
        move.minusPowerPoint();
        damage = Math.max(damage, 1);

        target.hp -= damage;
        target.hp = Math.max(target.hp, 0);
    }

    public void rest() {
        if (!isAlive()) {
            return;
        }

        super.hp = Math.min(super.hp + 10, super.maxHp);

        for (Move move : moves) {
            if (move.getPower() < move.getMaxPowerPoint()) {
                move.plusPowerPoint();
            }
        }
    }

    private Move getMoveOrNull(String name) {
        int index = -1;
        for (int i = 0; i < moves.size(); i++) {
            if (moves.get(i).getName().equals(name)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            return moves.get(index);
        }
        return null;
    }
}
