package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public class FillHorizontalLineCommand extends AbstractCommand implements ICommand {
    private final int y;
    private final char character;
    private final ArrayList<Character> previousPixels = new ArrayList<>();

    public FillHorizontalLineCommand(int y, char character) {
        this.y = y;
        this.character = character;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (isExecuted()) {
            return false;
        }

        if (y < 0 || y >= canvas.getHeight()) {
            return false;
        }

        executedCanvas = canvas;
        savePreviousChar(canvas);
        canvas.fillHorizontalLine(y, character);
        onUpdate();
        return true;
    }

    @Override
    public boolean undo() {
        if (!canUndo()) {
            return false;
        }

        isUndid = true;

        for (int x = 0; x < executedCanvas.getWidth(); ++x) {
            executedCanvas.drawPixel(x, y, previousPixels.get(x));
        }

        onUpdate();
        return true;
    }

    @Override
    public boolean redo() {
        if (!canRedo()) {
            return false;
        }

        isUndid = false;

        executedCanvas.fillHorizontalLine(y, character);
        onUpdate();
        return true;
    }

    private void savePreviousChar(Canvas canvas) {
        for (int x = 0; x < canvas.getWidth(); ++x) {
            previousPixels.add(canvas.getPixel(x, y));
        }
    }
}
