package academy.pocu.comp2500.assignment4;

public class ClearCommand extends Command implements ICommand {
    private char[][] canvasBefore;

    public ClearCommand() {
        super(0, 0);
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (!this.wasExecuted) {
            saveOriginalState(canvas);
            canvas.clear();
            this.canvasReference = canvas;
            this.wasExecuted = true;
            return true;
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
            this.canvasReference.clear();
            this.wasUndone = false;
            return true;
        }
        return false;
    }

    @Override
    protected void saveOriginalState(Canvas canvas) {
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        this.canvasBefore = new char[height][width];

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                this.canvasBefore[y][x] = canvas.getPixel(x, y);
            }
        }
    }

    @Override
    protected boolean canCurrentStateBeUndone() {
        int height = canvasReference.getHeight();
        int width = canvasReference.getWidth();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (this.canvasReference.getPixel(x, y) != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void restoreOriginalState() {
        int height = canvasReference.getHeight();
        int width = canvasReference.getWidth();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                canvasReference.drawPixel(x, y, this.canvasBefore[y][x]);
            }
        }
    }

    @Override
    protected boolean canCurrentStateBeRedone() {
        int height = canvasReference.getHeight();
        int width = canvasReference.getWidth();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (this.canvasReference.getPixel(x, y) != this.canvasBefore[y][x]) {
                    return false;
                }
            }
        }
        return true;
    }
}
