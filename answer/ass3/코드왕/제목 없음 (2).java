// ICollisionEvent.java
package academy.pocu.comp2500.assignment3;

public interface ICollisionEvent {
    boolean isTriggered();
    void setTriggerOn();
    void setTriggerOff();
    void checkTrigger();
    void registeredAsCollisionEventListener();
    void unregisteredInCollisionEventListener();
    AttackIntent attack();
}

// CollisionEventUnit.java
package academy.pocu.comp2500.assignment3;

import java.util.ArrayList;

public abstract class CollisionEventUnit extends Unit implements ICollisionEvent {
    private boolean isTriggered;

    protected CollisionEventUnit(Symbol symbol, UnitType type, int sight, int aoe, int maxHp, int ap, ArrayList<UnitType> targets, IntVector2D position, boolean invisible, boolean detectable) {
        super(symbol, type, sight, aoe, maxHp, ap, targets, position, invisible, detectable);
    }

    public void onSpawn() {
        SimulationManager.getInstance().registerCollisionEventListener(this);
    }

    @Override
    public void onFuneral() {
        super.onFuneral();
        SimulationManager.getInstance().unregisterCollisionEventListener(this);
    }

    public boolean isTriggered() {
        return this.isTriggered;
    }

    public void setTriggerOn() {
        this.isTriggered = true;
    }

    public void setTriggerOff() {
        this.isTriggered = false;
    }

    public abstract void checkTrigger();

    @Override
    public void registeredAsCollisionEventListener() {
        SimulationManager.getInstance().registerCollisionEventListener(this);
    }

    @Override
    public void unregisteredInCollisionEventListener() {
        SimulationManager.getInstance().unregisterCollisionEventListener(this);
    }

    @Override
    public void resetActionStatus() {
        super.resetActionStatus();
        this.setTriggerOff();
    }
}

// Mine.java
package academy.pocu.comp2500.assignment3;

import java.util.ArrayList;
import java.util.Arrays;

public class Mine extends CollisionEventUnit {
    private int stepOnCount;
    private final int MAX_STEP_ON_COUNT;
    private ArrayList<Unit> unitsOnMine = new ArrayList<>();

    public Mine(IntVector2D position, int maxStepOnCount) {
        super(Symbol.N, UnitType.GROUND, 0, 0, 1, 10, new ArrayList<>(Arrays.asList(UnitType.GROUND)), position, true, false);
        this.MAX_STEP_ON_COUNT = maxStepOnCount;
        setTargetTile(getPosition());
    }

    protected Mine(Symbol symbol, UnitType type, int sight, int aoe, int maxHp, int ap, ArrayList<UnitType> targets, IntVector2D position, boolean invisible, int maxStepOnCount, boolean detectable) {
        super(symbol, type, sight, aoe, maxHp, ap, targets, position, invisible, detectable);
        this.MAX_STEP_ON_COUNT = maxStepOnCount;
        setTargetTile(getPosition());
    }

    @Override
    public AttackIntent attack() {
        ArrayList<BattleInfo> battleInfos = new ArrayList<>();

        ArrayList<Unit> enemiesInTargetTile = findAllEnemiesInTargetTile(getTargetTile());
        for (Unit e : enemiesInTargetTile) {
            battleInfos.add(super.normalAttack(e));
        }

        ArrayList<Unit> enemiesInAoe = super.findAllUnitsInAoe(getTargetTile());
        for (Unit e : enemiesInAoe) {
            battleInfos.add(aoeAttack(e, getTargetTile()));
        }
        System.out.printf("%s attacks %s%s", this, getTargetTile(), System.lineSeparator());
        selfDestruct();

        return new AttackIntent(this, battleInfos);
    }

    public void selfDestruct() {
        setHp(0);
        System.out.printf("%s self-destructs! ", this.toString());
    }

    @Override
    public void checkTrigger() {
        if (checkIfStepOnCountExceeds()) {
            System.out.printf("stepOnCount exceeds max. %s's trigger is pulled. ", this.toString());
            setTriggerOn();
        }
    }

    public boolean checkIfStepOnCountExceeds() {
        ArrayList<Unit> currentEnemiesOnMine = findAllEnemiesInTargetTile(getTargetTile());
        int numberOfNewStepper = currentEnemiesOnMine.size();
        for (Unit e : currentEnemiesOnMine) {
            if (unitsOnMine.contains(e)) {
                numberOfNewStepper--;
            }
        }
        increaseStepOnCountBy(numberOfNewStepper);

        return stepOnCount >= MAX_STEP_ON_COUNT;
    }

    public int getStepOnCount() {
        return this.stepOnCount;
    }

    public void increaseStepOnCountBy(int value) {
        this.stepOnCount += value;
        if (value > 0) {
            System.out.printf("%s is stepped %d times. total: %d%s", this.toString(), value, stepOnCount, System.lineSeparator());
        }
    }


    public int getMaxStepOnCount() {
        return this.MAX_STEP_ON_COUNT;
    }

    @Override
    public void resetActionStatus() {

    }
}

// SmartMine.java
package academy.pocu.comp2500.assignment3;

import java.util.ArrayList;
import java.util.Arrays;

public class SmartMine extends Mine implements IThinkable {
    private final int MAX_SENSE_COUNT;
    private boolean isFirstFrame = true;

    public SmartMine(IntVector2D position, int maxStepOnCount, int maxSenseCount) {
        super(Symbol.A, UnitType.GROUND, 1, 1, 1, 15, new ArrayList<>(Arrays.asList(UnitType.GROUND)), position, true, maxStepOnCount, false);
        this.MAX_SENSE_COUNT = maxSenseCount;
    }

    public boolean isFirstFrame() {
        return this.isFirstFrame;
    }

    @Override
    public void think() {
//        ArrayList<Unit> firstEnemies = new ArrayList<>(); // 첫 스폰에만 감지 못하게
//        if (isFirstFrame()) {
//            for (Unit e : findAllEnemiesInSight()) {
//                firstEnemies.add(e);
//            }
//        }

        ArrayList<Unit> enemiesInSight = findAllEnemiesInSight();
//        for (Unit e : enemiesInSight) {
//            if (!enemiesInSight.contains(e)) {
//                firstEnemies.remove(e);
//            }
//        }
//        enemiesInSight.removeAll(firstEnemies);
        if (enemiesInSight.size() < MAX_SENSE_COUNT) {
            setAction(Action.NOTHING);
        } else {
            setAction(Action.NOTHING);
            System.out.printf("%s senses more than %d units. SmartMine's trigger is pulled%s", this.toString(), MAX_SENSE_COUNT, System.lineSeparator());
            setTriggerOn();
        }
    }

    @Override
    public void onSpawn() {
        super.onSpawn();
        registeredAsThinkable();
    }

    @Override
    public void registeredAsThinkable() {
        SimulationManager.getInstance().registerThinkable(this);
    }

    public void unregisteredInThinkable() {
        SimulationManager.getInstance().unregisterThinkable(this);
    }

    @Override
    public void checkTrigger() {
        super.checkTrigger();
    }

    public int getMaxSenseCount() {
        return this.MAX_SENSE_COUNT;
    }

    @Override
    public void resetActionStatus() {
        if (isFirstFrame()) {
            isFirstFrame = false;
        }
    }
}

// IntVector2D.java
package academy.pocu.comp2500.assignment3;

import java.util.Vector;

public class IntVector2D {
    private int x;
    private int y;

    public IntVector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVector(IntVector2D other) {
        this.x = other.x;
        this.y = other.y;
    }



    public boolean equals(IntVector2D other) {
        if (this == other) {
            return true;
        }

        return this.x == other.x && this.y == other.y;
    }


    @Override
    public int hashCode() {
        return Integer.hashCode(x) * Integer.hashCode(y);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    public boolean isBetweenVectors(IntVector2D v1, IntVector2D v2, IntVectorCompareMode mode) {
        if (this.equals(v1) || this.equals(v2)) {
            return false;
        }

        int smaller = 0;
        int bigger = 0;

        switch (mode) {
            case HORIZONTAL:
                if (v1.y != v2.y || this.y != v1.y) {
                    return false;
                }

                smaller = Integer.min(v1.x, v2.x);
                bigger = Integer.max(v1.x, v2.x);

                return this.x > smaller && this.x < bigger;
            case VERTICAL:
                if (v1.x != v2.x || this.x != v1.x) {
                    return false;
                }

                smaller = Integer.min(v1.y, v2.y);
                bigger = Integer.max(v1.y, v2.y);

                return this.y > smaller && this.y < bigger;
            default:
                assert false : "Unexpected IntVectorCompareMode detected";
                return false;
        }
    }

    public int getManhattanDistanceTo(IntVector2D destination) {
        return Math.abs(destination.x - this.x) + Math.abs(destination.y - this.y);
    }

    public float getEuclideanDistanceTo(IntVector2D destination) {
        return (float) Math.sqrt(Math.pow(destination.x - this.x, 2) + Math.pow(destination.y - this.y, 2));
    }

    public float getClockwiseRotateDegree(IntVector2D origin) {
        IntVector2D north = new IntVector2D(0, -1);
        IntVector2D converted = new IntVector2D(this.x - origin.x, this.y - origin.y);
        float cosineTheta = (float) (north.x * converted.x + north.y * converted.y) / (north.getMagnitude() * converted.getMagnitude());
        float thetaInDegree = this.x < origin.x ? -(float) Math.toDegrees(Math.acos(cosineTheta)) : (float) Math.toDegrees(Math.acos(cosineTheta));

        return this.x < origin.x ? thetaInDegree + 360 : thetaInDegree;
    }

    public float getMagnitude() {
        return (float) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }
}