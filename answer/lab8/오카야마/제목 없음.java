-----------------------------------------------------
// Schedule.java

public class Schedule {
    private int startTick;
    private int duration;

    public Schedule(int startTick, int duration) {
        this.startTick = startTick;
        this.duration = duration;
    }

    public int getStartTick() {
        return this.startTick;
    }

    public int getDuration() {
        return this.duration;
    }
}

-----------------------------------------------------
// SmartDevice.java

public abstract class SmartDevice {
    protected boolean isOn;
    protected int ticksSinceLastUpdate;
    protected int currentTick;

    public SmartDevice() {
        this.isOn = false;

        this.currentTick = 0;
        this.ticksSinceLastUpdate = 0;
    }

    public abstract void onInstall(Planter planter);

    public abstract void onTick();
    public boolean isOn() {
        return this.isOn;
    }

    public int getTicksSinceLastUpdate() {
        return this.ticksSinceLastUpdate;
    }

    public void switchPower() {
        this.isOn = !this.isOn;
        this.ticksSinceLastUpdate = 0;
    }
}

-----------------------------------------------------
// Sprinkler.java

public class Sprinkler extends SmartDevice implements ISprayable {
    private static final int SPRAY_PER_TICK = 15;
    private ArrayList<Schedule> schedules;
    private int scheduledTicks;

    public Sprinkler() {
        this.scheduledTicks = 0;
        this.schedules = new ArrayList<Schedule>();
    }

    @Override
    public void onInstall(Planter planter) {
        planter.registerSprayable(this);
    }

    @Override
    public void spray(Planter planter) {
        if (isOn) {
            planter.addWater(SPRAY_PER_TICK);
        }
    }

    @Override
    public void onTick() {
        ++this.currentTick;

        this.checkSchedule();
    }

    public void addSchedule(Schedule schedule) {
        assert (schedule.getStartTick() >= 0);

        int startTick = schedule.getStartTick();
        int endTick = startTick + schedule.getDuration();

        if (startTick == 0) {
            return;
        }

        if (startTick > this.scheduledTicks) {
            schedules.add(schedule);
        }

        if (endTick > this.scheduledTicks) {
            this.scheduledTicks = endTick;
        }
    }

    private void checkSchedule() {
        for (Schedule schedule : this.schedules) {
            int startTick = schedule.getStartTick();
            int endTick = startTick + schedule.getDuration();

            if (this.currentTick >= startTick && this.currentTick < endTick) {
                if (this.isOn) {
                    ++this.ticksSinceLastUpdate;
                } else {
                    this.switchPower();
                }
            }
        }

        if (!this.isOn) {
            ++this.ticksSinceLastUpdate;
        } else {
            this.switchPower();
        }
    }
}

-----------------------------------------------------
// Drainer.java

public class Drainer extends SmartDevice implements IWaterDetectable, IDrainable {
    private static final int DRAIN_PER_TICK = 7;
    private final int waterThreshold;

    public Drainer(int waterThreshold) {
        this.waterThreshold = waterThreshold;
    }

    @Override
    public void onInstall(Planter planter) {
        planter.registerDrainable(this);
        planter.registerDetectable(this);
    }

    @Override
    public void drain(Planter planter) {
        if (isOn()) {
            planter.reduceWaterAmount(DRAIN_PER_TICK);
        }
    }
    @Override
    public void onTick() {
        ++this.currentTick;
    }

    @Override
    public void detect(final int waterLevel) {
        if (waterLevel >= this.waterThreshold) {
            if (!this.isOn) {
                this.switchPower();

                return;
            }
        } else {
            if (this.isOn) {
                this.switchPower();

                return;
            }
        }

        ++this.ticksSinceLastUpdate;
    }
}

-----------------------------------------------------
// Planter.java

public class Planter {
    private ArrayList<SmartDevice> smartDevices;
    private ArrayList<IDrainable> drainables;
    private ArrayList<SmartDevice> drainers;
    private ArrayList<IWaterDetectable> detectables;
    private ArrayList<ISprayable> sprayables;
    private int waterAmount;

    public Planter(int waterAmount) {
        this.waterAmount = waterAmount;

        this.smartDevices = new ArrayList<SmartDevice>();
        this.drainables = new ArrayList<IDrainable>();
        this.detectables = new ArrayList<IWaterDetectable>();
        this.sprayables = new ArrayList<ISprayable>();
    }

    public void installSmartDevice(SmartDevice smartDevice) {
        smartDevice.onInstall(this);

        this.smartDevices.add(smartDevice);
    }

    public void registerSprayable(ISprayable sprayable) {
        this.sprayables.add(sprayable);
    }

    public void registerDetectable(IWaterDetectable detectable) {
        this.detectables.add(detectable);
    }

    public void registerDrainable(IDrainable drainable) {
        this.drainables.add(drainable);
    }

    public int getWaterAmount() {
        return this.waterAmount;
    }

    public void absorbWater() {
        this.waterAmount = Math.max(0, this.waterAmount - 2);
    }

    public void addWater(int waterAmount) {
        this.waterAmount += waterAmount;
    }

    public void reduceWaterAmount(int waterAmount) {
        this.waterAmount = Math.max(0, this.waterAmount - waterAmount);
    }

    public void tick() {
        for (SmartDevice smartDevice : this.smartDevices) {
            smartDevice.onTick();
        }

        for (IWaterDetectable detectable : this.detectables) {
            detectable.detect(this.waterAmount);
        }

        for (ISprayable sprayable : this.sprayables) {
            sprayable.spray(this);
        }

        for (IDrainable drainable : this.drainables) {
            drainable.drain(this);
        }

        this.absorbWater();
    }
}