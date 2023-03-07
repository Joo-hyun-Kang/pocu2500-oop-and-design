// Marine.java
package academy.pocu.comp2500.assignment3;

import java.util.ArrayList;
import java.util.Arrays;

public class Marine extends MovableUnit {
//    private IntVector2D targetTile;
//
//    public IntVector2D getTargetTile() {
//        return this.targetTile;
//    }
//
//    public void setTargetTile() {
//        this.targetTile =
//    }

    public Marine(IntVector2D position) {
        super(Symbol.M, UnitType.GROUND, 2, 0, 35, 6, new ArrayList<>(Arrays.asList(UnitType.GROUND, UnitType.AIR)), position, false, AttackRangeType.FULL_CROSS, 1, false);
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

        // 공격 범위에 없는 경우
        ArrayList<Unit> closestEnemies = findTheClosestEnemies(enemiesInSight);
        if (closestEnemies.size() == 1) {
            setAction(Action.MOVE);
            setTargetTile(getNextPositionOnTheWay(closestEnemies.get(0).getPosition()));

            return;
        } else if (closestEnemies.size() > 1) {
            String me = super.toString(); // debug
            if (checkEnemiesInSameTile(closestEnemies)) {
                setAction(Action.MOVE);
                setTargetTile(getNextPositionOnTheWay(closestEnemies.get(0).getPosition()));

                return;
            }

            ArrayList<Unit> lowestHpEnemies = findTheLowestHpEnemies(closestEnemies);
            if (lowestHpEnemies.size() == 1) {
                setAction(Action.MOVE);
                setTargetTile(getNextPositionOnTheWay(lowestHpEnemies.get(0).getPosition()));

                return;
            } else if (lowestHpEnemies.size() > 1) {
                if (checkEnemiesInSameTile(lowestHpEnemies)) {
                    setAction(Action.MOVE);
                    setTargetTile(getNextPositionOnTheWay(lowestHpEnemies.get(0).getPosition()));

                    return;
                }
            }

            Unit firstUnitInClockWise = findFirstUnitInClockWiseOrNull(lowestHpEnemies);
            setAction(Action.MOVE);
            setTargetTile(getNextPositionOnTheWay(firstUnitInClockWise.getPosition())); // 수상
        }
    }
}

// Tank.java
package academy.pocu.comp2500.assignment3;

import java.util.ArrayList;
import java.util.Arrays;

public class Tank extends MovableUnit {
    private boolean isSiegeMode = false;
    private Direction lastMoveDirection = Direction.EAST;

    public Tank(IntVector2D position) {
        super(Symbol.T, UnitType.GROUND, 3, 1, 85, 8, new ArrayList<>(Arrays.asList(UnitType.GROUND)), position, false, AttackRangeType.EMPTY_RECTANGLE_WITHOUT_VERTEX, 2, false);
    }

    public void onSpawn() {
        super.onSpawn();
    }

    @Override
    public void move() {
        System.out.printf("%s -> ", super.toString());
        switch (lastMoveDirection) {
            case WEST:
                if (getPosition().getX() - 1 < 0) {
                    getPosition().setX(getPosition().getX() + 1);
                    lastMoveDirection = Direction.EAST;
                } else {
                    getPosition().setX(getPosition().getX() - 1);
                    lastMoveDirection = Direction.WEST;
                }
                break;
            default:
                if (getPosition().getX() + 1 > 15) {
                    getPosition().setX(getPosition().getX() - 1);
                    lastMoveDirection = Direction.WEST;
                } else {
                    getPosition().setX(getPosition().getX() + 1);
                    lastMoveDirection = Direction.EAST;
                }
                break;
        }
        System.out.printf("%s%s", getPosition().toString(), System.lineSeparator());
    }

    @Override
    public void think() {
        ArrayList<Unit> enemiesInSight = findAllEnemiesInSight();
        if (enemiesInSight.size() == 0) {
            if (isSiegeMode()) {
                toggleSiegeMode();
                setAction(Action.NOTHING);

                return;
            } else {
                setAction(Action.MOVE);

                return;
            }
        }

        if (!isSiegeMode()) {
            toggleSiegeMode();
            setAction(Action.NOTHING);

            return;
        }

        ArrayList<Unit> enemiesInAttackRange = findAllEnemiesInAttackRange();
        if (enemiesInAttackRange.size() != 0) { // 공격 범위에 있는 경우
            if (!isSiegeMode()) {
                toggleSiegeMode();
                setAction(Action.NOTHING);

                return;
            }
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

    @Override
    public void onAttacked(int damage) {
        if (isSiegeMode()) {
            System.out.printf(" It's a critical damage %d!", 2 * damage);
            super.onAttacked(2 * damage);
        } else {
            super.onAttacked(damage);
        }
    }

    public boolean isSiegeMode() {
        return this.isSiegeMode;
    }

    public void toggleSiegeMode() {
        if (isSiegeMode) {
            System.out.printf("%s toggles to tank mode!%s", this.toString(), System.lineSeparator());
        } else {
            System.out.printf("%s toggles to siege mode!%s", this.toString(), System.lineSeparator());
        }

        isSiegeMode ^= true;
    }
}

// Wraith.java
package academy.pocu.comp2500.assignment3;

import java.util.ArrayList;
import java.util.Arrays;

public class Wraith extends MovableUnit {
    private final IntVector2D INITIAL_POSITION;
    private boolean hasShield = true;
    private final int SHIELD_DURATION = 1;
    private int shieldBrokenElapsedTime = 0;
    private boolean isShieldAttacked = false;
    private boolean isTimerOn = false;

    public Wraith(IntVector2D position) {
        super(Symbol.W, UnitType.AIR, 4, 0, 80, 6, new ArrayList<>(Arrays.asList(UnitType.GROUND, UnitType.AIR)), position, false, AttackRangeType.FULL_CROSS, 1, false);
        INITIAL_POSITION = new IntVector2D(position.getX(), position.getY());
    }

    public IntVector2D getInitialPosition() {
        return INITIAL_POSITION;
    }

    public void setShieldOff() {
        this.hasShield = false;
    }

    public int getShieldDuration() {
        return this.SHIELD_DURATION;
    }

    public int getShieldBrokenElapsedTime() {
        return shieldBrokenElapsedTime;
    }

    public void increaseShieldBrokenElapsedTime() {
        this.shieldBrokenElapsedTime++;
    }

    public void resetShieldBrokenElapsedTime() {
        this.shieldBrokenElapsedTime = 0;
    }

    public boolean isTimerOn() {
        return this.isTimerOn;
    }

    public void setTimerOn() {
        this.isTimerOn = true;
    }

    public void setTimerOff() {
        this.isTimerOn = false;
        resetShieldBrokenElapsedTime();
    }

    public void onSpawn() {
        super.onSpawn();
    }

    @Override
    public void onAttacked(int damage) {
        if (!isShieldAttacked) {
            isShieldAttacked = true;
            activateShieldDisabledTimer();
            System.out.print(" Shield got attacked for the first time!");
        }

        if (hasShield) {
            System.out.printf(" %s's shield blocks %d damage!%s", this.toString(), damage, System.lineSeparator());
        } else {
            super.onAttacked(damage);
        }
    }

    public void activateShieldDisabledTimer() {
        setTimerOn();
    }

    @Override
    public void think() {
        ArrayList<Unit> enemiesInSight = findAllEnemiesInSight();
        if (enemiesInSight.size() == 0) {
            setAction(Action.MOVE);
            setTargetTile(getNextPositionOnTheWay(this.INITIAL_POSITION));

            return;
        }

        ArrayList<Unit> enemiesInAttackRange = findAllEnemiesInAttackRange();
        enemiesInAttackRange = getOnlyAirOrGroundUnits(enemiesInAttackRange);
        if (enemiesInAttackRange.size() != 0) { // 공격 범위에 있는 경우  // enemiesInAttackRange 에 공중 유닛 또는 지상 유닛 한 종류만 남아 있음
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

        // 공격 범위에 없는 경우
        enemiesInSight = getOnlyAirOrGroundUnits(enemiesInSight);
        ArrayList<Unit> closestEnemies = findTheClosestEnemies(enemiesInSight);
        if (closestEnemies.size() == 1) {
            setAction(Action.MOVE);
            setTargetTile(getNextPositionOnTheWay(closestEnemies.get(0).getPosition()));

            return;
        } else if (closestEnemies.size() > 1) {
            String me = super.toString(); // debug
            if (checkEnemiesInSameTile(closestEnemies)) {
                setAction(Action.MOVE);
                setTargetTile(getNextPositionOnTheWay(closestEnemies.get(0).getPosition()));

                return;
            }

            ArrayList<Unit> lowestHpEnemies = findTheLowestHpEnemies(closestEnemies);
            if (lowestHpEnemies.size() == 1) {
                setAction(Action.MOVE);
                setTargetTile(getNextPositionOnTheWay(lowestHpEnemies.get(0).getPosition()));

                return;
            } else if (lowestHpEnemies.size() > 1) {
                if (checkEnemiesInSameTile(lowestHpEnemies)) {
                    setAction(Action.MOVE);
                    setTargetTile(getNextPositionOnTheWay(lowestHpEnemies.get(0).getPosition()));

                    return;
                }
            }

            Unit firstUnitInClockWise = findFirstUnitInClockWiseOrNull(lowestHpEnemies);
            setAction(Action.MOVE);
            setTargetTile(getNextPositionOnTheWay(firstUnitInClockWise.getPosition()));
        }
    }
    
    private ArrayList<Unit> getOnlyAirOrGroundUnits(ArrayList<Unit> units) {
        boolean hasAirUnit = false;
        ArrayList<Unit> toBeRemoved = new ArrayList<>();
        if (units.size() > 0) {
            for (Unit e : units) {
                if (e.getType().equals(UnitType.GROUND)) {
                    toBeRemoved.add(e);
                }
                if (e.getType().equals(UnitType.AIR)) {
                    hasAirUnit = true;
                }
            }

            if (hasAirUnit) {
                units.removeAll(toBeRemoved); // 공중 유닛만 남김
            }
        }     
        
        return units;
    }

    public void resetActionStatus() {
        super.resetActionStatus();
        if (isTimerOn) {
            increaseShieldBrokenElapsedTime();
            if (getShieldBrokenElapsedTime() >= getShieldDuration()) {
                setShieldOff();
                setTimerOff();
            }
        }
    }
}