package academy.pocu.comp2500.assignment4;

public class ToUpperCommand extends AbstractCommand implements ICommand {
    private final int x;
    private final int y;
    private char previousChar;

    public ToUpperCommand(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (isExecuted()) {
            return false;
        }

        if (x < 0 || y < 0 || x >= canvas.getWidth() || y >= canvas.getHeight()) {
            return false;
        }

        executedCanvas = canvas;
        previousChar = canvas.getPixel(x, y);
        canvas.toUpper(x, y);
        onUpdate();
        return true;
    }

    @Override
    public boolean undo() {
        if (!canUndo()) {
            return false;
        }

        isUndid = true;

        executedCanvas.drawPixel(x, y, previousChar);
        onUpdate();
        return true;
    }

    @Override
    public boolean redo() {
        if (!canRedo()) {
            return false;
        }

        isUndid = false;

        executedCanvas.toUpper(x, y);
        onUpdate();
        return true;
    }
}
