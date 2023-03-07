package academy.pocu.comp2500.assignment4;

public class FillVerticalLineCommand extends Command implements ICommand {
    private final char character;
    private char[] lineBefore;

    public FillVerticalLineCommand(int x, char character) {
        super(x, 0);
        this.character = character;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (!this.wasExecuted) {
            try {
                this.canvasReference = canvas;
                saveOriginalState(canvas);
                canvas.fillVerticalLine(this.x, this.character);
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
            this.canvasReference.fillVerticalLine(this.x, this.character);
            this.wasUndone = false;
            return true;
        }
        return false;
    }

    @Override
    protected void saveOriginalState(Canvas canvas) {
        int height = canvas.getHeight();
        this.lineBefore = new char[height];
        for (int y = 0; y < height; ++y) {
            this.lineBefore[y] = canvas.getPixel(this.x, y);
        }
    }

    @Override
    protected boolean canCurrentStateBeUndone() {
        int height = this.canvasReference.getHeight();
        for (int y = 0; y < height; ++y) {
            if (this.canvasReference.getPixel(this.x, y) != this.character) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void restoreOriginalState() {
        for (int y = 0; y < this.lineBefore.length; y++) {
            this.canvasReference.drawPixel(this.x, y, this.lineBefore[y]);
        }
    }

    @Override
    protected boolean canCurrentStateBeRedone() {
        int height = this.canvasReference.getHeight();

        for (int y = 0; y < height; ++y) {
            if (this.canvasReference.getPixel(this.x, y) != this.lineBefore[y]) {
                return false;
            }
        }
        return true;
    }
}
