//----------------------Canvas.java----------------------

package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public class Canvas {
    private static final char MINIMUM_ASCII_RANGE = 32;
    private static final char MAXIMUM_ASCII_RANGE = 126;

    private final ArrayList<ArrayList<Character>> pixels;
    private final int width;
    private final int height;

    public Canvas(final int width, final int height) {
        this.width = width;
        this.height = height;
        pixels = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            pixels.add(new ArrayList<>());
            for (int x = 0; x < width; x++) {
                pixels.get(y).add(' ');
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void drawPixel(final int x, final int y, final char character) {
        pixels.get(y).set(x, character);
        assert (MINIMUM_ASCII_RANGE <= pixels.get(y).get(x) &&
                pixels.get(y).get(x) <= MAXIMUM_ASCII_RANGE) : "Invalid ASCII Code Range";
    }

    public char getPixel(final int x, final int y) {
        return pixels.get(y).get(x);
    }

    public boolean increasePixel(final int x, final int y) {
        if (pixels.get(y).get(x) < MAXIMUM_ASCII_RANGE) {
            drawPixel(x, y, (char) (getPixel(x, y) + 1));
            return true;
        }
        return false;
    }

    public boolean decreasePixel(final int x, final int y) {
        if (pixels.get(y).get(x) > MINIMUM_ASCII_RANGE) {
            drawPixel(x, y, (char) (getPixel(x, y) - 1));
            return true;
        }
        return false;
    }

    public void toUpper(final int x, final int y) {
        if (97 <= getPixel(x, y) && getPixel(x, y) <= 122) {
            drawPixel(x, y, (char) (getPixel(x, y) - 32));
        }
    }

    public void toLower(final int x, final int y) {
        if (65 <= getPixel(x, y) && getPixel(x, y) <= 90) {
            drawPixel(x, y, (char) (getPixel(x, y) + 32));
        }
    }

    public void fillHorizontalLine(final int y, final char character) {
        for (int x = 0; x < width; x++) {
            drawPixel(x, y, character);
        }
    }

    public void fillVerticalLine(final int x, final char character) {
        for (int y = 0; y < height; y++) {
            drawPixel(x, y, character);
        }
    }

    public void clear() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                drawPixel(x, y, ' ');
            }
        }
    }

    public String getDrawing() {
        final StringBuilder sb = new StringBuilder();

        addHorizontalBorder(sb);
        for (int y = 0; y < height; y++) {
            sb.append('|');
            for (int x = 0; x < width; x++) {
                sb.append(getPixel(x, y));
            }
            sb.append('|');
            sb.append(System.lineSeparator());
        }
        addHorizontalBorder(sb);

        return sb.toString();
    }

    private void addHorizontalBorder(final StringBuilder sb) {
        sb.append('+');
        for (int i = 0; i < width; ++i) {
            sb.append('-');
        }
        sb.append('+');
        sb.append(System.lineSeparator());
    }
}


//----------------------OverdrawAnalyzer.java----------------------

package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;
import java.util.LinkedList;

public class OverdrawAnalyzer extends Canvas {
    private final ArrayList<ArrayList<LinkedList<Character>>> pixelsHistory;

    public OverdrawAnalyzer(final int width, final int height) {
        super(width, height);
        pixelsHistory = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            pixelsHistory.add(new ArrayList<>());
            for (int x = 0; x < width; x++) {
                pixelsHistory.get(y).add(new LinkedList<>());
            }
        }
    }

    @Override
    public void drawPixel(final int x, final int y, final char character) {
        if (getPixel(x, y) == character) {
            return;
        }
        super.drawPixel(x, y, character);
        pixelsHistory.get(y).get(x).add(character);
    }

    public LinkedList<Character> getPixelHistory(final int x, final int y) {
        return pixelsHistory.get(y).get(x);
    }

    public int getOverdrawCount(final int x, final int y) {
        return pixelsHistory.get(y).get(x).size();
    }

    public int getOverdrawCount() {
        int overdrawCount = 0;
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                overdrawCount += pixelsHistory.get(y).get(x).size();
            }
        }
        return overdrawCount;
    }
}


//----------------------CommandHistoryManager.java----------------------

package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public class CommandHistoryManager {
    private final Canvas canvas;
    private final ArrayList<ICommand> canUndoCommands = new ArrayList<>();
    private final ArrayList<ICommand> canRedoCommands = new ArrayList<>();

    public CommandHistoryManager(final Canvas canvas) {
        this.canvas = canvas;
    }

    public boolean execute(ICommand command) {
        if (command.execute(canvas)) {
            canUndoCommands.add(command);
            canRedoCommands.clear();
            return true;
        }
        return false;
    }

    public boolean canUndo() {
        return !canUndoCommands.isEmpty();
    }

    public boolean canRedo() {
        return !canRedoCommands.isEmpty();
    }

    public boolean undo() {
        if (!canUndo()) {
            return false;
        }
        ICommand command = canUndoCommands.get(canUndoCommands.size() - 1);
        if (command.undo()) {
            canRedoCommands.add(command);
            canUndoCommands.remove(canUndoCommands.size() - 1);
            return true;
        }
        return false;
    }

    public boolean redo() {
        if (!canRedo()) {
            return false;
        }
        ICommand command = canRedoCommands.get(canRedoCommands.size() - 1);
        if (command.redo()) {
            canUndoCommands.add(command);
            canRedoCommands.remove(canRedoCommands.size() - 1);
            return true;
        }
        return false;
    }
}


//----------------------ICommand.java----------------------

package academy.pocu.comp2500.assignment4;

public interface ICommand {
    boolean execute(Canvas canvas);
    boolean undo();
    boolean redo();
}


//----------------------BaseCommand.java----------------------

package academy.pocu.comp2500.assignment4;

public abstract class BaseCommand implements ICommand {
    protected int cmdX;
    protected int cmdY;
    protected boolean canUndo;
    protected boolean canRedo;
    protected char anteSavedAscii;
    protected char postSavedAscii;
    protected Canvas canvas;

    public abstract boolean execute(Canvas canvas);

    public boolean undo() {
        if (!canUndo || canvas.getPixel(cmdX, cmdY) != postSavedAscii) {
            return false;
        }

        canvas.drawPixel(cmdX, cmdY, anteSavedAscii);
        canUndo = false;
        return canRedo = true;
    }

    public boolean redo() {
        if (!canRedo || canvas.getPixel(cmdX, cmdY) != anteSavedAscii) {
            return false;
        }

        canvas.drawPixel(cmdX, cmdY, postSavedAscii);
        canRedo = false;
        return canUndo = true;
    }

    protected boolean isAlreadyExecuted() {
        return this.canvas != null;
    }

    protected boolean isValidRangeOfX(final Canvas canvas, final int x) {
        return 0 <= x && x < canvas.getWidth();
    }

    protected boolean isValidRangeOfY(final Canvas canvas, final int y) {
        return 0 <= y && y < canvas.getHeight();
    }
}


//----------------------DrawPixelCommand.java----------------------

package academy.pocu.comp2500.assignment4;

public class DrawPixelCommand extends BaseCommand {
    public DrawPixelCommand(final int x, final int y, final char character) {
        this.cmdX = x;
        this.cmdY = y;
        this.postSavedAscii = character;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (isAlreadyExecuted() || !isValidRangeOfX(canvas, cmdX) || !isValidRangeOfY(canvas, cmdY)) {
            return false;
        }

        this.canvas = canvas;
        anteSavedAscii = canvas.getPixel(cmdX, cmdY);
        canvas.drawPixel(cmdX, cmdY, postSavedAscii);
        return canUndo = true;
    }
}


//----------------------IncreasePixelCommand.java----------------------

package academy.pocu.comp2500.assignment4;

public class IncreasePixelCommand extends BaseCommand {
    public IncreasePixelCommand(final int x, final int y) {
        this.cmdX = x;
        this.cmdY = y;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (isAlreadyExecuted() || !isValidRangeOfX(canvas, cmdX) || !isValidRangeOfY(canvas, cmdY)) {
            return false;
        }

        this.canvas = canvas;
        anteSavedAscii = canvas.getPixel(cmdX, cmdY);
        if (canvas.increasePixel(cmdX, cmdY)) {
            postSavedAscii = (char) (anteSavedAscii + 1);
            return canUndo = true;
        }
        return false;
    }
}


//----------------------DecreasePixelCommand.java----------------------

package academy.pocu.comp2500.assignment4;

public class DecreasePixelCommand extends BaseCommand {
    public DecreasePixelCommand(final int x, final int y) {
        this.cmdX = x;
        this.cmdY = y;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (isAlreadyExecuted() || !isValidRangeOfX(canvas, cmdX) || !isValidRangeOfY(canvas, cmdY)) {
            return false;
        }

        this.canvas = canvas;
        anteSavedAscii = canvas.getPixel(cmdX, cmdY);
        if (canvas.decreasePixel(cmdX, cmdY)) {
            postSavedAscii = (char) (anteSavedAscii - 1);
            return canUndo = true;
        }
        return false;
    }
}


//----------------------ToUppercaseCommand.java----------------------

package academy.pocu.comp2500.assignment4;

public class ToUppercaseCommand extends BaseCommand {
    public ToUppercaseCommand(final int x, final int y) {
        this.cmdX = x;
        this.cmdY = y;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (isAlreadyExecuted() || !isValidRangeOfX(canvas, cmdX) || !isValidRangeOfY(canvas, cmdY)) {
            return false;
        }

        this.canvas = canvas;
        anteSavedAscii = canvas.getPixel(cmdX, cmdY);
        canvas.toUpper(cmdX, cmdY);
        postSavedAscii = canvas.getPixel(cmdX, cmdY);
        return canUndo = true;
    }
}


//----------------------ToLowercaseCommand.java----------------------

package academy.pocu.comp2500.assignment4;

public class ToLowercaseCommand extends BaseCommand {
    public ToLowercaseCommand(final int x, final int y) {
        this.cmdX = x;
        this.cmdY = y;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (isAlreadyExecuted() || !isValidRangeOfX(canvas, cmdX) || !isValidRangeOfY(canvas, cmdY)) {
            return false;
        }

        this.canvas = canvas;
        anteSavedAscii = canvas.getPixel(cmdX, cmdY);
        canvas.toLower(cmdX, cmdY);
        postSavedAscii = canvas.getPixel(cmdX, cmdY);
        return canUndo = true;
    }
}
