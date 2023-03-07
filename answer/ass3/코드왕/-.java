// Unit.java
package academy.pocu.comp2500.assignment3;

import java.util.ArrayList;

public abstract class Unit {
    private IntVector2D position;
    private final Symbol SYMBOL;
    private final UnitType TYPE;
    private final int SIGHT;
    private final int AOE;
    private final int AP;
    private final int MAX_HP;
    private int hp;
    private final ArrayList<UnitType> TARGETS;
    private final boolean INVISIBLE;
    private final boolean DETECTABLE;
    private Action action;
    private IntVector2D targetTile;



    protected Unit(Symbol symbol, UnitType type, int sight, int aoe, int maxHp, int ap, ArrayList<UnitType> targets, IntVector2D position, boolean invisible, boolean detectable) {
        this.SYMBOL = symbol;
        this.TYPE = type;
        this.SIGHT = sight;
        this.AOE = aoe;
        this.MAX_HP = maxHp;
        this.hp = MAX_HP;
        this.AP = ap;
        this.TARGETS = targets;
        this.position = position;
        this.INVISIBLE = invisible;
        this.DETECTABLE = detectable;
    }

    public Action getAction() {
        return this.action;
    }

    protected void setAction(Action action) {
        this.action = action;
    }

    public boolean isDetectable() {
        return this.DETECTABLE;
    }

    public IntVector2D getTargetTile() {
        String test1 = this.toString();
        IntVector2D test2 = targetTile;
        return this.targetTile;
    }

    public void setTargetTile(IntVector2D targetTile) {
        if (targetTile == null) {

        } else {
//            System.out.printf("%s wants to set %s as targetTile%s", this.toString(), targetTile.toString(), System.lineSeparator()); // debug
            int x = targetTile.getX();
            int y = targetTile.getY();
            this.targetTile = new IntVector2D(x, y);

//            this.targetTile = targetTile; // Don't do this
        }

    }

    public IntVector2D getPosition() {
        return this.position;
    }

    public int getMaxHp() {
        return this.MAX_HP;
    }

    public int getHp() {
        return this.hp;
    }

    protected void setHp(int value) {
        hp = value;
    }

    public int getAp() {
        return this.AP;
    }

    public int getAoe() {
        return this.AOE;
    }

    public int getSight() {
        return this.SIGHT;
    }

    public char getSymbol() {
        return SYMBOL.toString().charAt(0);
    }

    public UnitType getType() {
        return TYPE;
    }

    public ArrayList<UnitType> getTargets() {
        return this.TARGETS;
    }

    public boolean isInvisible() {
        return this.INVISIBLE;
    }

    public AttackIntent attack() {
        ArrayList<BattleInfo> battleInfos = new ArrayList<>();
        IntVector2D targetTile = getTargetTile();

        ArrayList<Unit> enemiesInTargetTile = findAllEnemiesInTargetTile(targetTile);
        for (Unit e : enemiesInTargetTile) {
            battleInfos.add(normalAttack(e));
        }

        ArrayList<Unit> enemiesInAoe = findAllUnitsInAoe(targetTile);
        for (Unit e : enemiesInAoe) {
            battleInfos.add(aoeAttack(e, targetTile));
        }
        System.out.printf("%s attacks %s%s", this, targetTile, System.lineSeparator());

        return new AttackIntent(this, battleInfos);
    }

    public BattleInfo normalAttack(Unit enemy) {
        String thisUnit = toString(); // debug
        return new BattleInfo(this, enemy, this.AP, DamageType.NORMAL);
    }

    public BattleInfo aoeAttack(Unit enemy, IntVector2D center) {
        IntVector2D otherPosition = enemy.getPosition();
        int otherX = otherPosition.getX();
        int otherY = otherPosition.getY();
        int centerX = center.getX();
        int centerY = center.getY();
        int aoeDistance = Math.max(Math.abs(otherX - centerX), Math.abs(otherY - centerY));
        int aoeDamage = (int) ((this.AP) * (1 - ((double) aoeDistance) / (this.AOE + 1)));

        return new BattleInfo(this, enemy, aoeDamage, DamageType.AOE);
    }

    public void onAttacked(int damage) {
        this.hp -= damage;
        this.hp = Math.max(0, this.hp);
        System.out.printf(", result-> hp: %d%s", this.hp, System.lineSeparator());
    }

    public abstract void onSpawn();

    public void onFuneral() {
        SimulationManager simulationManager = SimulationManager.getInstance();
        ArrayList<Unit> unitsInField = simulationManager.getUnitsInField();
        unitsInField.remove(this);
    }

    public ArrayList<Unit> findAllEnemiesInSight() { // must change it to protected
        ArrayList<Unit> unitsInSight = new ArrayList<>();

        ArrayList<Unit> unitsInField = SimulationManager.getInstance().getUnitsInField();
        int myX = this.position.getX();
        int myY = this.position.getY();
        for (Unit unit : unitsInField) {
            IntVector2D otherPosition = unit.getPosition();
            int otherX = otherPosition.getX();
            int otherY = otherPosition.getY();
            if (unit != this && (!unit.isInvisible() || this.isDetectable()) && otherX >= myX - SIGHT && otherX <= myX + SIGHT && otherY >= myY - SIGHT && otherY <= myY + SIGHT && this.TARGETS.contains(unit.getType())) {
                unitsInSight.add(unit);
            }
        }

        return unitsInSight;
    }

    public ArrayList<Unit> findAllUnitsInAoe(IntVector2D center) {
        ArrayList<Unit> unitsInAoe = new ArrayList<>();

        ArrayList<Unit> unitsInField = SimulationManager.getInstance().getUnitsInField();
        int centerX = center.getX();
        int centerY = center.getY();
        for (Unit unit : unitsInField) {
            IntVector2D otherPosition = unit.getPosition();
            int otherX = otherPosition.getX();
            int otherY = otherPosition.getY();
            if (unit != this && !center.equals(otherPosition) && otherX >= centerX - AOE && otherX <= centerX + AOE && otherY >= centerY - AOE && otherY <= centerY + AOE && this.TARGETS.contains(unit.getType())) {
                unitsInAoe.add(unit);
            }
        }

        return unitsInAoe;
    }

    public ArrayList<Unit> findAllEnemiesInTargetTile(IntVector2D targetTile) {
        ArrayList<Unit> enemiesInTargetTile = new ArrayList<>();

        ArrayList<Unit> unitsInField = SimulationManager.getInstance().getUnitsInField();
        for (Unit unit : unitsInField) {
            if (unit != this && unit.getPosition().equals(targetTile) && this.TARGETS.contains(unit.getType())) {
                enemiesInTargetTile.add(unit);
            }
        }

        return enemiesInTargetTile;
    }



    public ArrayList<Unit> findTheLowestHpEnemies(ArrayList<Unit> units) { // change it to protected
        ArrayList<Unit> lowestHpEnemies = new ArrayList<>();
        int lowestHp = Integer.MAX_VALUE;
        for (Unit u : units) {
            if (u.getHp() < lowestHp) {
                lowestHp = u.getHp();
                lowestHpEnemies.clear();
                lowestHpEnemies.add(u);
            } else if (u.getHp() == lowestHp) {
                lowestHpEnemies.add(u);
            }
        }

        return lowestHpEnemies;
    }

    public Unit findFirstUnitInClockWiseOrNull(ArrayList<Unit> units) { // change it to protected
        if (units.size() == 0) {
            System.out.print("Unit tries to find first unit in clockwise but there is no enemy in attackRange"); // debug

            return null;
        }

        float smallestClockwiseDegree = 360.0f;
        Unit firstUnit = null;
        for (Unit u : units) {
            float clockWiseDegree = u.getPosition().getClockwiseRotateDegree(this.getPosition());
            if (clockWiseDegree < smallestClockwiseDegree) {
                smallestClockwiseDegree = clockWiseDegree;
                firstUnit = u;
            }
        }

        return firstUnit;
    }

    public ArrayList<Unit> findTheClosestEnemies(ArrayList<Unit> units) { // change it to protected
        ArrayList<Unit> closestEnemies = new ArrayList<>();
        int shortestDistance = Integer.MAX_VALUE;
        for (Unit u : units) {
            if (this.getPosition().getManhattanDistanceTo(u.getPosition()) < shortestDistance) {
                closestEnemies.clear();
                closestEnemies.add(u);
                shortestDistance = this.getPosition().getManhattanDistanceTo(u.getPosition());
            } else if (this.getPosition().getManhattanDistanceTo(u.getPosition()) == shortestDistance) {
                closestEnemies.add(u);
            }
        }
        return closestEnemies;
    }



    public boolean checkEnemiesInSameTile(ArrayList<Unit> enemies) { // should change it to protected
        if (enemies.size() < 2) {
            System.out.println("size of enemies are must bigger than 1");

            return false;
        }

        IntVector2D enemyPosition1 = enemies.get(0).getPosition();
        for (int i = 1; i < enemies.size(); i++) {
            Unit unit = enemies.get(i); // debug
            if (!enemyPosition1.equals(enemies.get(i).getPosition())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return getPosition().toString();
//        return getClass().getSimpleName() + getPosition().toString();
    }

    public void resetActionStatus() {
        this.action = null;
        targetTile = null;
    }

}