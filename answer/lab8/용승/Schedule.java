package academy.pocu.comp2500.lab8;

public class Schedule {
    private final int tickToStart;
    private final int length;

    public Schedule(int tickToStart, int scheduleLengthInTicks) {
        assert (tickToStart >= 0 && scheduleLengthInTicks > 0);

        this.tickToStart = tickToStart;
        this.length = scheduleLengthInTicks;
    }

    public int getTickToStart() {
        return this.tickToStart;
    }

    public int getLength() {
        return this.length;
    }
}
