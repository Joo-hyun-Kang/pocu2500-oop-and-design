package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public abstract class AbstractCommand {
    protected Canvas executedCanvas;
    protected boolean isUndid;
    protected ArrayList<ArrayList<Character>> lastUpdatedBoard;

    protected boolean canUndo() {
        if (executedCanvas == null) {
            return false;
        }

        if (isUndid) {
            return false;
        }

        return cloneBoard(executedCanvas).equals(lastUpdatedBoard);
    }

    protected boolean canRedo() {
        if (executedCanvas == null) {
            return false;
        }

        if (!isUndid) {
            return false;
        }

        return cloneBoard(executedCanvas).equals(lastUpdatedBoard);
    }

    protected void onUpdate() {
        lastUpdatedBoard = cloneBoard(executedCanvas);
    }

    protected boolean isExecuted() {
        return (executedCanvas != null);
    }

    private ArrayList<ArrayList<Character>> cloneBoard(Canvas canvas) {
        ArrayList<ArrayList<Character>> board = new ArrayList<>();

        for (int y = 0; y < canvas.getHeight(); ++y) {
            board.add(new ArrayList<>());
            for (int x = 0; x < canvas.getWidth(); ++x) {
                board.get(y).add(canvas.getPixel(x, y));
            }
        }

        return board;
    }
}
