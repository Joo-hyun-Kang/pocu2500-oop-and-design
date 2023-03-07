package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public class Canvas {
    private final int width;
    private final int height;
    private final ArrayList<ArrayList<Character>> board = new ArrayList<>();

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;

        for (int y = 0; y < height; ++y) {
            board.add(new ArrayList<>());
            for (int x = 0; x < width; ++x) {
                board.get(y).add(' ');
            }
        }
    }

    public void clear() {
        for (int y = 0; y < height; ++y) {
            board.add(new ArrayList<>());
            for (int x = 0; x < width; ++x) {
                drawPixel(x, y, ' ');
            }
        }
    }

    public void drawPixel(int x, int y, char character) {
        if (!isValidChar(character) || !isValidPosition(x, y)) {
            return;
        }

        this.board.get(y).set(x, character);
    }

    public char getPixel(int x, int y) {
        return this.board.get(y).get(x);
    }

    public boolean increasePixel(int x, int y) {
        if (!isValidPosition(x, y)) {
            return false;
        }

        char character = (char) (board.get(y).get(x) + 1);

        if (!isValidChar(character)) {
            return false;
        }

        drawPixel(x, y, character);
        return true;
    }

    public boolean decreasePixel(int x, int y) {
        if (!isValidPosition(x, y)) {
            return false;
        }

        char character = (char) (board.get(y).get(x) - 1);

        if (!isValidChar(character)) {
            return false;
        }

        drawPixel(x, y, character);
        return true;
    }

    public void toUpper(int x, int y) {
        if (!isValidPosition(x, y)) {
            return;
        }

        char character = board.get(y).get(x);
        drawPixel(x, y, Character.toUpperCase(character));
    }

    public void toLower(int x, int y) {
        if (!isValidPosition(x, y)) {
            return;
        }

        char character = board.get(y).get(x);
        drawPixel(x, y, Character.toLowerCase(character));
    }

    public void fillHorizontalLine(int y, char character) {
        if (!isValidPosition(0, y)) {
            return;
        }

        for (int x = 0; x < width; ++x) {
            drawPixel(x, y, character);
        }
    }

    public void fillVerticalLine(int x, char character) {
        if (!isValidPosition(x, 0)) {
            return;
        }

        for (int y = 0; y < height; ++y) {
            drawPixel(x, y, character);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getDrawing() {
        StringBuilder sb = new StringBuilder();

        drawHorizontalBar(sb);
        for (int y = 0; y < height; ++y) {
            sb.append('|');
            for (int x = 0; x < width; ++x) {
                sb.append(getPixel(x, y));
            }
            sb.append('|').append(System.lineSeparator());
        }
        drawHorizontalBar(sb);

        return sb.toString();
    }

    private static boolean isValidChar(char character) {
        return (character >= 32 && character <= 126);
    }

    private void drawHorizontalBar(StringBuilder sb) {
        sb.append('+');
        sb.append("-".repeat(width));
        sb.append('+');
        sb.append(System.lineSeparator());
    }

    private boolean isValidPosition(int x, int y) {
        return (x >= 0 && y >= 0 && x < width && y < height);
    }
}
