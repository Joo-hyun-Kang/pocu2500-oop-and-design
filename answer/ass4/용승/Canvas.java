package academy.pocu.comp2500.assignment4;

public class Canvas {
    private final int width;
    private final int height;
    private final char[][] pixels;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new char[height][width];
        initializeCanvas();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void drawPixel(int x, int y, char character) {
        if (isPixelOutsideBound(x, y) || isCharNotPrintable(character)) {
            throw new IllegalArgumentException();
        }

        this.pixels[y][x] = character;
    }

    public char getPixel(int x, int y) {
        if (isPixelOutsideBound(x, y)) {
            throw new IllegalArgumentException();
        }

        return this.pixels[y][x];
    }

    public boolean increasePixel(int x, int y) {
        if (isPixelOutsideBound(x, y)) {
            throw new IllegalArgumentException();
        }

        if (isCharNotPrintable((char) (this.pixels[y][x] + 1))) {
            return false;
        }

        this.pixels[y][x] += 1;
        return true;
    }

    public boolean decreasePixel(int x, int y) {
        if (isPixelOutsideBound(x, y)) {
            throw new IllegalArgumentException();
        }

        if (isCharNotPrintable((char) (this.pixels[y][x] - 1))) {
            return false;
        }

        this.pixels[y][x] -= 1;
        return true;
    }

    public void toUpper(int x, int y) {
        if (isPixelOutsideBound(x, y)) {
            throw new IllegalArgumentException();
        }

        if (isCharNotEnglishAlphabet(this.pixels[y][x])) {
            return;
        }

        this.pixels[y][x] &= (~0x20);
    }

    public void toLower(int x, int y) {
        if (isPixelOutsideBound(x, y)) {
            throw new IllegalArgumentException();
        }

        if (isCharNotEnglishAlphabet(this.pixels[y][x])) {
            return;
        }

        this.pixels[y][x] |= 0x20;
    }

    public void fillHorizontalLine(int y, char character) {
        if (y < 0 || y >= this.height || isCharNotPrintable(character)) {
            throw new IllegalArgumentException();
        }

        for (int x = 0; x < this.width; ++x) {
            this.pixels[y][x] = character;
        }
    }

    public void fillVerticalLine(int x, char character) {
        if (x < 0 || x >= this.width || isCharNotPrintable(character)) {
            throw new IllegalArgumentException();
        }

        for (int y = 0; y < this.height; ++y) {
            this.pixels[y][x] = character;
        }
    }

    public void clear() {
        initializeCanvas();
    }

    public String getDrawing() {
        StringBuilder sb = new StringBuilder();

        drawHorizontalBound(sb);
        drawRows(sb);
        drawHorizontalBound(sb);

        return sb.toString();
    }

    private void initializeCanvas() {
        for (int y = 0; y < this.height; ++y) {
            for (int x = 0; x < this.width; ++x) {
                this.pixels[y][x] = ' ';
            }
        }
    }

    private boolean isCharNotPrintable(char character) {
        return character < 32 || 126 < character;
    }

    private boolean isPixelOutsideBound(int x, int y) {
        return 0 > x || x >= this.width || 0 > y || y >= this.height;
    }

    private boolean isCharNotEnglishAlphabet(char character) {
        return character < 'A'
                || ('Z' < character && character < 'a')
                || 'z' < character;
    }

    private void drawHorizontalBound(StringBuilder stringBuilder) {
        stringBuilder.append("+");
        stringBuilder.append(String.format("%" + this.width + "s", " ").replace(" ", "-"));
        stringBuilder.append("+");
        stringBuilder.append(System.lineSeparator());
    }

    private void drawRows(StringBuilder stringBuilder) {
        for (int y = 0; y < this.height; ++y) {
            drawRow(stringBuilder, y);
        }
    }

    private void drawRow(StringBuilder stringBuilder, int y) {
        stringBuilder.append("|");
        for (int x = 0; x < this.width; ++x) {
            stringBuilder.append(this.pixels[y][x]);
        }
        stringBuilder.append("|");
        stringBuilder.append(System.lineSeparator());
    }
}
