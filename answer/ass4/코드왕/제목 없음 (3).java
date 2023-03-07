// OverdrawAnalyzer.java
package academy.pocu.comp2500.assignment4;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class OverdrawAnalyzer extends Canvas {

    private HashMap<Coordinate, LinkedList<Character>> coordinateAndHistoryMap;

    public OverdrawAnalyzer(int width, int height) {
        super(width, height);
        coordinateAndHistoryMap = new HashMap<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                LinkedList<Character> history = new LinkedList<>();
                coordinateAndHistoryMap.put(new Coordinate(x, y), history);
            }
        }
    }

    @Override
    public void drawPixel(int x, int y, char c) {
        char oldCharacter = getPixel(x, y);
        super.drawPixel(x, y, c);
        char newCharacter = getPixel(x, y);

        if (oldCharacter != newCharacter) {
            addOverdrawHistory(x, y, c);
        }
    }


    public LinkedList<Character> getPixelHistory(int x, int y) {
        return getCoordinateAndHistoryMap().get(new Coordinate(x, y));

    }

    public int getOverdrawCount(int x, int y) {
        return getPixelHistory(x, y).size();
    }

    public int getOverdrawCount() {
        int sum = 0;
        for (Map.Entry<Coordinate, LinkedList<Character>> entry : getCoordinateAndHistoryMap().entrySet()) {
            sum += entry.getValue().size();
        }

        return sum;
    }

    private HashMap<Coordinate, LinkedList<Character>> getCoordinateAndHistoryMap() {
        return this.coordinateAndHistoryMap;
    }

    private void addOverdrawHistory(int x, int y, char c) {
        getPixelHistory(x, y).add(c);
    }
}

// ToLower.java
package academy.pocu.comp2500.assignment4;

public class ToLower extends ChangeOnePixel {


    public ToLower(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (!super.execute(canvas)) {
            return false;
        }

        canvas.toLower(getX(), getY());
        finallyExecute();

        return isExecuted();
    }

    @Override
    public boolean redo() {
        if (!super.redo()) {
            return false;
        }

        getCanvas().toLower(getX(), getY());
        finallyRedo();

        return true;
    }
}

// ToUpper.java
package academy.pocu.comp2500.assignment4;

public class ToUpper extends ChangeOnePixel {

    public ToUpper(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (!super.execute(canvas)) {
            return false;
        }

        canvas.toUpper(getX(), getY());
        finallyExecute();

        return isExecuted();
    }

    @Override
    public boolean redo() {
        if (!super.redo()) {
            return false;
        }

        getCanvas().toUpper(getX(), getY());
        finallyRedo();

        return true;
    }
}