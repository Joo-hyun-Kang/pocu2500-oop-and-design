package academy.pocu.comp2500.assignment4;

public class IncreasePixelCommand extends AbstractCommand implements ICommand {
    private final int x;
    private final int y;

    public IncreasePixelCommand(int x, int y) {
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
        boolean isSuccess = executedCanvas.increasePixel(x, y);
        onUpdate();
        return isSuccess;
    }

    @Override
    public boolean undo() {
        if (!canUndo()) {
            return false;
        }

        isUndid = true;
        boolean isSuccess = executedCanvas.decreasePixel(x, y);
        onUpdate();
        return isSuccess;
    }

    @Override
    public boolean redo() {
        if (!canRedo()) {
            return false;
        }

        isUndid = false;

        boolean isSuccess = executedCanvas.increasePixel(x, y);
        onUpdate();
        return isSuccess;
    }
}
