package academy.pocu.comp2500.assignment4;

public class DrawPixelCommand extends AbstractCommand implements ICommand {
    private final int x;
    private final int y;
    private final char character;
    private char previousChar;

    public DrawPixelCommand(int x, int y, char character) {
        this.x = x;
        this.y = y;
        this.character = character;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (isExecuted()) {
            return false;
        }

        if (x < 0 || y < 0 || x >= canvas.getWidth() || y >= canvas.getHeight()) {
            return false;
        }

        previousChar = canvas.getPixel(x, y);
        executedCanvas = canvas;

        canvas.drawPixel(x, y, character);
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

        executedCanvas.drawPixel(x, y, character);
        onUpdate();
        return true;
    }
}
