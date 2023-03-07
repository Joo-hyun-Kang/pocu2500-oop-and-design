// Coordinate.java
package academy.pocu.comp2500.assignment4;

import java.util.Objects;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Coordinate)) {
            return false;
        }

        Coordinate other = (Coordinate) obj;

        return getX() == other.getX() && getY() == other.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}

// DecreasePixel.java
package academy.pocu.comp2500.assignment4;

public class DecreasePixel extends ChangeOnePixel {

    public DecreasePixel(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (!super.execute(canvas)) {
            return false;
        }

        if (!canvas.decreasePixel(getX(), getY())) {
            return false;
        }

        finallyExecute();

        return isExecuted();
    }

    @Override
    public boolean redo() {
        if (!super.redo()) {
            return false;
        }

        getCanvas().decreasePixel(getX(), getY());

        finallyRedo();

        return true;
    }
}

// DrawPixel.java
package academy.pocu.comp2500.assignment4;

public class DrawPixel extends ChangeOnePixel {

    public DrawPixel(int x, int y, char character) {
        super(x, y);
        setNewCharacter(character);
    }

    @Override
    public String getCommandInfo() {
        return String.format("%s(%d, %d, %c)", getClass().getSimpleName(), getX(), getY(), getNewCharacter());
    }



    @Override
    public boolean execute(Canvas canvas) {
        if (!super.execute(canvas)) {
            return false;
        }

        canvas.drawPixel(getX(), getY(), getNewCharacter());
        finallyExecute();

        return isExecuted();
    }

    @Override
    public boolean redo() {
        if (!super.redo()) {
            return false;
        }

        getCanvas().drawPixel(getX(), getY(), getNewCharacter());
        finallyRedo();

        return true;
    }

}

// FillHorizontalLine.java
package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public class FillHorizontalLine extends FillLine {

    public FillHorizontalLine(int y, char character) {
        super(y, character);
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (!super.execute(canvas)) {
            return false;
        }

        canvas.fillHorizontalLine(getTargetIndex(), getNewCharacter());
        finallyExecute();

        return isExecuted();
    }



    @Override
    public boolean redo() {
        if (!super.redo()) {
            return false;
        }

        getCanvas().fillHorizontalLine(getTargetIndex(), getNewCharacter());
        finallyRedo();

        return true;
    }

    @Override
    protected ArrayList<Character> findOriginalCharacters(Canvas canvas, int targetIndex) {
        ArrayList<Character> charactersInRow = new ArrayList<>();
        for (int x = 0; x < canvas.getWidth(); x++) {
            charactersInRow.add(canvas.getPixel(x, targetIndex));
        }

        return charactersInRow;
    }

    protected boolean isValidTargetIndex(int index) {
        return index >= 0 && index < getCanvas().getHeight();
    }

    @Override
    protected void reDrawOriginal(Canvas canvas) {
        int x = 0;
        for (Character c : getOriginalCharacters()) {
            canvas.drawPixel(x++, getTargetIndex(), c);
        }
    }
}

// FillLine.java
package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public abstract class FillLine extends Command {
    private final int TARGET_INDEX;
    private final char NEW_CHARACTER;

    private final ArrayList<Character> originalCharacters = new ArrayList<>();


    protected FillLine(int targetIndex, char character) {
        TARGET_INDEX = targetIndex;
        NEW_CHARACTER = character;
    }

    protected int getTargetIndex() {
        return this.TARGET_INDEX;
    }

    protected char getNewCharacter() {
        return this.NEW_CHARACTER;
    }

    protected ArrayList<Character> getOriginalCharacters() {
        return this.originalCharacters;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (!super.execute(canvas)) {
            return false;
        }

        if (!isValidTargetIndex(getTargetIndex())) {
            return false;
        }

        getOriginalCharacters().addAll(findOriginalCharacters(getCanvas(), getTargetIndex()));

        return true;
    }

    @Override
    public boolean undo() {
        if (!super.undo()) {
            return false;
        }

        reDrawOriginal(getCanvas());
        finallyUndo();

        return true;
    }

    protected abstract ArrayList<Character> findOriginalCharacters(Canvas canvas, int targetIndex);

    protected abstract void reDrawOriginal(Canvas canvas);

    protected abstract boolean isValidTargetIndex(int index);


    @Override
    public String getCommandInfo() {
        return String.format("%s(%d, %c)", getClass().getSimpleName(), getTargetIndex(), getNewCharacter());
    }
}

// FillVerticalLine.java
package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public class FillVerticalLine extends FillLine {

    public FillVerticalLine(int x, char character) {
        super(x, character);
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (!super.execute(canvas)) {
            return false;
        }
        canvas.fillVerticalLine(getTargetIndex(), getNewCharacter());
        finallyExecute();

        return isExecuted();
    }



    @Override
    public boolean redo() {
        if (!super.redo()) {
            return false;
        }

        getCanvas().fillVerticalLine(getTargetIndex(), getNewCharacter());
        finallyRedo();

        return true;
    }

    @Override
    protected ArrayList<Character> findOriginalCharacters(Canvas canvas, int targetIndex) {
        ArrayList<Character> charactersInColumn = new ArrayList<>();
        for (int y = 0; y < canvas.getHeight(); y++) {
            charactersInColumn.add(canvas.getPixel(targetIndex, y));
        }

        return charactersInColumn;
    }

    protected boolean isValidTargetIndex(int index) {
        return index >= 0 && index < getCanvas().getWidth();
    }

    @Override
    protected void reDrawOriginal(Canvas canvas) {
        int y = 0;
        for (Character c : getOriginalCharacters()) {
            canvas.drawPixel(getTargetIndex(), y++, c);
        }
    }
}

// IncreasePixel.java
package academy.pocu.comp2500.assignment4;

public class IncreasePixel extends ChangeOnePixel {

    public IncreasePixel(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (!super.execute(canvas)) {
            return false;
        }

        if (!canvas.increasePixel(getX(), getY())) {
            return false;
        }

        updateLastDrawingId();
        finallyExecute();

        return isExecuted();
    }

    @Override
    public boolean redo() {
        if (!super.redo()) {
            return false;
        }

        getCanvas().increasePixel(getX(), getY());

        finallyRedo();

        return true;
    }
}

// Os.java
package academy.pocu.comp2500.assignment4;

public enum Os {
    WINDOWS,
    MAC,
    LINUX,
    UNDEFINED
}