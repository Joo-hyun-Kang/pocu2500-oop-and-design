//----------------------Canvas.java----------------------

package academy.pocu.comp2500.assignment4;

import java.util.Arrays;

public class Canvas {

    private static final char MIN_CHARACTER = 32;
    private static final char MAX_CHARACTER = 126;
    private static final char ASCII_UPPER_A = 65;
    private static final char ASCII_UPPER_Z = 90;
    private static final char ASCII_LOWER_A = 97;
    private static final char ASCII_LOWER_Z = 122;
    private static final char TOP_BOTTOM_LINE = '-';
    private static final char SIDE_LINE = '|';
    private static final char EDGE = '+';
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private final char[][] image;

    public Canvas(final int width, final int height) {
        assert (width < 1 || height < 1) : "width and height must be greater than 0";

        this.image = new char[height][width];
        fillMinCharacter();
    }

    public int getWidth() {
        return this.image[0].length;
    }

    public int getHeight() {
        return this.image.length;
    }

    public void drawPixel(final int x, final int y, final char character) {
        this.image[y][x] = character;
    }

    public char getPixel(final int x, final int y) {
        return this.image[y][x];
    }

    public boolean increasePixel(final int x, final int y) {
        if (this.image[y][x] < MAX_CHARACTER) {
            ++this.image[y][x];
            return true;
        }
        return false;
    }

    public boolean decreasePixel(final int x, final int y) {
        if (MIN_CHARACTER < this.image[y][x]) {
            --this.image[y][x];
            return true;
        }
        return false;
    }

    public void toUpper(final int x, final int y) {
        char target = this.image[y][x];
        if (ASCII_LOWER_A <= target && target <= ASCII_LOWER_Z) {
            this.image[y][x] = (char) (target ^ MIN_CHARACTER);
        }
    }

    public void toLower(final int x, final int y) {
        char target = this.image[y][x];
        if (ASCII_UPPER_A <= target && target <= ASCII_UPPER_Z) {
            this.image[y][x] = (char) (target ^ MIN_CHARACTER);
        }
    }

    public void fillHorizontalLine(final int y, final char character) {
        Arrays.fill(this.image[y], character);
    }

    public void fillVerticalLine(final int x, final char character) {
        for (int i = 0; i < this.image.length; ++i) {
            this.image[i][x] = character;
        }
    }

    public void clear() {
        fillMinCharacter();
    }

    public String getDrawing() {
        StringBuilder stringBuilder = new StringBuilder();
        drawOutline(stringBuilder);
        for (char[] chars : this.image) {
            stringBuilder.append(SIDE_LINE);
            for (int i = 0; i < chars.length; ++i) {
                stringBuilder.append(chars[i]);
            }
            stringBuilder.append(SIDE_LINE);
            stringBuilder.append(LINE_SEPARATOR);
        }
        drawOutline(stringBuilder);
        return stringBuilder.toString();
    }

    private void fillMinCharacter() {
        for (char[] chars : this.image) {
            Arrays.fill(chars, MIN_CHARACTER);
        }
    }

    private void drawOutline(final StringBuilder stringBuilder) {
        stringBuilder.append(EDGE);
        stringBuilder.append(String.valueOf(TOP_BOTTOM_LINE).repeat(this.image[0].length));
        stringBuilder.append(EDGE);
        stringBuilder.append(LINE_SEPARATOR);
    }
}


//----------------------CommandClear.java----------------------

package academy.pocu.comp2500.assignment4;

public class CommandClear implements ICommand {

    private Canvas canvasOrNull;
    private String prevDrawingOrNull;
    private String postDrawingOrNull;
    private boolean undone;

    public CommandClear() {
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (this.canvasOrNull == null) {
            this.canvasOrNull = canvas;
            this.prevDrawingOrNull = this.canvasOrNull.getDrawing();
            this.canvasOrNull.clear();
            this.postDrawingOrNull = this.canvasOrNull.getDrawing();
            return true;
        }
        return false;
    }

    @Override
    public boolean undo() {
        if (this.canvasOrNull != null) {
            if (!this.undone && this.canvasOrNull.getDrawing().equals(this.postDrawingOrNull)) {
                rollback();
                this.undone = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean redo() {
        if (this.canvasOrNull != null) {
            if (this.undone && this.canvasOrNull.getDrawing().equals(this.prevDrawingOrNull)) {
                this.canvasOrNull.clear();
                this.undone = false;
                return true;
            }
        }
        return false;
    }

    private void rollback() {
        String[] lines = this.prevDrawingOrNull.split(System.lineSeparator());
        for (int i = 1; i < lines.length - 1; ++i) {
            for (int j = 1; j < lines[i].length() - 1; ++j) {
                this.canvasOrNull.drawPixel(j - 1, i - 1, lines[i].charAt(j));
            }
        }
    }
}


//----------------------CommandDecrease.java----------------------

package academy.pocu.comp2500.assignment4;

public class CommandDecrease implements ICommand {

    private final int x;
    private final int y;
    private Canvas canvasOrNull;
    private String prevDrawingOrNull;
    private String postDrawingOrNull;
    private boolean undone;

    public CommandDecrease(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (this.canvasOrNull == null) {
            if (invalidIndex(canvas.getWidth(), this.x) || invalidIndex(canvas.getHeight(), this.y)) {
                return false;
            }

            this.prevDrawingOrNull = canvas.getDrawing();
            if (!canvas.decreasePixel(this.x, this.y)) {
                return false;
            }

            this.canvasOrNull = canvas;
            this.postDrawingOrNull = this.canvasOrNull.getDrawing();
            return true;
        }
        return false;
    }

    @Override
    public boolean undo() {
        if (this.canvasOrNull != null) {
            if (!this.undone && this.canvasOrNull.getDrawing().equals(this.postDrawingOrNull)) {
                this.canvasOrNull.increasePixel(this.x, this.y);
                this.undone = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean redo() {
        if (this.canvasOrNull != null) {
            if (this.undone && this.canvasOrNull.getDrawing().equals(this.prevDrawingOrNull)) {
                this.canvasOrNull.decreasePixel(this.x, this.y);
                this.undone = false;
                return true;
            }
        }
        return false;
    }

    private boolean invalidIndex(final int size, final int point) {
        return point < 0 || size <= point;
    }
}


//----------------------CommandDrawCharacter.java----------------------

package academy.pocu.comp2500.assignment4;

public class CommandDrawCharacter implements ICommand {

    private final int x;
    private final int y;
    private final char character;
    private char prevCharacter;
    private Canvas canvasOrNull;
    private String prevDrawingOrNull;
    private String postDrawingOrNull;
    private boolean undone;

    public CommandDrawCharacter(final int x, final int y, final char character) {
        this.x = x;
        this.y = y;
        this.character = character;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (this.canvasOrNull == null) {
            if (invalidIndex(canvas.getWidth(), this.x) || invalidIndex(canvas.getHeight(), this.y)) {
                return false;
            }

            this.canvasOrNull = canvas;
            this.prevDrawingOrNull = this.canvasOrNull.getDrawing();
            this.prevCharacter = this.canvasOrNull.getPixel(this.x, this.y);

            this.canvasOrNull.drawPixel(this.x, this.y, this.character);
            this.postDrawingOrNull = this.canvasOrNull.getDrawing();
            return true;
        }
        return false;
    }

    @Override
    public boolean undo() {
        if (this.canvasOrNull != null) {
            if (!this.undone && this.canvasOrNull.getDrawing().equals(this.postDrawingOrNull)) {
                this.canvasOrNull.drawPixel(this.x, this.y, this.prevCharacter);
                this.undone = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean redo() {
        if (this.canvasOrNull != null) {
            if (this.undone && this.canvasOrNull.getDrawing().equals(this.prevDrawingOrNull)) {
                this.canvasOrNull.drawPixel(this.x, this.y, this.character);
                this.undone = false;
                return true;
            }
        }
        return false;
    }

    private boolean invalidIndex(final int size, final int point) {
        return point < 0 || size <= point;
    }
}


//----------------------CommandFillHorizontalLine.java----------------------

package academy.pocu.comp2500.assignment4;

public class CommandFillHorizontalLine implements ICommand {

    private final int y;
    private final char character;
    private char[] prevHorizontalLineOrNull;
    private Canvas canvasOrNull;
    private String prevDrawingOrNull;
    private String postDrawingOrNull;
    private boolean undone;

    public CommandFillHorizontalLine(final int y, final char character) {
        this.y = y;
        this.character = character;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (this.canvasOrNull == null) {
            if (invalidIndex(canvas.getHeight(), this.y)) {
                return false;
            }

            this.canvasOrNull = canvas;
            this.prevDrawingOrNull = this.canvasOrNull.getDrawing();

            this.prevHorizontalLineOrNull = new char[this.canvasOrNull.getWidth()];
            for (int i = 0; i < this.prevHorizontalLineOrNull.length; ++i) {
                this.prevHorizontalLineOrNull[i] = this.canvasOrNull.getPixel(i, this.y);
            }

            this.canvasOrNull.fillHorizontalLine(this.y, this.character);
            this.postDrawingOrNull = this.canvasOrNull.getDrawing();
            return true;
        }
        return false;
    }

    @Override
    public boolean undo() {
        if (this.canvasOrNull != null) {
            if (!this.undone && this.canvasOrNull.getDrawing().equals(this.postDrawingOrNull)) {
                for (int i = 0; i < this.prevHorizontalLineOrNull.length; ++i) {
                    this.canvasOrNull.drawPixel(i, this.y, this.prevHorizontalLineOrNull[i]);
                }
                this.undone = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean redo() {
        if (this.canvasOrNull != null) {
            if (this.undone && this.canvasOrNull.getDrawing().equals(this.prevDrawingOrNull)) {
                this.canvasOrNull.fillHorizontalLine(this.y, this.character);
                this.undone = false;
                return true;
            }
        }
        return false;
    }

    private boolean invalidIndex(final int size, final int point) {
        return point < 0 || size <= point;
    }
}


//----------------------CommandFillVerticalLine.java----------------------

package academy.pocu.comp2500.assignment4;

public class CommandFillVerticalLine implements ICommand {

    private final int x;
    private final char character;
    private char[] prevVerticalLineOrNull;
    private Canvas canvasOrNull;
    private String prevDrawingOrNull;
    private String postDrawingOrNull;
    private boolean undone;

    public CommandFillVerticalLine(final int x, final char character) {
        this.x = x;
        this.character = character;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (this.canvasOrNull == null) {
            if (invalidIndex(canvas.getWidth(), this.x)) {
                return false;
            }

            this.canvasOrNull = canvas;
            this.prevDrawingOrNull = this.canvasOrNull.getDrawing();

            this.prevVerticalLineOrNull = new char[this.canvasOrNull.getHeight()];
            for (int i = 0; i < this.prevVerticalLineOrNull.length; ++i) {
                this.prevVerticalLineOrNull[i] = this.canvasOrNull.getPixel(this.x, i);
            }

            this.canvasOrNull.fillVerticalLine(this.x, this.character);
            this.postDrawingOrNull = this.canvasOrNull.getDrawing();
            return true;
        }
        return false;
    }

    @Override
    public boolean undo() {
        if (this.canvasOrNull != null) {
            if (!this.undone && this.canvasOrNull.getDrawing().equals(this.postDrawingOrNull)) {
                for (int i = 0; i < this.prevVerticalLineOrNull.length; ++i) {
                    this.canvasOrNull.drawPixel(this.x, i, this.prevVerticalLineOrNull[i]);
                }
                this.undone = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean redo() {
        if (this.canvasOrNull != null) {
            if (this.undone && this.canvasOrNull.getDrawing().equals(this.prevDrawingOrNull)) {
                this.canvasOrNull.fillVerticalLine(this.x, this.character);
                this.undone = false;
                return true;
            }
        }
        return false;
    }

    private boolean invalidIndex(final int size, final int point) {
        return point < 0 || size <= point;
    }
}


//----------------------CommandHistoryManager.java----------------------

package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public class CommandHistoryManager {

    private static final int COMMAND_SIZE = 4096;
    private final Canvas canvas;
    private final ArrayList<ICommand> undoCommands;
    private final ArrayList<ICommand> redoCommands;

    public CommandHistoryManager(Canvas canvas) {
        this.canvas = canvas;
        this.undoCommands = new ArrayList<>(COMMAND_SIZE);
        this.redoCommands = new ArrayList<>(COMMAND_SIZE);
    }

    public boolean execute(ICommand command) {
        if (command.execute(this.canvas)) {
            this.undoCommands.add(command);
            this.redoCommands.clear();
            return true;
        }
        return false;
    }

    public boolean canUndo() {
        return this.undoCommands.size() > 0;
    }

    public boolean canRedo() {
        return this.redoCommands.size() > 0;
    }

    public boolean undo() {
        if (canUndo()) {
            ICommand command = this.undoCommands.get(this.undoCommands.size() - 1);
            if (command.undo()) {
                this.undoCommands.remove(command);
                this.redoCommands.add(command);
                return true;
            }
        }
        return false;
    }

    public boolean redo() {
        if (canRedo()) {
            ICommand command = this.redoCommands.get(this.redoCommands.size() - 1);
            if (command.redo()) {
                this.redoCommands.remove(command);
                this.undoCommands.add(command);
                return true;
            }
        }
        return false;
    }
}


//----------------------CommandIncrease.java----------------------

package academy.pocu.comp2500.assignment4;

public class CommandIncrease implements ICommand {

    private final int x;
    private final int y;
    private Canvas canvasOrNull;
    private String prevDrawingOrNull;
    private String postDrawingOrNull;
    private boolean undone;

    public CommandIncrease(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (this.canvasOrNull == null) {
            if (invalidIndex(canvas.getWidth(), this.x) || invalidIndex(canvas.getHeight(), this.y)) {
                return false;
            }

            this.prevDrawingOrNull = canvas.getDrawing();
            if (!canvas.increasePixel(this.x, this.y)) {
                return false;
            }

            this.canvasOrNull = canvas;
            this.postDrawingOrNull = this.canvasOrNull.getDrawing();
            return true;
        }
        return false;
    }

    @Override
    public boolean undo() {
        if (this.canvasOrNull != null) {
            if (!this.undone && this.canvasOrNull.getDrawing().equals(this.postDrawingOrNull)) {
                this.canvasOrNull.decreasePixel(this.x, this.y);
                this.undone = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean redo() {
        if (this.canvasOrNull != null) {
            if (this.undone && this.canvasOrNull.getDrawing().equals(this.prevDrawingOrNull)) {
                this.canvasOrNull.increasePixel(this.x, this.y);
                this.undone = false;
                return true;
            }
        }
        return false;
    }

    private boolean invalidIndex(final int size, final int point) {
        return point < 0 || size <= point;
    }
}


//----------------------CommandToLowerCase.java----------------------

package academy.pocu.comp2500.assignment4;

public class CommandToLowerCase implements ICommand {

    private static final char ASCII_UPPER_A = 65;
    private static final char ASCII_UPPER_Z = 90;
    private final int x;
    private final int y;
    private Canvas canvasOrNull;
    private String prevDrawingOrNull;
    private String postDrawingOrNull;
    private char prevCharacter;
    private boolean undone;

    public CommandToLowerCase(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (this.canvasOrNull == null) {
            if (invalidIndex(canvas.getWidth(), this.x) || invalidIndex(canvas.getHeight(), this.y)) {
                return false;
            }

            this.canvasOrNull = canvas;
            this.prevDrawingOrNull = this.canvasOrNull.getDrawing();
            this.prevCharacter = this.canvasOrNull.getPixel(this.x, this.y);
            this.canvasOrNull.toLower(this.x, this.y);
            this.postDrawingOrNull = this.canvasOrNull.getDrawing();
            return true;
        }
        return false;
    }

    @Override
    public boolean undo() {
        if (this.canvasOrNull != null) {
            if (!this.undone && this.canvasOrNull.getDrawing().equals(this.postDrawingOrNull)) {
                if (ASCII_UPPER_A <= this.prevCharacter && this.prevCharacter <= ASCII_UPPER_Z) {
                    this.canvasOrNull.toUpper(this.x, this.y);
                }
                this.undone = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean redo() {
        if (this.canvasOrNull != null) {
            if (this.undone && this.canvasOrNull.getDrawing().equals(this.prevDrawingOrNull)) {
                this.canvasOrNull.toLower(this.x, this.y);
                this.undone = false;
                return true;
            }
        }
        return false;
    }

    private boolean invalidIndex(final int size, final int point) {
        return point < 0 || size <= point;
    }
}


//----------------------CommandToUpperCase.java----------------------

package academy.pocu.comp2500.assignment4;

public class CommandToUpperCase implements ICommand {

    private static final char ASCII_LOWER_A = 97;
    private static final char ASCII_LOWER_Z = 122;
    private final int x;
    private final int y;
    private Canvas canvasOrNull;
    private String prevDrawingOrNull;
    private String postDrawingOrNull;
    private char prevCharacter;
    private boolean undone;

    public CommandToUpperCase(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (this.canvasOrNull == null) {
            if (invalidIndex(canvas.getWidth(), this.x) || invalidIndex(canvas.getHeight(), this.y)) {
                return false;
            }

            this.canvasOrNull = canvas;
            this.prevCharacter = this.canvasOrNull.getPixel(this.x, this.y);
            this.prevDrawingOrNull = this.canvasOrNull.getDrawing();
            this.canvasOrNull.toUpper(this.x, this.y);
            this.postDrawingOrNull = this.canvasOrNull.getDrawing();
            return true;
        }
        return false;
    }

    @Override
    public boolean undo() {
        if (this.canvasOrNull != null) {
            if (!this.undone && this.canvasOrNull.getDrawing().equals(this.postDrawingOrNull)) {
                if (ASCII_LOWER_A <= this.prevCharacter && this.prevCharacter <= ASCII_LOWER_Z) {
                    this.canvasOrNull.toLower(this.x, this.y);
                }
                this.undone = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean redo() {
        if (this.canvasOrNull != null) {
            if (this.undone && this.canvasOrNull.getDrawing().equals(this.prevDrawingOrNull)) {
                this.canvasOrNull.toUpper(this.x, this.y);
                this.undone = false;
                return true;
            }
        }
        return false;
    }

    private boolean invalidIndex(final int size, final int point) {
        return point < 0 || size <= point;
    }
}


//----------------------ICommand.java----------------------

package academy.pocu.comp2500.assignment4;

public interface ICommand {

    boolean execute(Canvas canvas);

    boolean undo();

    boolean redo();
}


//----------------------OverdrawAnalyzer.java----------------------

package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;
import java.util.LinkedList;

public class OverdrawAnalyzer extends Canvas {

    private static final int CANVAS_SIZE = 4096;
    private static final char MIN_CHARACTER = 32;
    private static final char MAX_CHARACTER = 126;
    private static final char ASCII_UPPER_A = 65;
    private static final char ASCII_UPPER_Z = 90;
    private static final char ASCII_LOWER_A = 97;
    private static final char ASCII_LOWER_Z = 122;
    private final ArrayList<ArrayList<LinkedList<Character>>> pixelHistory;
    private final int[][] overdrawCounts;

    public OverdrawAnalyzer(int width, int height) {
        super(width, height);
        this.pixelHistory = new ArrayList<>(CANVAS_SIZE);
        this.overdrawCounts = new int[height][width];

        for (int i = 0; i < height; ++i) {
            ArrayList<LinkedList<Character>> temp = new ArrayList<>(CANVAS_SIZE);
            for (int j = 0; j < width; ++j) {
                temp.add(new LinkedList<>());
            }
            this.pixelHistory.add(temp);
        }
    }

    public LinkedList<Character> getPixelHistory(final int x, final int y) {
        return this.pixelHistory.get(y).get(x);
    }

    public int getOverdrawCount() {
        int totalCount = 0;
        for (int[] horizontal : this.overdrawCounts) {
            for (int count : horizontal) {
                totalCount += count;
            }
        }
        return totalCount;
    }

    public int getOverdrawCount(int x, int y) {
        return this.overdrawCounts[y][x];
    }

    @Override
    public void drawPixel(final int x, final int y, final char character) {
        if (super.getPixel(x, y) != character) {
            super.drawPixel(x, y, character);
            this.pixelHistory.get(y).get(x).add(character);
            ++this.overdrawCounts[y][x];
        }
    }

    @Override
    public boolean increasePixel(final int x, final int y) {
        final char target = super.getPixel(x, y);
        if (target < MAX_CHARACTER) {
            super.increasePixel(x, y);
            this.pixelHistory.get(y).get(x).add(super.getPixel(x, y));
            ++overdrawCounts[y][x];
            return true;
        }
        return false;
    }

    @Override
    public boolean decreasePixel(final int x, final int y) {
        final char target = super.getPixel(x, y);
        if (MIN_CHARACTER < target) {
            super.decreasePixel(x, y);
            this.pixelHistory.get(y).get(x).add(super.getPixel(x, y));
            ++overdrawCounts[y][x];
            return true;
        }
        return false;
    }

    @Override
    public void toUpper(final int x, final int y) {
        final char target = super.getPixel(x, y);
        if (ASCII_LOWER_A <= target && target <= ASCII_LOWER_Z) {
            super.toUpper(x, y);
            this.pixelHistory.get(y).get(x).add(super.getPixel(x, y));
            ++overdrawCounts[y][x];
        }
    }

    @Override
    public void toLower(final int x, final int y) {
        final char target = super.getPixel(x, y);
        if (ASCII_UPPER_A <= target && target <= ASCII_UPPER_Z) {
            super.toLower(x, y);
            this.pixelHistory.get(y).get(x).add(super.getPixel(x, y));
            ++overdrawCounts[y][x];
        }
    }

    @Override
    public void fillHorizontalLine(final int y, final char character) {
        for (int i = 0; i < super.getWidth(); ++i) {
            if (super.getPixel(i, y) != character) {
                super.drawPixel(i, y, character);
                this.pixelHistory.get(y).get(i).add(character);
                ++this.overdrawCounts[y][i];
            }
        }
    }

    @Override
    public void fillVerticalLine(final int x, final char character) {
        for (int i = 0; i < super.getHeight(); ++i) {
            if (super.getPixel(x, i) != character) {
                super.drawPixel(x, i, character);
                this.pixelHistory.get(i).get(x).add(character);
                ++this.overdrawCounts[i][x];
            }
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < super.getHeight(); ++i) {
            for (int j = 0; j < super.getWidth(); ++j) {
                if (super.getPixel(j, i) != MIN_CHARACTER) {
                    super.drawPixel(j, i, MIN_CHARACTER);
                    this.pixelHistory.get(i).get(j).add(MIN_CHARACTER);
                    ++this.overdrawCounts[i][j];
                }
            }
        }
    }
}
