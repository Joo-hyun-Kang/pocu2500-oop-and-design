package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;
import java.util.LinkedList;

public class OverdrawAnalyzer extends Canvas {
    private final ArrayList<LinkedList<Character>> pixelHistories;

    public OverdrawAnalyzer(int width, int height) {
        super(width, height);
        this.pixelHistories = new ArrayList<>();
        initializePixelHistories();
    }

    public LinkedList<Character> getPixelHistory(int x, int y) {
        if (getIndex(x, y) >= this.pixelHistories.size()) {
            throw new IllegalArgumentException();
        }

        return this.pixelHistories.get(getIndex(x, y));
    }

    public int getOverdrawCount(int x, int y) {
        return this.pixelHistories.get(getIndex(x, y)).size();
    }

    public int getOverdrawCount() {
        int width = super.getWidth();
        int height = super.getHeight();
        int total = 0;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                total += getOverdrawCount(x, y);
            }
        }

        return total;
    }

    @Override
    public void drawPixel(int x, int y, char character) {
        if (super.getPixel(x, y) != character) {
            updatePixelHistory(x, y, character);
            super.drawPixel(x, y, character);
        }
    }

    @Override
    public boolean increasePixel(int x, int y) {
        if (super.getPixel(x, y) != 126) {
            updatePixelHistory(x, y, (char) (super.getPixel(x, y) + 1));
        }
        return super.increasePixel(x, y);
    }

    @Override
    public boolean decreasePixel(int x, int y) {
        if (super.getPixel(x, y) != 32) {
            updatePixelHistory(x, y, (char) (super.getPixel(x, y) - 1));
        }
        return super.decreasePixel(x, y);
    }

    @Override
    public void toUpper(int x, int y) {
        if (isLowercaseEnglishAlphabet(super.getPixel(x, y))) {
            updatePixelHistory(x, y, (char) (super.getPixel(x, y) & (~0x20)));
            super.toUpper(x, y);
        }
    }

    @Override
    public void toLower(int x, int y) {
        if (isUppercaseEnglishAlphabet(super.getPixel(x, y))) {
            updatePixelHistory(x, y, (char) (super.getPixel(x, y) | 0x20));
            super.toLower(x, y);
        }
    }

    @Override
    public void fillHorizontalLine(int y, char character) {
        int width = super.getWidth();
        for (int x = 0; x < width; ++x) {
            if (super.getPixel(x, y) != character) {
                super.drawPixel(x, y, character);
                updatePixelHistory(x, y, character);
            }
        }
    }

    @Override
    public void fillVerticalLine(int x, char character) {
        int height = super.getHeight();
        for (int y = 0; y < height; ++y) {
            if (super.getPixel(x, y) != character) {
                super.drawPixel(x, y, character);
                updatePixelHistory(x, y, character);
            }
        }
    }

    @Override
    public void clear() {
        int height = super.getHeight();
        int width = super.getWidth();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (super.getPixel(x, y) != ' ') {
                    super.drawPixel(x, y, ' ');
                    updatePixelHistory(x, y, ' ');
                }
            }
        }
    }

    private void initializePixelHistories() {
        int count = 0;
        int numPixels = super.getHeight() * super.getWidth();

        while (count < numPixels) {
            this.pixelHistories.add(new LinkedList<>());
            count++;
        }
    }

    private int getIndex(int x, int y) {
        return (y * super.getWidth()) + x;
    }

    private boolean isUppercaseEnglishAlphabet(char character) {
        return 'A' <= character && character <= 'Z';
    }

    private boolean isLowercaseEnglishAlphabet(char character) {
        return 'a' <= character && character <= 'z';
    }

    private void updatePixelHistory(int x, int y, Character overwrittenCharacter) {
        LinkedList<Character> pixelHistory = this.getPixelHistory(x, y);
        pixelHistory.addLast(overwrittenCharacter);
    }
}
