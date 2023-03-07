package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public class FillVerticalLineCommand extends AbstractCommand implements ICommand {
    private final int x;
    private final char character;
    private final ArrayList<Character> previousPixels = new ArrayList<>();

    public FillVerticalLineCommand(int x, char character) {
        this.x = x;
        this.character = character;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (isExecuted()) {
            return false;
        }

        if (x < 0 || x >= canvas.getWidth()) {
            return false;
        }

        executedCanvas = canvas;
        savePreviousChar(canvas);
        canvas.fillVerticalLine(x, character);
        onUpdate();
        return true;
    }

    @Override
    public boolean undo() {
        if (!canUndo()) {
            return false;
        }

        isUndid = true;

        for (int y = 0; y < executedCanvas.getHeight(); ++y) {
            executedCanvas.drawPixel(x, y, previousPixels.get(y));
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

        executedCanvas.fillVerticalLine(x, character);
        onUpdate();
        return true;
    }

    private void savePreviousChar(Canvas canvas) {
        for (int y = 0; y < canvas.getHeight(); ++y) {
            previousPixels.add(canvas.getPixel(x, y));
        }
    }
}
