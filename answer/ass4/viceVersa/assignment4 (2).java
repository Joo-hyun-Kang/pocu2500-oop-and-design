//----------------------FillHorizontalLineCommand.java----------------------

package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public class FillHorizontalLineCommand extends BaseCommand {
    private final ArrayList<Character> anteSavedAsciis;

    public FillHorizontalLineCommand(final int y, final char character) {
        this.cmdY = y;
        this.anteSavedAsciis = new ArrayList<>();
        this.postSavedAscii = character;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (isAlreadyExecuted() || !isValidRangeOfY(canvas, cmdY)) {
            return false;
        }

        this.canvas = canvas;
        for (int x = 0; x < canvas.getWidth(); x++) {
            anteSavedAsciis.add(canvas.getPixel(x, cmdY));
            canvas.drawPixel(x, cmdY, postSavedAscii);
        }
        return canUndo = true;
    }

    @Override
    public boolean undo() {
        if (!canUndo) {
            return false;
        }
        for (int x = 0; x < canvas.getWidth(); x++) {
            if (canvas.getPixel(x, cmdY) != postSavedAscii) {
                return false;
            }
        }

        for (int x = 0; x < canvas.getWidth(); x++) {
            canvas.drawPixel(x, cmdY, anteSavedAsciis.get(x));
        }
        canUndo = false;
        return canRedo = true;
    }

    @Override
    public boolean redo() {
        if (!canRedo) {
            return false;
        }
        for (int x = 0; x < canvas.getWidth(); x++) {
            if (canvas.getPixel(x, cmdY) != anteSavedAsciis.get(x)) {
                return false;
            }
        }

        for (int x = 0; x < canvas.getWidth(); x++) {
            canvas.drawPixel(x, cmdY, postSavedAscii);
        }
        canRedo = false;
        return canUndo = true;
    }
}


//----------------------FillVerticalLineCommand.java----------------------

package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public class FillVerticalLineCommand extends BaseCommand {
    private final ArrayList<Character> anteSavedAsciis;

    public FillVerticalLineCommand(final int x, final char character) {
        this.cmdX = x;
        this.anteSavedAsciis = new ArrayList<>();
        this.postSavedAscii = character;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (isAlreadyExecuted() || !isValidRangeOfX(canvas, cmdX)) {
            return false;
        }

        this.canvas = canvas;
        for (int y = 0; y < canvas.getHeight(); y++) {
            anteSavedAsciis.add(canvas.getPixel(cmdX, y));
            canvas.drawPixel(cmdX, y, postSavedAscii);
        }
        return canUndo = true;
    }

    @Override
    public boolean undo() {
        if (!canUndo) {
            return false;
        }
        for (int y = 0; y < canvas.getHeight(); y++) {
            if (canvas.getPixel(cmdX, y) != postSavedAscii) {
                return false;
            }
        }

        for (int y = 0; y < canvas.getHeight(); y++) {
            canvas.drawPixel(cmdX, y, anteSavedAsciis.get(y));
        }
        canUndo = false;
        return canRedo = true;
    }

    @Override
    public boolean redo() {
        if (!canRedo) {
            return false;
        }
        for (int y = 0; y < canvas.getHeight(); y++) {
            if (canvas.getPixel(cmdX, y) != anteSavedAsciis.get(y)) {
                return false;
            }
        }

        for (int y = 0; y < canvas.getHeight(); y++) {
            canvas.drawPixel(cmdX, y, postSavedAscii);
        }
        canRedo = false;
        return canUndo = true;
    }
}


//----------------------ClearCommand.java----------------------

package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public class ClearCommand extends BaseCommand {
    private ArrayList<ArrayList<Character>> anteSavedAsciis;
    private final char postSavedAscii;

    public ClearCommand() {
        this.postSavedAscii = ' ';
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (isAlreadyExecuted()) {
            return false;
        }

        this.canvas = canvas;
        anteSavedAsciis = new ArrayList<>();
        for (int y = 0; y < canvas.getHeight(); y++) {
            anteSavedAsciis.add(new ArrayList<>());
            for (int x = 0; x < canvas.getWidth(); x++) {
                anteSavedAsciis.get(y).add(canvas.getPixel(x, y));
                canvas.drawPixel(x, y, postSavedAscii);
            }
        }
        return canUndo = true;
    }

    @Override
    public boolean undo() {
        if (!canUndo) {
            return false;
        }
        for (int y = 0; y < canvas.getHeight(); y++) {
            for (int x = 0; x < canvas.getWidth(); x++) {
                if (canvas.getPixel(x, y) != postSavedAscii) {
                    return false;
                }
            }
        }

        for (int y = 0; y < canvas.getHeight(); y++) {
            for (int x = 0; x < canvas.getWidth(); x++) {
                canvas.drawPixel(x, y, anteSavedAsciis.get(y).get(x));
            }
        }
        canUndo = false;
        return canRedo = true;
    }

    @Override
    public boolean redo() {
        if (!canRedo) {
            return false;
        }
        for (int y = 0; y < canvas.getHeight(); y++) {
            for (int x = 0; x < canvas.getWidth(); x++) {
                if (canvas.getPixel(x, y) != anteSavedAsciis.get(y).get(x)) {
                    return false;
                }
            }
        }

        for (int y = 0; y < canvas.getHeight(); y++) {
            for (int x = 0; x < canvas.getWidth(); x++) {
                canvas.drawPixel(x, y, postSavedAscii);
            }
        }
        canRedo = false;
        return canUndo = true;
    }
}
