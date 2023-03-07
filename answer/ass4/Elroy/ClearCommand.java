package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public class ClearCommand extends AbstractCommand implements ICommand {
    private ArrayList<ArrayList<Character>> previousBoard = new ArrayList<>();

    @Override
    public boolean execute(Canvas canvas) {
        if (isExecuted()) {
            return false;
        }

        executedCanvas = canvas;
        setPreviousBoard(canvas);
        canvas.clear();
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
            for (int x = 0; x < executedCanvas.getWidth(); ++x) {
                executedCanvas.drawPixel(x, y, previousBoard.get(y).get(x));
            }
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

        executedCanvas.clear();
        onUpdate();
        return true;
    }

    private void setPreviousBoard(Canvas canvas) {
        for (int y = 0; y < canvas.getHeight(); ++y) {
            previousBoard.add(new ArrayList<>());
            for (int x = 0; x < canvas.getWidth(); ++x) {
                previousBoard.get(y).add(canvas.getPixel(x, y));
            }
        }
    }
}
