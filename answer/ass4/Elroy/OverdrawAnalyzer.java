package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;
import java.util.LinkedList;

public class OverdrawAnalyzer extends Canvas {
    private final ArrayList<ArrayList<LinkedList<Character>>> historyBoard = new ArrayList<>();

    public OverdrawAnalyzer(int width, int height) {
        super(width, height);
        initBoard();
    }

    @Override
    public void drawPixel(int x, int y, char character) {
        if (getPixel(x, y) == character) {
            return;
        }

        historyBoard.get(y).get(x).addLast(character);
        super.drawPixel(x, y, character);
    }

    public LinkedList<Character> getPixelHistory(int x, int y) {
        return historyBoard.get(y).get(x);
    }

    public int getOverdrawCount(int x, int y) {
        return historyBoard.get(y).get(x).size();
    }

    public int getOverdrawCount() {
        int count = 0;

        for (int y = 0; y < getHeight(); ++y) {
            for (int x = 0; x < getWidth(); ++x) {
                count += historyBoard.get(y).get(x).size();
            }
        }

        return count;
    }

    private void initBoard() {
        for (int y = 0; y < getHeight(); ++y) {
            historyBoard.add(new ArrayList<>());
            for (int x = 0; x < getWidth(); ++x) {
                historyBoard.get(y).add(new LinkedList<>());
            }
        }
    }
}
