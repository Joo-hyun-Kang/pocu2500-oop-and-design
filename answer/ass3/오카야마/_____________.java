Unit.java ------------------------------------------------

public abstract class Unit {
    protected IntVector2D position;
    protected final char symbol;
    protected final UnitType unitType;
    protected final int vision;
    protected final int attackRange;
    protected final int areaOfEffect;
    protected final int attackPower;
    protected int hp;
    protected final int maxHp;
    protected final EnumSet<UnitType> attackableUnitType;
    protected ActionType actionType;
    protected boolean isVisible;
    protected boolean hasShield;
    protected boolean isAttacked;
    protected AttackIntent attackIntent;
    protected IntVector2D nextPosition;

    public Unit(IntVector2D position, char symbol, UnitType unitType, int vision, int attackRange, int areaOfEffect, int attackPower, int maxHp, EnumSet<UnitType> attackableUnitType, boolean isVisible, boolean hasShield) {
        this.position = position;
        this.symbol = symbol;
        this.unitType = unitType;
        this.vision = vision;
        this.attackRange = attackRange;
        this.areaOfEffect = areaOfEffect;
        this.attackPower = attackPower;
        this.hp = maxHp;
        this.maxHp = maxHp;
        this.attackableUnitType = attackableUnitType;
        this.actionType = ActionType.NONE;
        this.isVisible = isVisible;
        this.hasShield = hasShield;
        this.isAttacked = false;
        this.attackIntent = null;
        this.nextPosition = null;
    }

    public IntVector2D getPosition() {
        return position;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }

    public int getHp() {
        return this.hp;
    }

    public EnumSet<UnitType> getAttackableUnitType() {
        return this.attackableUnitType;
    }

    public boolean isAlive() {
        return (this.hp > 0);
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public boolean isAttacked() {
        return this.isAttacked;
    }

    public boolean hasShield() {
        return this.hasShield;
    }

    public void breakShield() {
        this.hasShield = false;
    }

    public ActionType getActionType() {
        return this.actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public abstract void onSpawn();

    public abstract void onDeath();

    public AttackIntent attack() {
        assert (this.actionType == ActionType.ATTACK) : "attack() actionType != ATTACK";

        return this.attackIntent;
    }

    public void onAttacked(int damage) {
        this.isAttacked = true;
        this.hp = Math.max(0, this.hp - damage);
    }

    public boolean isEnemyInAttackZone(ArrayList<Unit> enemies) {
        ArrayList<Unit> enemiesInAttackZone = getEnemiesInAttackZone(enemies);

        return enemiesInAttackZone.size() > 0 ? true : false;
    }

    public ArrayList<Unit> getEnemiesInAttackZone(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this));

        ArrayList<Unit> enemiesInAttackZone = new ArrayList<Unit>();
        for (Unit enemy : enemies) {
            if (!enemy.isVisible() || !this.attackableUnitType.contains(enemy.unitType)) {
                continue;
            }

            int xPos[] = {0, -1, 0, 0, 1};
            int yPos[] = {0, 0, 1, -1, 0};

            int dx = this.position.getX() - enemy.getPosition().getX();
            int dy = this.position.getY() - enemy.getPosition().getY();

            for (int i = 0; i < xPos.length; ++i) {
                if (xPos[i] == dx && yPos[i] == dy) {
                    enemiesInAttackZone.add(enemy);

                    break;
                }
            }
        }

        return enemiesInAttackZone;
    }

    public boolean isEnemyInSight(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "isEnemyInSight() contains this";

        ArrayList<Unit> enemiesInSight = getEnemiesInSight(enemies);

        return enemiesInSight.size() > 0 ? true : false;
    }

    protected ArrayList<Unit> getEnemiesInSight(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "getEnemiesInSight() contains this";

        ArrayList<Unit> enemiesInSight = new ArrayList<Unit>();
        for (Unit enemy : enemies) {
            int range = this.calculateRange(enemy.getPosition());
            if (range > this.vision || !enemy.isVisible()) {
                continue;
            }

            if (this.attackableUnitType.contains(enemy.unitType)) {
                enemiesInSight.add(enemy);
            }
        }

        return enemiesInSight;
    }

    public ArrayList<Unit> getWeakestEnemies(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "getWeakestEnemies() contains this";

        ArrayList<Unit> attackableUnits = this.filterUnattackableUnits(enemies);

        int weakestUnitHp = attackableUnits.get(0).getHp();

        for (int i = 1; i < attackableUnits.size(); ++i) {
            int hp = attackableUnits.get(i).getHp();
            if (hp < weakestUnitHp) {
                weakestUnitHp = hp;
            }
        }

        ArrayList<Unit> weakestUnits = new ArrayList<Unit>();

        for (Unit unit : attackableUnits) {
            if (unit.hp == weakestUnitHp) {
                weakestUnits.add(unit);
            }
        }

        return weakestUnits;
    }

    protected ArrayList<Unit> getClosestEnemiesByManhattanDistance(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "getClosestEnemiesByManhattanDistance() : contains this";

        ArrayList<Unit> attackableUnits = this.filterUnattackableUnits(enemies);

        Unit closestUnit = attackableUnits.get(0);
        int closestDistance = calculateManhattanDistance(closestUnit.position);

        for (int i = 1; i < attackableUnits.size(); ++i) {
            int distance = calculateManhattanDistance(attackableUnits.get(i).position);
            if (distance < closestDistance) {
                closestDistance = distance;
            }
        }

        ArrayList<Unit> closestEnemies = new ArrayList<Unit>();

        for (Unit unit : attackableUnits) {
            int distance = calculateManhattanDistance(unit.position);
            if (distance == closestDistance) {
                closestEnemies.add(unit);
            }
        }

        return closestEnemies;
    }

    public IntVector2D getEnemyPositionClockwise(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "getEnemyPositionClockwise() : contains this";

        ArrayList<Unit> attackableUnits = this.filterUnattackableUnits(enemies);
        assert (!enemies.isEmpty()) : "getEnemyPositionClockwise() : attackable units empty";

        int maxX = attackableUnits.get(0).position.getX();
        int maxY = attackableUnits.get(0).position.getY();

        if (this.position.equals(attackableUnits.get(0).position)) {
            return new IntVector2D(this.position.getX(), this.position.getY());
        }

        for (int i = 1; i < attackableUnits.size(); ++i) {
            if (this.position.equals(attackableUnits.get(i).position)) {
                return new IntVector2D(this.position.getX(), this.position.getY());
            }

            int x = this.position.getX();
            int y = this.position.getY();

            int enemyX = attackableUnits.get(i).position.getX();
            int enemyY = attackableUnits.get(i).position.getY();

            if ((maxX >= x && enemyX >= x && enemyY < maxY) || (maxX < x && enemyX < x && enemyY > maxY) || (maxX < x && enemyX >= x)) {
                maxX = enemyX;
                maxY = enemyY;
            }
        }

        return new IntVector2D(maxX, maxY);
    }

    protected HashMap<IntVector2D, Integer> createPositionToDamageMapWithAoE1(IntVector2D attackedPosition) {
        HashMap<IntVector2D, Integer> positionToDamageMap = new HashMap<IntVector2D, Integer>();

        int x = attackedPosition.getX();
        int y = attackedPosition.getY();

        IntVector2D center = new IntVector2D(x, y);
        positionToDamageMap.put(center, this.attackPower);

        int damageFromAoE = calculateAoEDamage(center, 1);

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {

                if (i == 0 && j == 0) {
                    continue;
                }

                IntVector2D pos = new IntVector2D(x + i, y + j);
                positionToDamageMap.put(pos, damageFromAoE);
            }
        }

        return positionToDamageMap;
    }

    public void move() {
        if (this.actionType == ActionType.MOVE) {
            this.position = this.nextPosition;
        }
    }

    protected void setNextPosition() {
        assert (this.actionType == ActionType.MOVE) : "setMoveTo() : actionType not MOVE";

        SimulationManager instance = SimulationManager.getInstance();
        ArrayList<Unit> enemies = instance.getUnits();

        enemies.remove(this);

        ArrayList<Unit> enemiesInSight = getEnemiesInSight(enemies);
        ArrayList<Unit> closestEnemies = getClosestEnemiesByManhattanDistance(enemiesInSight);

        if (closestEnemies.size() == 1) {
            this.nextPosition = getNextPositionToward(closestEnemies.get(0).position);

            return;
        }
        assert (closestEnemies.size() > 1) : "closestEnemiest.size() should be greater than 1";

        ArrayList<Unit> weakestEnemies = getWeakestEnemies(closestEnemies);
        if (weakestEnemies.size() == 1) {
            this.nextPosition = getNextPositionToward(weakestEnemies.get(0).position);

            return;
        }

        IntVector2D firstUnit = getEnemyPositionClockwise(weakestEnemies);
        this.nextPosition = getNextPositionToward(firstUnit);
    }

    protected IntVector2D getNextPositionToward(IntVector2D position) {
        IntVector2D next = new IntVector2D(this.position.getX(), this.position.getY());

        if (this.calculateRange(position) == 0) {
            return next;
        }

        if (Math.abs(position.getY() - this.position.getY()) > 0) {
            int y = position.getY() - this.position.getY();

            if (y > 0) {
                next.setY(this.position.getY() + 1);
            } else {
                next.setY(this.position.getY() - 1);
            }

            return next;
        }

        if (Math.abs(position.getX() - this.position.getX()) > 0) {
            int x = position.getX() - this.position.getX();

            if (x > 0) {
                next.setX(this.position.getX() + 1);
            } else {
                next.setX(this.position.getX() - 1);
            }

            return next;
        }

        return next;
    }

    protected void makeAttackIntent(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this));

        ArrayList<Unit> enemiesInAttackZone = getEnemiesInAttackZone(enemies);
        HashMap<IntVector2D, Integer> positionToDamageMap = new HashMap<IntVector2D, Integer>();

        if (enemiesInAttackZone.size() == 1) {
            positionToDamageMap.put(enemiesInAttackZone.get(0).position, this.attackPower);
            this.attackIntent = new AttackIntent(this, positionToDamageMap, this.attackableUnitType);

            return;
        }

        assert (enemiesInAttackZone.size() > 0);

        ArrayList<Unit> weakestEnemies = getWeakestEnemies(enemiesInAttackZone);
        if (weakestEnemies.size() == 1) {
            positionToDamageMap.put(weakestEnemies.get(0).position, this.attackPower);
            this.attackIntent = new AttackIntent(this, positionToDamageMap, this.attackableUnitType);

            return;
        }

        IntVector2D enemyPositionClockwise = getEnemyPositionClockwise(weakestEnemies);
        positionToDamageMap.put(enemyPositionClockwise, this.attackPower);
        this.attackIntent = new AttackIntent(this, positionToDamageMap, this.attackableUnitType);
    }


    protected int calculateAoEDamage(IntVector2D attackPosition, int offset) {
        return (int) Math.abs((this.attackPower * (1 - offset / (double) (this.areaOfEffect + 1))));
    }

    protected int calculateRange(IntVector2D position) {
        return Math.max(Math.abs(position.getX() - this.position.getX()), Math.abs(position.getY() - this.position.getY()));
    }

    protected int calculateManhattanDistance(IntVector2D position) {
        return Math.abs(position.getX() - this.position.getX()) + Math.abs(position.getY() - this.position.getY());
    }

    protected ArrayList<Unit> filterUnattackableUnits(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "filterUnattackableUnits() : contains this";

        ArrayList<Unit> attackableUnits = new ArrayList<Unit>();

        for (Unit enemy : enemies) {
            if (this.attackableUnitType.contains(enemy.unitType)) {
                attackableUnits.add(enemy);
            }
        }
        return attackableUnits;
    }
}

Marine.java ------------------------------------------------

public class Marine extends Unit implements IMovable, IThinkable {
    public Marine(IntVector2D position) {
        super(position, 'M', UnitType.GROUND, 2, 1, 0, 6, 35, EnumSet.of(UnitType.AIR, UnitType.GROUND, UnitType.INVISIBLE), true, false);
    }

    @Override
    public void onSpawn() {
        SimulationManager simulationManager = SimulationManager.getInstance();

        simulationManager.registerThinkable(this);
        simulationManager.registerMovable(this);
    }

    @Override
    public void onDeath() {
        SimulationManager simulationManager = SimulationManager.getInstance();

        simulationManager.unregisterThinkable(this);
        simulationManager.unregisterMovable(this);
    }

    @Override
    public void onThink() {
        SimulationManager instance = SimulationManager.getInstance();
        ArrayList<Unit> enemies = instance.getUnits();

        enemies.remove(this);
        this.isAttacked = false;

        if (isEnemyInAttackZone(enemies)) {
            this.setActionType(ActionType.ATTACK);
            this.makeAttackIntent(enemies);

            return;
        }

        if (isEnemyInSight(enemies)) {
            this.setActionType(ActionType.MOVE);
            this.setNextPosition();

            return;
        }

        this.setActionType(ActionType.NONE);
    }
}

Tank.java ------------------------------------------------

public class Tank extends Unit implements IMovable, IThinkable {
    private TankMode tankMode;
    private Direction movingDirection;
    private static final int WEST_BOUNDARY = 0;
    private static final int EAST_BOUNDARY = 15;
    private static final int NORTH_BOUNDARY = 7;
    private static final int SOUTH_BOUNDARY = 0;

    public Tank(IntVector2D position) {
        super(position, 'T', UnitType.GROUND, 3, 2, 1, 8, 85, EnumSet.of(UnitType.GROUND, UnitType.INVISIBLE), true, false);

        this.tankMode = TankMode.TANK;
        this.movingDirection = Direction.EAST;
    }

    @Override
    public void onSpawn() {
        SimulationManager simulationManager = SimulationManager.getInstance();

        simulationManager.registerThinkable(this);
        simulationManager.registerMovable(this);
    }

    @Override
    public void onDeath() {
        SimulationManager simulationManager = SimulationManager.getInstance();

        simulationManager.unregisterThinkable(this);
        simulationManager.unregisterMovable(this);
    }

    @Override
    public void onAttacked(int damage) {
        if (this.tankMode == TankMode.SIEGE) {
            damage *= 2;
        }

        this.isAttacked = true;
        this.hp = Math.max(0, this.hp - damage);
    }

    @Override
    public void move() {
        if (this.actionType == ActionType.MOVE) {
            this.position = this.nextPosition;
        }
    }

    @Override
    public void onThink() {
        SimulationManager instance = SimulationManager.getInstance();
        ArrayList<Unit> enemies = instance.getUnits();

        enemies.remove(this);
        this.isAttacked = false;

        if (isEnemyInSight(enemies)) {

            if (this.tankMode == TankMode.TANK) {
                this.tankMode = TankMode.SIEGE;
                this.actionType = ActionType.NONE;
            } else {
                if (isEnemyInAttackZone(enemies)) {
                    this.setActionType(ActionType.ATTACK);
                    this.makeAttackIntent(enemies);

                    return;
                }

                this.actionType = ActionType.NONE;
            }

        } else {

            if (this.tankMode == TankMode.SIEGE) {
                this.tankMode = TankMode.TANK;
                this.actionType = ActionType.NONE;

                return;
            }

            this.actionType = ActionType.MOVE;
            this.setNextPosition();
        }
    }

    @Override
    protected void setNextPosition() {
        assert (this.actionType == ActionType.MOVE) : "setMoveTo() : actionType not MOVE";

        int x = this.position.getX();
        int y = this.position.getY();

        switch (this.movingDirection) {
            case NORTH:
                if (y + 1 > NORTH_BOUNDARY) {
                    this.movingDirection = Direction.SOUTH;
                    this.setNextPosition();
                } else {
                    this.nextPosition = new IntVector2D(x, y + 1);
                }
                break;
            case EAST:
                if (x + 1 > EAST_BOUNDARY) {
                    this.movingDirection = Direction.WEST;
                    this.setNextPosition();
                } else {
                    this.nextPosition = new IntVector2D(x + 1, y);
                }
                break;
            case SOUTH:
                if (y - 1 < SOUTH_BOUNDARY) {
                    this.movingDirection = Direction.NORTH;
                    this.setNextPosition();
                } else {
                    this.nextPosition = new IntVector2D(x, y - 1);
                }
                break;
            case WEST:
                if (x - 1 < WEST_BOUNDARY) {
                    this.movingDirection = Direction.EAST;
                    this.setNextPosition();
                } else {
                    this.nextPosition = new IntVector2D(x - 1, y);
                }
                break;
            default:
                assert (false) : "invalid direction type" + this.movingDirection;
                break;
        }
    }

    @Override
    public boolean isEnemyInAttackZone(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "isEnemyInAttackZone() contains this";

        ArrayList<Unit> enemiesInAttackZone = getEnemiesInAttackZone(enemies);

        return enemiesInAttackZone.size() > 0 ? true : false;
    }

    @Override
    public ArrayList<Unit> getEnemiesInAttackZone(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this));

        ArrayList<Unit> enemiesInAttackZone = new ArrayList<Unit>();
        for (Unit enemy : enemies) {
            if (this.position.equals(enemy.position) || !enemy.isVisible() || !this.attackableUnitType.contains(enemy.unitType)) {
                continue;
            }

            int range = calculateRange(enemy.getPosition());
            int manhattanDistance = calculateManhattanDistance(enemy.getPosition());

            if (range == 2 && manhattanDistance != 4) {
                enemiesInAttackZone.add(enemy);
            }
        }

        return enemiesInAttackZone;
    }

    @Override
    public IntVector2D getEnemyPositionClockwise(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "getEnemyPositionClockwise() : units should not contains this";
        assert (!enemies.isEmpty()) : "getEnemyPositonClockwise() : more than 1 unit expected";

        int xPos[] = {0, 1, 2, 2, 2, 1, 0, -1, -2, -2, -2, -1};
        int yPos[] = {-2, -2, -1, 0, 1, 2, 2, 2, 1, 0, -1, -2};

        int dx = enemies.get(0).position.getX() - this.position.getX();
        int dy = enemies.get(0).position.getY() - this.position.getY();

        int firstUnitIndex = 0;
        for (int i = 0; i < xPos.length; ++i) {
            if (dx == xPos[i] && dy == yPos[i]) {
                firstUnitIndex = i;

                break;
            }
        }

        for (int i = 1; i < enemies.size(); ++i) {
            dx = enemies.get(i).position.getX() - this.position.getX();
            dy = enemies.get(i).position.getY() - this.position.getY();

            for (int j = 0; j < xPos.length; ++j) {
                if (dx == xPos[j] && dy == yPos[j]) {
                    if (j < firstUnitIndex) {
                        firstUnitIndex = j;
                    }

                    break;
                }
            }
        }

        int newX = xPos[firstUnitIndex] + this.position.getX();
        int newY = yPos[firstUnitIndex] + this.position.getY();

        return new IntVector2D(newX, newY);
    }

    @Override
    protected void makeAttackIntent(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this));

        ArrayList<Unit> enemiesInAttackZone = getEnemiesInAttackZone(enemies);

        if (enemiesInAttackZone.size() == 1) {
            this.attackIntent = new AttackIntent(this, createPositionToDamageMapWithAoE1(enemiesInAttackZone.get(0).position), this.attackableUnitType);

            return;
        }

        assert (enemiesInAttackZone.size() > 0);

        ArrayList<Unit> weakestEnemies = getWeakestEnemies(enemiesInAttackZone);
        if (weakestEnemies.size() == 1) {
            this.attackIntent = new AttackIntent(this, createPositionToDamageMapWithAoE1(weakestEnemies.get(0).position), this.attackableUnitType);

            return;
        }

        IntVector2D enemyPositionClockwise = getEnemyPositionClockwise(weakestEnemies);
        this.attackIntent = new AttackIntent(this, createPositionToDamageMapWithAoE1(enemyPositionClockwise), this.attackableUnitType);
    }
}

Wraith.java ------------------------------------------------

public class Wraith extends Unit implements IMovable, IThinkable {
    private IntVector2D origin;

    public Wraith(IntVector2D position) {
        super(position, 'W', UnitType.AIR, 4, 1, 0, 6, 80, EnumSet.of(UnitType.GROUND, UnitType.AIR, UnitType.INVISIBLE), true, true);

        this.origin = new IntVector2D(position.getX(), position.getY());
    }

    @Override
    public void onSpawn() {
        SimulationManager simulationManager = SimulationManager.getInstance();

        simulationManager.registerThinkable(this);
        simulationManager.registerMovable(this);
    }

    @Override
    public void onDeath() {
        SimulationManager simulationManager = SimulationManager.getInstance();

        simulationManager.unregisterThinkable(this);
        simulationManager.unregisterMovable(this);
    }

    @Override
    public void onAttacked(int damage) {
        if (hasShield) {
            this.isAttacked = true;
        } else {
            super.onAttacked(damage);
        }
    }

    @Override
    public void onThink() {
        SimulationManager instance = SimulationManager.getInstance();
        ArrayList<Unit> enemies = instance.getUnits();

        enemies.remove(this);
        this.isAttacked = false;

        if (isEnemyInAttackZone(enemies)) {
            this.setActionType(ActionType.ATTACK);
            this.makeAttackIntent(enemies);

            return;
        }

        if (isEnemyInSight(enemies)) {
            this.setActionType(ActionType.MOVE);
            this.setNextPosition();

            return;
        }

        this.nextPosition = getNextPositionToward(this.origin);
        this.setActionType(ActionType.MOVE);
    }

    @Override
    protected void setNextPosition() {
        SimulationManager instance = SimulationManager.getInstance();
        ArrayList<Unit> enemies = instance.getUnits();

        enemies.remove(this);

        ArrayList<Unit> enemiesInSight = getEnemiesInSight(enemies);
        ArrayList<Unit> flyingUnits = getFlyingUnits(enemiesInSight);

        if (flyingUnits.size() == 1) {
            this.nextPosition = getNextPositionToward(flyingUnits.get(0).position);

            return;
        }

        if (flyingUnits.size() > 1) {
            ArrayList<Unit> closestEnemies = getClosestEnemiesByManhattanDistance(flyingUnits);
            assert (!closestEnemies.isEmpty()) : "closestEnemies should not be empty";

            if (closestEnemies.size() == 1) {
                this.nextPosition = getNextPositionToward(closestEnemies.get(0).position);

                return;
            }
            assert (closestEnemies.size() > 1) : "closestEnemies.size() should be more than 1";

            ArrayList<Unit> weakestEnemies = getWeakestEnemies(closestEnemies);
            assert (!weakestEnemies.isEmpty()) : "weakestEnemies should not be empty";

            if (weakestEnemies.size() == 1) {
                this.nextPosition = getNextPositionToward(weakestEnemies.get(0).position);

                return;
            }
            assert (weakestEnemies.size() > 1) : "weakestEnemies.size() should be more than 1";

            this.nextPosition = getNextPositionToward(getEnemyPositionClockwise(weakestEnemies));
        }

        if (flyingUnits.isEmpty()) {
            super.setNextPosition();

            return;
        }

        if (!isEnemyInSight(flyingUnits)) {
            this.nextPosition = getNextPositionToward(this.origin);
        }
    }

    @Override
    public void move() {
        if (this.actionType == ActionType.MOVE) {
            this.position = this.nextPosition;
        }
    }

    @Override
    protected void makeAttackIntent(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "makeAttackIntent() : enemies contains this";

        ArrayList<Unit> enemiesInAttackZone = getEnemiesInAttackZone(enemies);
        ArrayList<Unit> flyingUnits = getFlyingUnits(enemiesInAttackZone);

        if (flyingUnits.isEmpty()) {
            super.makeAttackIntent(enemies);

            return;
        }

        if (flyingUnits.size() == 1) {
            HashMap<IntVector2D, Integer> positionToDamage = new HashMap<IntVector2D, Integer>();
            positionToDamage.put(flyingUnits.get(0).position, this.attackPower);

            this.attackIntent = new AttackIntent(this, positionToDamage, this.attackableUnitType);

            return;
        }

        if (flyingUnits.size() > 1) {
            ArrayList<Unit> weakestEnemies = getWeakestEnemies(flyingUnits);
            assert (!weakestEnemies.isEmpty()) : "weakestEnemies cannot be empty";

            if (weakestEnemies.size() == 1) {
                HashMap<IntVector2D, Integer> positionToDamage = new HashMap<IntVector2D, Integer>();
                positionToDamage.put(weakestEnemies.get(0).position, this.attackPower);

                this.attackIntent = new AttackIntent(this, positionToDamage, this.attackableUnitType);

                return;
            }
            assert (weakestEnemies.size() > 1) : "weakestEnemies.size() should be greater than 1";


            IntVector2D positionToAttack = getEnemyPositionClockwise(weakestEnemies);
            HashMap<IntVector2D, Integer> positionToDamage = new HashMap<IntVector2D, Integer>(); // put Map
            positionToDamage.put(positionToAttack, this.attackPower);

            this.attackIntent = new AttackIntent(this, positionToDamage, this.attackableUnitType);

            return;
        }

        super.makeAttackIntent(flyingUnits);
    }

    private ArrayList<Unit> getFlyingUnits(ArrayList<Unit> units) {
        assert (!units.contains(this));

        ArrayList<Unit> flyingUnits = new ArrayList<Unit>();
        for (Unit unit : units) {
            if (unit.unitType == UnitType.AIR) {
                flyingUnits.add(unit);
            }
        }

        return flyingUnits;
    }
}

Turret.java ------------------------------------------------
public class Turret extends Unit implements IThinkable {
    public Turret(IntVector2D position) {
        super(position, 'U', UnitType.GROUND, 2, 1, 0, 7, 99, EnumSet.of(UnitType.AIR), true, false);
    }

    @Override
    public void onSpawn() {
        SimulationManager simulationManager = SimulationManager.getInstance();

        simulationManager.registerThinkable(this);
    }

    @Override
    public void onDeath() {
        SimulationManager simulationManager = SimulationManager.getInstance();

        simulationManager.unregisterThinkable(this);
    }

    @Override
    public void onThink() {
        SimulationManager instance = SimulationManager.getInstance();
        ArrayList<Unit> enemies = instance.getUnits();

        enemies.remove(this);
        this.isAttacked = false;

        if (isEnemyInAttackZone(enemies)) {
            this.setActionType(ActionType.ATTACK);
            this.makeAttackIntent(enemies);
        } else {
            this.setActionType(ActionType.NONE);
        }
    }

    @Override
    public boolean isEnemyInAttackZone(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "isEnemyInAttackZone contains this";

        ArrayList<Unit> enemiesInAttackZone = getEnemiesInAttackZone(enemies);

        return enemiesInAttackZone.size() > 0 ? true : false;
    }

    @Override
    public ArrayList<Unit> getEnemiesInAttackZone(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "units should not contain this";

        ArrayList<Unit> enemiesInAttackZone = new ArrayList<Unit>();

        for (Unit enemy : enemies) {
            int range = calculateRange(enemy.getPosition());
            if (this.attackableUnitType.contains(enemy.getUnitType()) && (range == 0 || range == 1)) {
                enemiesInAttackZone.add(enemy);
            }
        }

        return enemiesInAttackZone;
    }

    @Override
    public IntVector2D getEnemyPositionClockwise(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "getEnemyPositionClockwise() : units should not contains this";
        assert (!enemies.isEmpty()) : "getEnemyPositonClockwise() : more than 1 unit expected";

        int xPos[] = {0, 0, 1, 1, 1, 0, -1, -1, -1};
        int yPos[] = {0, -1, -1, 0, 1, 1, 1, 0, -1};

        int dx = enemies.get(0).position.getX() - this.position.getX();
        int dy = enemies.get(0).position.getY() - this.position.getY();

        int firstUnitIndex = 0;
        for (int i = 0; i < xPos.length; ++i) {
            if (dx == xPos[i] && dy == yPos[i]) {
                firstUnitIndex = i;

                break;
            }
        }

        for (int i = 1; i < enemies.size(); ++i) {
            dx = enemies.get(i).position.getX() - this.position.getX();
            dy = enemies.get(i).position.getY() - this.position.getY();

            for (int j = 0; j < xPos.length; ++j) {
                if (dx == xPos[j] && dy == yPos[j]) {
                    if (j < firstUnitIndex) {
                        firstUnitIndex = j;
                    }

                    break;
                }
            }
        }

        int newX = xPos[firstUnitIndex] + this.position.getX();
        int newY = yPos[firstUnitIndex] + this.position.getY();

        return new IntVector2D(newX, newY);
    }
}

Mine.java ------------------------------------------------

public class Mine extends Unit implements ICollisionEventListener {
    private MineType mineType;
    protected int collisionCount;
    protected final int maxCollisionCount;

    public Mine(IntVector2D position, int maxCollisionCount) {
        super(position, 'N', UnitType.INVISIBLE, 0, 0, 0, 10, 1, EnumSet.of(UnitType.GROUND, UnitType.INVISIBLE), false, false);

        this.mineType = MineType.SIMPLE;
        this.collisionCount = 0;
        this.maxCollisionCount = maxCollisionCount;
    }

    public Mine(IntVector2D position, int maxCollisionCount, MineType mineType) {
        super(position, 'A', UnitType.INVISIBLE, 1, 0, 1, 15, 1, EnumSet.of(UnitType.GROUND, UnitType.INVISIBLE), false, false);

        this.mineType = mineType;
        this.collisionCount = 0;
        this.maxCollisionCount = maxCollisionCount;
    }

    @Override
    public void onSpawn() {
        SimulationManager simulationManager = SimulationManager.getInstance();

        simulationManager.registerCollisionEventListener(this);
    }

    @Override
    public void onDeath() {
        SimulationManager simulationManager = SimulationManager.getInstance();

        simulationManager.unregisterCollisionEventListener(this);
    }

    @Override
    public void onListen() {
        SimulationManager instance = SimulationManager.getInstance();
        ArrayList<Unit> enemies = instance.getUnits();

        enemies.remove(this);
        this.isAttacked = false;

        for (Unit enemy : enemies) {
            if (this.position.equals(enemy.position) && this.attackableUnitType.contains(enemy.unitType)) {
                this.collisionCount = Math.min(this.collisionCount + 1, this.maxCollisionCount);
            }
        }

        if (this.collisionCount == this.maxCollisionCount) {
            this.setActionType(ActionType.ATTACK);
            this.makeAttackIntent(enemies);
        } else {
            this.setActionType(ActionType.NONE);
        }
    }

    @Override
    protected void makeAttackIntent(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "makeAttackIntent() : enemies contains this";

        HashMap<IntVector2D, Integer> positionToDamageMap = new HashMap<IntVector2D, Integer>();
        positionToDamageMap.put(this.position, this.attackPower);
        this.attackIntent = new AttackIntent(this, positionToDamageMap, this.attackableUnitType);
    }

    @Override
    public AttackIntent attack() {
        assert (this.actionType == ActionType.ATTACK) : "attack() actionType != ATTACK";

        this.hp = 0;

        return this.attackIntent;
    }
}

SmartMine.java ------------------------------------------------

public class SmartMine extends Mine implements IThinkable {
    private int detectCount;
    private final int maxDetectCount;

    public SmartMine(IntVector2D position, int maxCollisionCount, int maxDetectCount) {
        super(position, maxCollisionCount, MineType.SMART);

        this.detectCount = 0;
        this.maxDetectCount = maxDetectCount;
    }

    @Override
    public void onSpawn() {
        SimulationManager simulationManager = SimulationManager.getInstance();

        simulationManager.registerCollisionEventListener(this);
        simulationManager.registerThinkable(this);
    }

    @Override
    public void onDeath() {
        SimulationManager simulationManager = SimulationManager.getInstance();

        simulationManager.unregisterCollisionEventListener(this);
        simulationManager.unregisterThinkable(this);
    }

    @Override
    public void onThink() {
        SimulationManager instance = SimulationManager.getInstance();
        ArrayList<Unit> enemies = instance.getUnits();

        this.detectCount = 0;
        this.isAttacked = false;
        enemies.remove(this);

        for (Unit enemy : enemies) {
            int range = calculateRange(enemy.position);
            if (enemy.unitType == UnitType.GROUND && (range == 0 || range == 1)) {
                this.detectCount = Math.min(this.detectCount + 1, this.maxDetectCount);
            }
        }

        if (this.detectCount == this.maxDetectCount) {
            this.setActionType(ActionType.ATTACK);
            this.makeAttackIntent(enemies);
        } else {
            this.setActionType(ActionType.NONE);
        }
    }

    @Override
    public void onListen() {
        if (this.actionType == ActionType.ATTACK) {
            return;
        }

        SimulationManager instance = SimulationManager.getInstance();
        ArrayList<Unit> enemies = instance.getUnits();

        enemies.remove(this);
        this.isAttacked = false;

        for (Unit enemy : enemies) {
            if (this.position.equals(enemy.position) && this.attackableUnitType.contains(enemy.unitType)) {
                this.collisionCount = Math.min(this.collisionCount + 1, this.maxCollisionCount);
            }
        }

        if (this.collisionCount == this.maxCollisionCount) {
            this.setActionType(ActionType.ATTACK);
            makeAttackIntent(enemies);
        } else {
            this.setActionType(ActionType.NONE);
        }
    }

    @Override
    public ArrayList<Unit> getEnemiesInSight(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "getEnemiesInSight() contains this";

        ArrayList<Unit> enemiesInSight = new ArrayList<Unit>();
        for (Unit enemy : enemies) {
            int range = this.calculateRange(enemy.getPosition());
            if (range > this.vision) {
                continue;
            }

            if (this.attackableUnitType.contains(enemy.unitType)) {
                enemiesInSight.add(enemy);
            }
        }

        return enemiesInSight;
    }

    @Override
    protected void makeAttackIntent(ArrayList<Unit> enemies) {
        assert (!enemies.contains(this)) : "makeAttackIntent() : enemies contains this";

        ArrayList<Unit> enemiesInAttackZone = getEnemiesInAttackZone(enemies);
        this.attackIntent = new AttackIntent(this, createPositionToDamageMapWithAoE1(this.position), this.attackableUnitType);
    }
}

Destroyer.java ------------------------------------------------

public class Destroyer extends Unit {
    public Destroyer(IntVector2D position) {
        super(position, 'D', UnitType.GROUND, 0, 3, 0, 9999, 100, EnumSet.of(UnitType.AIR, UnitType.GROUND, UnitType.INVISIBLE), true, false);
    }

    @Override
    public void onSpawn() {
        this.actionType = ActionType.ATTACK;
    }

    @Override
    public void onDeath() {
    }

    @Override
    public AttackIntent attack() {
        SimulationManager instance = SimulationManager.getInstance();
        ArrayList<Unit> enemies = instance.getUnits();

        enemies.remove(this);

        HashMap<IntVector2D, Integer> positionToDamageMap = new HashMap<IntVector2D, Integer>();
        for (Unit enemy : enemies) {
            positionToDamageMap.put(new IntVector2D(enemy.position.getX(), enemy.position.getY()), this.attackPower);
        }

        return new AttackIntent(this, positionToDamageMap, this.attackableUnitType);
    }

    @Override
    public void onAttacked(int damage) {
        this.isAttacked = true;
        this.hp = Math.max(this.hp - 1, 0);
    }
}

SimulationManager.java ------------------------------------------------

public final class SimulationManager {
    private static SimulationManager simulationManager;
    private ArrayList<Unit> units;
    private ArrayList<IMovable> movables;
    private ArrayList<IThinkable> thinkables;
    private ArrayList<ICollisionEventListener> listeners;

    private SimulationManager() {
        this.units = new ArrayList<Unit>();
        this.movables = new ArrayList<IMovable>();
        this.thinkables = new ArrayList<IThinkable>();
        this.listeners = new ArrayList<ICollisionEventListener>();
    }

    public static SimulationManager getInstance() {
        if (simulationManager == null) {
            simulationManager = new SimulationManager();
        }

        return simulationManager;
    }

    public ArrayList<Unit> getUnits() {
        ArrayList<Unit> aliveUnits = new ArrayList<Unit>();

        for (Unit unit : this.units) {
            if (unit.isAlive()) {
                aliveUnits.add(unit);
            }
        }

        return aliveUnits;
    }

    public void spawn(Unit unit) {
        units.add(unit);

        unit.onSpawn();
    }

    public void registerThinkable(IThinkable thinkable) {
        this.thinkables.add(thinkable);
    }

    public void unregisterThinkable(IThinkable thinkable) {
        this.thinkables.remove(thinkable);
    }

    public void registerMovable(IMovable movable) {
        this.movables.add(movable);
    }

    public void unregisterMovable(IMovable movable) {
        this.movables.remove(movable);
    }

    public void registerCollisionEventListener(ICollisionEventListener listener) {
        this.listeners.add(listener);
    }

    public void unregisterCollisionEventListener(ICollisionEventListener listener) {
        this.listeners.remove(listener);
    }

    public void update() {
        ArrayList<Unit> aliveUnits = this.getUnits();

        // 각 유닛들이 이번 프레임에서 할 행동(선택지: 공격, 이동, 아무것도 안 함)을 결정
        for (IThinkable thinkable : this.thinkables) {
            thinkable.onThink();
        }

        // 움직일 수 있는 각 유닛에게 이동할 기회를 줌
        for (IMovable movable : this.movables) {
            movable.move();
        }

        // 이동 후 충돌 처리
        for (ICollisionEventListener listener : this.listeners) {
            listener.onListen();
        }

        // 각 유닛에게 공격할 기회를 줌
        ArrayList<AttackIntent> attackIntents = new ArrayList<AttackIntent>();

        for (Unit aliveUnit : aliveUnits) {
            if (aliveUnit.getActionType() == ActionType.ATTACK) {
                attackIntents.add(aliveUnit.attack());
            }
        }

        // 피해를 입어야 하는 각 유닛에게 피해를 입힘
        for (Unit aliveUnit : aliveUnits) {
            for (AttackIntent attackIntent : attackIntents) {
                if (aliveUnit == attackIntent.getAttacker()) {
                    continue;
                }

                if (!attackIntent.getAttackableUnitType().contains(aliveUnit.getUnitType())) {
                    continue;
                }

                HashMap<IntVector2D, Integer> positionToDamageMap = attackIntent.getPositionToDamageMap();
                if (positionToDamageMap.containsKey(aliveUnit.getPosition())) {
                    aliveUnit.onAttacked(positionToDamageMap.get(aliveUnit.getPosition()));
                }
            }
        }

        // 망령이 한번이라도 공격 받았다면, 쉴드를 깨는 로직
        for (Unit aliveUnit : aliveUnits) {
            if (aliveUnit.hasShield() && aliveUnit.isAttacked()) {
                aliveUnit.breakShield();
            }
        }

        // 죽은 유닛들을 모두 게임에서 제거함
        for (Unit aliveUnit : aliveUnits) {
            if (!aliveUnit.isAlive()) {
                aliveUnit.onDeath();

                units.remove(aliveUnit);
            }
        }
    }
}