package academy.pocu.comp2500.assignment4;

public class FillHorizontalLineCommand extends Command implements ICommand {
    private final char character;
    private char[] lineBefore;

    public FillHorizontalLineCommand(int y, char character) {
        super(0, y);
        this.character = character;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (!this.wasExecuted) {
            try {
                saveOriginalState(canvas);
                canvas.fillHorizontalLine(this.y, this.character);
                this.canvasReference = canvas;
                this.wasExecuted = true;
                return true;
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
            this.canvasReference.fillHorizontalLine(this.y, this.character);
            this.wasUndone = false;
            return true;
        }
        return false;
    }

    @Override
    protected void saveOriginalState(Canvas canvas) {
        int width = canvas.getWidth();

        this.lineBefore = new char[width];
        for (int x = 0; x < width; ++x) {
            this.lineBefore[x] = canvas.getPixel(x, this.y);
        }
    }

    @Override
    protected boolean canCurrentStateBeUndone() {
        int width = this.canvasReference.getWidth();

        for (int x = 0; x < width; ++x) {
            if (this.canvasReference.getPixel(x, this.y) != this.character) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void restoreOriginalState() {
        for (int x = 0; x < this.lineBefore.length; x++) {
            this.canvasReference.drawPixel(x, this.y, this.lineBefore[x]);
        }
    }

    @Override
    protected boolean canCurrentStateBeRedone() {
        int width = this.canvasReference.getWidth();

        for (int x = 0; x < width; ++x) {
            if (this.canvasReference.getPixel(x, this.y) != this.lineBefore[x]) {
                return false;
            }
        }
        return true;
    }
}
