package academy.pocu.comp2500.assignment4;

public class IncreasePixelCommand extends Command implements ICommand {
    private char characterBeforeIncrease;

    public IncreasePixelCommand(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (!this.wasExecuted) {
            try {
                saveOriginalState(canvas);
                if (canvas.increasePixel(this.x, this.y)) {
                    this.canvasReference = canvas;
                    this.wasExecuted = true;
                    return true;
                }
            } catch (IllegalArgumentException exception) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean undo() {
        if (canUndo()) {
            restoreOriginalState();
            this.wasUndone = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean redo() {
        if (canRedo()) {
            this.canvasReference.increasePixel(this.x, this.y);
            this.wasUndone = false;
            return true;
        }
        return false;
    }


    @Override
    protected void saveOriginalState(Canvas canvas) {
        this.characterBeforeIncrease = canvas.getPixel(this.x, this.y);
    }

    @Override
    protected boolean canCurrentStateBeUndone() {
        return this.canvasReference.getPixel(this.x, this.y) - 1 == this.characterBeforeIncrease;
    }

    @Override
    protected void restoreOriginalState() {
        this.canvasReference.decreasePixel(this.x, this.y);
    }

    @Override
    protected boolean canCurrentStateBeRedone() {
        return this.canvasReference.getPixel(this.x, this.y) == this.characterBeforeIncrease;
    }
}
