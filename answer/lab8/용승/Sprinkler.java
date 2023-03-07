package academy.pocu.comp2500.lab8;

import java.util.ArrayList;

public class Sprinkler extends SmartDevice implements ISprayable {
    private static final int WATER_AMOUNT_SPRAYED_PER_TICK = 15;

    private final ArrayList<Schedule> schedules;

    public Sprinkler() {
        this.schedules = new ArrayList<>();
    }

    public void addSchedule(Schedule schedule) {
        assert (schedule != null);

        this.schedules.add(schedule);
    }

    @Override
    public void onInstallation(Planter planter) {
        assert (planter != null);

        planter.registerSprayable(this);
    }

    @Override
    public void onTick() {
        super.onTick();

        if (super.isOn()) {
            Schedule schedule = this.schedules.get(0);
            if (isScheduleEnded(schedule)) {
                super.turnOff();
                this.schedules.remove(0);
                setLastUpdatedTick(super.getCurrentTick());
            }
            return;
        }

        Schedule nextSchedule = getValidNextScheduleOrNull();
        if (nextSchedule != null && nextSchedule.getTickToStart() == super.getCurrentTick()) {
            super.turnOn();
            setLastUpdatedTick(super.getCurrentTick());
        }
    }

    @Override
    public void spray(Planter planter) {
        assert (planter != null);

        if (super.isOn()) {
            planter.adjustWaterAmount(WATER_AMOUNT_SPRAYED_PER_TICK);
        }
    }

    private Schedule getValidNextScheduleOrNull() {
        Schedule schedule = null;
        while (!this.schedules.isEmpty()) {
            schedule = this.schedules.get(0);
            if (schedule.getTickToStart() != 0 && isTickToEndBiggerThanOrEqualToCurrentTick(schedule)) {
                break;
            }
            this.schedules.remove(0);
        }
        return schedule;
    }

    private boolean isScheduleEnded(Schedule schedule) {
        return super.getTicksSinceLastUpdate() == schedule.getLength();
    }

    private boolean isTickToEndBiggerThanOrEqualToCurrentTick(Schedule schedule) {
        return schedule.getTickToStart() + schedule.getLength() >= super.getCurrentTick();
    }
}
