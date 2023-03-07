package academy.pocu.comp2500.lab8;

public class Drainer extends SmartDevice implements IDrainable, IWaterDetectable {
    private static final int WATER_AMOUNT_DRAINED_PER_TICK = 7;

    private final int waterAmountToTurnOn;

    public Drainer(int waterAmountToTurnOnInLiter) {
        this.waterAmountToTurnOn = waterAmountToTurnOnInLiter;
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void onInstallation(Planter planter) {
        planter.registerDrainable(this);
        planter.registerWaterDetectable(this);
    }

    @Override
    public void drain(Planter planter) {
        assert (planter != null);

        if (super.isOn()) {
            planter.adjustWaterAmount(WATER_AMOUNT_DRAINED_PER_TICK * (-1));
        }
    }

    @Override
    public void detect(int waterLevel) {
        if (!super.isOn()) {
            if (waterLevel >= waterAmountToTurnOn) {
                super.turnOn();
                setLastUpdatedTick(getCurrentTick());
            }
            return;
        }

        if (waterLevel < waterAmountToTurnOn) {
            super.turnOff();
            setLastUpdatedTick(getCurrentTick());
        }
    }
}
