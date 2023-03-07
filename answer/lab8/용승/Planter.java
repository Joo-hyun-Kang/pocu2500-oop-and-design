package academy.pocu.comp2500.lab8;

import java.util.HashSet;

public class Planter {
    private static final int WATER_AMOUNT_IN_LITER_CONSUMED_PER_TICK = 2;

    private int waterAmountInLiter;
    private final HashSet<SmartDevice> smartDevices;
    private final HashSet<ISprayable> sprayables;
    private final HashSet<IDrainable> drainables;
    private final HashSet<IWaterDetectable> waterDetectables;

    public Planter(int initialWaterAmountInLiter) {
        assert (initialWaterAmountInLiter >= 0);

        this.waterAmountInLiter = initialWaterAmountInLiter;
        this.smartDevices = new HashSet<>();
        this.sprayables = new HashSet<>();
        this.drainables = new HashSet<>();
        this.waterDetectables = new HashSet<>();
    }

    public int getWaterAmount() {
        return this.waterAmountInLiter;
    }

    public void installSmartDevice(SmartDevice smartDevice) {
        assert (smartDevice != null);

        this.smartDevices.add(smartDevice);
        smartDevice.onInstallation(this);
    }

    public void registerSprayable(ISprayable sprayable) {
        this.sprayables.add(sprayable);
    }

    public void registerDrainable(IDrainable drainable) {
        this.drainables.add(drainable);
    }

    public void registerWaterDetectable(IWaterDetectable waterDetectable) {
        this.waterDetectables.add(waterDetectable);
    }

    public void tick() {
        for (SmartDevice smartDevice : this.smartDevices) {
            smartDevice.onTick();
            for (IWaterDetectable waterDetectable : this.waterDetectables) {
                waterDetectable.detect(getWaterAmount());
            }
        }

        for (ISprayable sprayable : this.sprayables) {
            sprayable.spray(this);
        }

        for (IDrainable drainable : this.drainables) {
            drainable.drain(this);
        }

        this.waterAmountInLiter = Math.max(0, this.waterAmountInLiter - WATER_AMOUNT_IN_LITER_CONSUMED_PER_TICK);
    }

    public void adjustWaterAmount(int waterAmountInLiter) {
        this.waterAmountInLiter = Math.max(this.waterAmountInLiter + waterAmountInLiter, 0);
    }
}
