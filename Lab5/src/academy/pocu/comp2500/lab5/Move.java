package academy.pocu.comp2500.lab5;

public class Move {
    private String name;
    private int power;
    private int powerPoint;
    private final int MAX_POWERPOINT;

    public Move(String name, int power, int powerPoint) {
        this.name = name;
        this.power = power;
        this.powerPoint = powerPoint;
        this.MAX_POWERPOINT = powerPoint;
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

    public int getMAX_POWERPOINT() {
        return MAX_POWERPOINT;
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
