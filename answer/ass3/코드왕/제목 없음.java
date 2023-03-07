// Desroyer.java
package academy.pocu.comp2500.assignment3;

import java.util.ArrayList;
import java.util.Arrays;

public class Destroyer extends Unit {

    public Destroyer(IntVector2D position) {
        super(Symbol.D, UnitType.GROUND, 1000, 0, 1000, 10000, new ArrayList<>(Arrays.asList(UnitType.GROUND, UnitType.AIR)), position, false, true);
        setAction(Action.ATTACK);
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public AttackIntent attack() {
        ArrayList<BattleInfo> battleInfos = new ArrayList<>();

        ArrayList<Unit> allEnemies = findAllEnemiesInSight();
        for (Unit e : allEnemies) {
            battleInfos.add(normalAttack(e));
        }

        System.out.printf("%s attacks all enemies%s", this, System.lineSeparator());

        return new AttackIntent(this, battleInfos);
    }

    @Override
    public void onAttacked(int damage) {
        super.onAttacked(1);
    }

    @Override
    public void resetActionStatus() {

    }
}

// IThinkable.java
package academy.pocu.comp2500.assignment3;

public interface IThinkable {

    void think();
    void registeredAsThinkable();
    void unregisteredInThinkable();
}

//ThinkableUnit.java
package academy.pocu.comp2500.assignment3;

import java.util.ArrayList;

public abstract class ThinkableUnit extends Unit implements IThinkable {
    private AttackRangeType attackRangeType;
    private int attackRangeLength;

    protected ThinkableUnit(Symbol symbol, UnitType type, int sight, int aoe, int maxHp, int ap, ArrayList<UnitType> targets, IntVector2D position, boolean invisible, AttackRangeType attackRangeType, int attackRangeLength, boolean detectable) {
        super(symbol, type, sight, aoe, maxHp, ap, targets, position, invisible, detectable);
        this.attackRangeType = attackRangeType;
        this.attackRangeLength = attackRangeLength;
    }

    public AttackRangeType getAttackRangeType() {
        return this.attackRangeType;
    }

    public abstract void think();

    @Override
    public void registeredAsThinkable() {
        SimulationManager.getInstance().registerThinkable(this);
    }

    @Override
    public void unregisteredInThinkable() {
        SimulationManager.getInstance().unregisterThinkable(this);
    }

    public ArrayList<Unit> findAllEnemiesInAttackRange() { // must change to protected
        ArrayList<Unit> enemiesInAttackRange = new ArrayList<>();

        ArrayList<Unit> enemiesInSight = this.findAllEnemiesInSight();
        for (Unit u : enemiesInSight) {
            int thisX = this.getPosition().getX();
            int thisY = this.getPosition().getY();
            IntVector2D enemyPosition = u.getPosition();
            int otherX = enemyPosition.getX();
            int otherY = enemyPosition.getY();
            int northEnd = thisY - attackRangeLength;
            int southEnd = thisY + attackRangeLength;
            int westEnd = thisX - attackRangeLength;
            int eastEnd = thisX + attackRangeLength;
            IntVector2D westNorthVertex = new IntVector2D(westEnd, northEnd);
            IntVector2D eastNorthVertex = new IntVector2D(eastEnd, northEnd);
            IntVector2D eastSouthVertex = new IntVector2D(eastEnd, southEnd);
            IntVector2D westSouthVertex = new IntVector2D(westEnd, southEnd);

            boolean isInAttackRange = false;
            switch (attackRangeType) {
                case FULL_CROSS:
                    boolean isInVerticalLine = thisX == otherX && (otherY >= northEnd && otherY <= southEnd);
                    boolean isInHorizontalLine = thisY == otherY && (otherX >= westEnd && otherX <= eastEnd);
                    isInAttackRange = isInVerticalLine || isInHorizontalLine;
                    break;
                case FULL_RECTANGLE:
                    isInAttackRange = (otherX >= westEnd && otherX <= eastEnd) && (otherY >= northEnd && otherY <= southEnd);
                    break;
                case EMPTY_RECTANGLE_WITHOUT_VERTEX:
                    boolean isBetweenVertex1 = enemyPosition.isBetweenVectors(westNorthVertex, eastNorthVertex, IntVectorCompareMode.HORIZONTAL);
                    boolean isBetweenVertex2 = enemyPosition.isBetweenVectors(eastNorthVertex, eastSouthVertex, IntVectorCompareMode.VERTICAL);
                    boolean isBetweenVertex3 = enemyPosition.isBetweenVectors(eastSouthVertex, westSouthVertex, IntVectorCompareMode.HORIZONTAL);
                    boolean isBetweenVertex4 = enemyPosition.isBetweenVectors(westSouthVertex, westNorthVertex, IntVectorCompareMode.VERTICAL);
                    isInAttackRange = isBetweenVertex1 || isBetweenVertex2 || isBetweenVertex3 || isBetweenVertex4;
                    break;
                default:
                    assert (false) : "An unexpected AttackRangeType detected in findAllEnemiesInAttackRange()";
                    break;
            }


            if (isInAttackRange) {
                enemiesInAttackRange.add(u);
            }
        }

        return enemiesInAttackRange;
    }

    public void onSpawn() {
        registeredAsThinkable();
    }

    @Override
    public void onFuneral() {
        super.onFuneral();
        SimulationManager.getInstance().unregisterThinkable(this);
    }

    @Override
    public void resetActionStatus() {
        super.resetActionStatus();
        setTargetTile(null);
    }
}

// Turret.java
package academy.pocu.comp2500.assignment3;

import java.util.ArrayList;
import java.util.Arrays;

public class Turret extends ThinkableUnit {

    public Turret(IntVector2D position) {
        super(Symbol.U, UnitType.GROUND, 2, 0, 99, 7, new ArrayList<>(Arrays.asList(UnitType.AIR)), position, false, AttackRangeType.FULL_RECTANGLE, 1, false);
    }

    @Override
    public void think() {
        ArrayList<Unit> enemiesInSight = findAllEnemiesInSight();
        if (enemiesInSight.size() == 0) {
            setAction(Action.NOTHING);

            return;
        }

        ArrayList<Unit> enemiesInAttackRange = findAllEnemiesInAttackRange();
        if (enemiesInAttackRange.size() != 0) { // 공격 범위에 있는 경우
            ArrayList<Unit> lowestHpUnits = findTheLowestHpEnemies(enemiesInAttackRange);
            if (lowestHpUnits.size() == 1 || checkEnemiesInSameTile(lowestHpUnits)) {
                super.setTargetTile(lowestHpUnits.get(0).getPosition());
                super.setAction(Action.ATTACK);

                return;
            } else if (lowestHpUnits.size() > 1) {
                for (Unit u : lowestHpUnits) {
                    if (u.getPosition().equals(this.getPosition())) {
                        super.setTargetTile(u.getPosition());
                        super.setAction(Action.ATTACK);

                        return;
                    }
                }
                super.setTargetTile(findFirstUnitInClockWiseOrNull(lowestHpUnits).getPosition());
                super.setAction(Action.ATTACK);

                return;
            }
        }
    }
}

// IMovable.java
package academy.pocu.comp2500.assignment3;

public interface IMovable {
    void move();
    void registeredAsMovable();
    void unregisteredInMovable();
    Action getAction();
}

// MovableUnit.java
package academy.pocu.comp2500.assignment3;

import java.util.ArrayList;

public abstract class MovableUnit extends ThinkableUnit implements IMovable {

    protected MovableUnit(Symbol symbol, UnitType type, int sight, int aoe, int maxHp, int ap, ArrayList<UnitType> targets, IntVector2D position, boolean invisible, AttackRangeType attackRangeType, int attackRangeLength, boolean detectable) {
        super(symbol, type, sight, aoe, maxHp, ap, targets, position, invisible, attackRangeType, attackRangeLength, detectable);
    }

    public void onSpawn() {
        super.onSpawn();
        SimulationManager.getInstance().registerMovable(this);
    }

    @Override
    public void onFuneral() {
        super.onFuneral();
        SimulationManager.getInstance().unregisterMovable(this);
    }

    @Override
    public void move() {
        System.out.printf("%s -> ", super.toString());
        super.getPosition().setVector(super.getTargetTile());
        System.out.printf("%s%s", getPosition().toString(), System.lineSeparator());
    }

    @Override
    public void registeredAsMovable() {
        SimulationManager.getInstance().registerMovable(this);
    }

    @Override
    public void unregisteredInMovable() {
        SimulationManager.getInstance().unregisterMovable(this);
    }

    public IntVector2D getNextPositionOnTheWay(IntVector2D otherPosition) { // change it to protected
        if (super.getPosition().equals(otherPosition)) {
            System.out.println("Target unit or tile is on same tile");

            return super.getPosition();
        }

        int otherX = otherPosition.getX();
        int otherY = otherPosition.getY();
        int thisX = super.getPosition().getX();
        int thisY = super.getPosition().getY();
        if (thisY < otherY) {

            return new IntVector2D(thisX, thisY + 1);

        } else if (thisY > otherY) {

            return new IntVector2D(thisX, thisY - 1);

        } else {
            if (thisX < otherX) {

                return new IntVector2D(thisX + 1, thisY);

            } else {

                return new IntVector2D(thisX - 1, thisY);

            }
        }
    }
}