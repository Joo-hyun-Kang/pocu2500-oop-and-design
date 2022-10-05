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
        if (super.name.equals(target.name)) {
            return;
        }

        Move move = getMoveOrNull(moveName);
        if (move == null || move.getPowerPoint() < 1) {
            return;
        }

        double damage = (int) (((double) super.attack / (target.defense * move.getPower())) / 2.0);
        damage = Math.max(damage, 1);

        target.hp -= damage;
        target.hp = Math.max(target.hp, 0);
    }

    public void rest() {
        super.hp += 10;
        moves.forEach(move -> move.plusPowerPoint());
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

    /*
3.4 또 다른 attack() 메서드를 구현한다
이 메서드는 다음의 인자들을 받습니다.

적을 공격할 때 사용할 스킬의 이름: String
공격할 적
지정된 공격 스킬을 사용하여 적을 공격합니다.

이 메서드는 아무것도 반환하지 않습니다.

그 스킬을 모를 경우 공격에 실패합니다.

남아있는 파워 수치가 충분치 않다면 공격에 실패합니다.

적이 받는 피해치는 다음과 같이 계산합니다.

피해치 = (공격자의 공격력 / 방어자의 방어력 * 스킬의 파워) / 2
계산을 할 때는 double 자료형을 사용하고 계산 뒤에는 소수점 이하는 버리세요.

공격에 성공하면 최소 1의 피해를 입힙니다.

Gladiator gladiator0 = new Gladiator("Dragonborn Whiterun", 100, 250, 10);
Gladiator gladiator1 = new Gladiator("Ulfric Stormcloak", 1000, 300, 77);
Move move = new Move("Hadoken", 120, 20);

gladiator0.addMove(move); // true

gladiator0.attack("Hadoken", gladiator1);

gladiator1.getHp(); // 806
3.5 rest() 메서드를 구현한다
이 메서드는 아무 인자도 받지 않습니다.
HP는 10만큼, 파워 수치는 알고 있는 스킬마다 1씩 회복합니다.
Gladiator gladiator0 = new Gladiator("Dragonborn Whiterun", 100, 250, 10);
Gladiator gladiator1 = new Gladiator("Ulfric Stormcloak", 1000, 300, 77);
Move move = new Move("Hadoken", 120, 20);

gladiator0.addMove(move);

gladiator0.attack("Hadoken", gladiator1);

gladiator1.getHp(); // 806

gladiator1.rest();

gladiator1.getHp(); // 816
     */



}


