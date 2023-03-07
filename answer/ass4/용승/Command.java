package academy.pocu.comp2500.assignment4;

public abstract class Command {
    protected final int x;
    protected final int y;
    protected boolean wasExecuted;
    protected boolean wasUndone;
    protected Canvas canvasReference;

    public Command(int x, int y) {
        this.x = x;
        this.y = y;
        this.wasExecuted = false;
        this.wasUndone = false;
    }

    protected final boolean canUndo() {
        return this.wasExecuted && !this.wasUndone
                && this.canCurrentStateBeUndone();
    }

    protected final boolean canRedo() {
        return this.wasUndone && this.canCurrentStateBeRedone();
    }

    protected abstract void saveOriginalState(Canvas canvas);

    protected abstract boolean canCurrentStateBeUndone();
    protected abstract void restoreOriginalState();

    protected abstract boolean canCurrentStateBeRedone();
}
