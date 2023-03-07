// SimulationManager.java
package academy.pocu.comp2500.assignment3;

import java.util.ArrayList;

public final class SimulationManager {
    private static SimulationManager instance = null;
    private ArrayList<Unit> allParticipantsList = new ArrayList<>();
    private ArrayList<Unit> unitsInField = new ArrayList<>();
    private ArrayList<IThinkable> thinkableUnits = new ArrayList<>();
    private ArrayList<IMovable> movableUnits = new ArrayList<>();
    private ArrayList<ICollisionEvent> collisionEventUnits = new ArrayList<>();
    private ArrayList<AttackIntent> attackIntents = new ArrayList<>();

    private SimulationManager() {

    }

    public static SimulationManager getInstance() {
        if (instance == null) {
            instance = new SimulationManager();
        }

        return instance;
    }

    public ArrayList<Unit> getUnitsInField() {
        return this.unitsInField;
    }
    
    public ArrayList<Unit> getUnits() {
//        return this.allParticipantsList;
        return this.unitsInField;
    }

    public ArrayList<IThinkable> getThinkableUnits() {
        return this.thinkableUnits;
    }

    public ArrayList<IMovable> getMovableUnits() {
        return this.movableUnits;
    }

    public ArrayList<ICollisionEvent> getCollisionEventUnits() {
        return this.collisionEventUnits;
    }

    public ArrayList<AttackIntent> getAttackIntents() {
        return this.attackIntents;
    }


    public void spawn(Unit unit) {
        unitsInField.add(unit);
        allParticipantsList.add(unit);
        unit.onSpawn();
    }

    public void registerThinkable(IThinkable thinkable) {
        thinkableUnits.add(thinkable);
    }

    public void registerMovable(IMovable movable) {
        movableUnits.add(movable);
    }

    public void registerCollisionEventListener(ICollisionEvent listener) {
        collisionEventUnits.add(listener);
    }

    public void unregisterThinkable(IThinkable thinkable) {
        thinkableUnits.remove(thinkable);
    }

    public void unregisterMovable(IMovable movable) {
        movableUnits.remove(movable);
    }

    public void unregisterCollisionEventListener(ICollisionEvent listener) {
        collisionEventUnits.remove(listener);
    }

    public void update() {
        goToThinkingPhase();
        goToMovePhase();
        goToCollisionEventPhase();
        goToBattlePhase();
        goToDamageCalculationPhase();
        goToFuneralPhase();
        goToResetPhase();
    }

    private void goToThinkingPhase() {
        for (IThinkable thinkableUnit : this.thinkableUnits) {
            thinkableUnit.think();
        }
    }

    private void goToMovePhase() {
        System.out.println("[Move Phase]");
        for (IMovable m : movableUnits) {
            if (m.getAction() != null && m.getAction().equals(Action.MOVE)) {
//                System.out.printf("<%d> ", visualizer.getUnitId(m)); // debug
                m.move();
            }
        }
        System.out.println();
    }

    private void goToCollisionEventPhase() {
        System.out.println("[CollisionEvent Phase]");
        for (ICollisionEvent c : collisionEventUnits) {
            c.checkTrigger();
            if (c.isTriggered()) {
                this.attackIntents.add(c.attack());
            }
        }
        System.out.println();
        System.out.println();
    }

    private void goToBattlePhase() {
        System.out.println("[Battle Phase]");
        for (Unit u : unitsInField) {
            if (u.getAction() == Action.ATTACK) {
//                System.out.printf("<%X> ", visualizer.getUnitId(u)); // debug
                this.attackIntents.add(u.attack());
            }
        }
        System.out.println();
    }

    private void goToDamageCalculationPhase() {
        System.out.println("[Damage Calculation Phase]");
        for (AttackIntent ai : attackIntents) {
            applyDamage(ai);
        }
        System.out.println();
    }

    private void goToFuneralPhase() {
        System.out.println("[Funeral Phase]");
        ArrayList<Unit> toBeRemoved = new ArrayList<>();
        for (Unit u : unitsInField) {
            if (u.getHp() <= 0) {
                toBeRemoved.add(u);
            }
        }

        for (Unit deadUnit : toBeRemoved) {
            deadUnit.onFuneral();
        }
        System.out.printf("R.I.P : %s%s", toBeRemoved, System.lineSeparator());
        System.out.println();
    }

    private void applyDamage(AttackIntent attackIntent) {
        for (BattleInfo bi : attackIntent.getBattleInfos()) {
            Unit attacker = bi.getAttacker();
            int damage = bi.getDamage();
//            System.out.printf("<%X> ", visualizer.getUnitId(attacker)); // debug
            System.out.printf(bi.toString());
            Unit defender = bi.getDefender();
            defender.onAttacked(damage);
        }
    }

    private void goToResetPhase() {
        for (Unit u : unitsInField) {
            u.resetActionStatus();
        }

        attackIntents.clear();
    }

//    public void installVisualizer(SimulationVisualizer2 visualizer) { // debug
//        this.visualizer = visualizer;
//    }
}

// IntVectorCompareMode.java
package academy.pocu.comp2500.assignment3;

public enum IntVectorCompareMode {
    HORIZONTAL,
    VERTICAL
}

// Action.java
package academy.pocu.comp2500.assignment3;

public enum Action {
    MOVE,
    ATTACK,
    NOTHING
}

// AttackIntent.java
package academy.pocu.comp2500.assignment3;

import java.util.ArrayList;

public class 
// AttackIntent.java {
    private Unit attacker;
    private ArrayList<BattleInfo> battleInfos = new ArrayList<>();

    public AttackIntent(Unit attacker, ArrayList<BattleInfo> battleInfos) {
        this.attacker = attacker;
        this.battleInfos = battleInfos;
    }

    public Unit getAttacker() {
        return this.attacker;
    }

    public ArrayList<BattleInfo> getBattleInfos() {
        return this.battleInfos;
    }
}

// BattleInfo.java
package academy.pocu.comp2500.assignment3;

public class BattleInfo {
    private final Unit ATTACKER;
    private final Unit DEFENDER;
    private final int DAMAGE;
    private final DamageType damageType;

    public BattleInfo(Unit attacker, Unit defender, int damage, DamageType damageType) {
        this.ATTACKER = attacker;
        this.DEFENDER = defender;
        this.DAMAGE = damage;
        this.damageType = damageType;
    }

    public Unit getAttacker() {
        return this.ATTACKER;
    }

    public Unit getDefender() {
        return this.DEFENDER;
    }

    public int getDamage() {
        return this.DAMAGE;
    }

    public DamageType getDamageType() {
        return this.damageType;
    }

    @Override
    public String toString() {
        return String.format("%s attacks %s with %s %d", getAttacker(), getDefender(), damageType, DAMAGE);
    }
}

// AttackRangeType.java
package academy.pocu.comp2500.assignment3;

public enum AttackRangeType {
    FULL_CROSS,
    FULL_RECTANGLE,
    EMPTY_RECTANGLE_WITHOUT_VERTEX,
}

// DamageType.java
package academy.pocu.comp2500.assignment3;

public enum DamageType {
    NORMAL,
    AOE
}

// Direction.java
package academy.pocu.comp2500.assignment3;

public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST
}

// Symbol.java
package academy.pocu.comp2500.assignment3;

public enum Symbol {
    M,
    T,
    W,
    U,
    N,
    A,
    D
}

// UnitType.java
package academy.pocu.comp2500.assignment3;

public enum UnitType {
    GROUND,
    AIR
}