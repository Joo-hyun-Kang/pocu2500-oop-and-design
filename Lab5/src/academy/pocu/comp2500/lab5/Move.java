package academy.pocu.comp2500.lab5;

public class Move {
    private String name;
    private int power;
    private int powerPoint;
    private final int MAX_POWER_POINT;

    public Move(String name, int power, int powerPoint) {
        this.name = name;
        this.power = power;
        this.powerPoint = powerPoint;
        this.MAX_POWER_POINT = powerPoint;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public int getPowerPoint() {
        return powerPoint;
    }

    public int getMaxPowerPoint() {
        return MAX_POWER_POINT;
    }

    public void plusPowerPoint() {
        this.powerPoint++;
    }

    public void minusPowerPoint() {
        if (this.powerPoint > 0) {
            this.powerPoint--;
        }
    }
}
