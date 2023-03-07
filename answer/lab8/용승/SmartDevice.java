package academy.pocu.comp2500.lab8;

public abstract class SmartDevice {
    private int currentTick;
    private int lastUpdatedTick;
    private boolean isOn;

    public SmartDevice() {
        this.currentTick = 0;
        this.lastUpdatedTick = 0;
        this.isOn = false;
    }

    public boolean isOn() {
        return this.isOn;
    }

    protected void turnOff() {
        this.isOn = false;
    }

    protected void turnOn() {
        this.isOn = true;
    }

    protected void setLastUpdatedTick(int lastUpdatedTick) {
        this.lastUpdatedTick = lastUpdatedTick;
    }

    public void onTick() {
        this.currentTick++;
    }

    public int getCurrentTick() {
        return this.currentTick;
    }

    public int getTicksSinceLastUpdate() {
        return this.currentTick - this.lastUpdatedTick;
    }

    public abstract void onInstallation(Planter planter);
}
