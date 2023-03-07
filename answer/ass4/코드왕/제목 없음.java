// Canvas.java
package academy.pocu.comp2500.assignment4;
public class Canvas {
    private final char DEFAULT = ' ';
    private Character[][] canvas;
    public Canvas(int width, int height) {
        canvas = new Character[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                canvas[y][x] = DEFAULT;
            }
        }
    }
    public int getWidth() {
        return canvas[0].length;
    }
    public int getHeight() {
        return canvas.length;
    }
    public void drawPixel(int x, int y, char c) {
        if (!isValidCoordinate(x, y)) {
            System.out.println("Invalid input x, y");
            return;
        }
        if (!isValidChar(c)) {
            System.out.println("Invalid input char");
            return;
        }
        canvas[y][x] = c;
    }
    public char getPixel(int x, int y) {
        return this.canvas[y][x];
    }
    public boolean increasePixel(int x, int y) {
        return changePixelBy(x, y, 1);
    }
    public boolean decreasePixel(int x, int y) {
        return changePixelBy(x, y, -1);
    }
    public void toUpper(int x, int y) {
        if (getPixel(x, y) < 97 || getPixel(x, y) > 122) {
            return;
        }
        changePixelBy(x, y, -32);
    }
    public void toLower(int x, int y) {
        if (getPixel(x, y) < 65 || getPixel(x, y) > 90) {
            return;
        }
        changePixelBy(x, y, 32);
    }
    private boolean changePixelBy(int x, int y, int value) {
        if (!isValidCoordinate(x, y)) {
            System.out.println("Invalid input x, y");
            return false;
        }
        char original = getPixel(x, y);
        char newChar = (char) (original + value);
        if (!isValidChar(newChar)) {
            return false;
        }
        drawPixel(x, y, newChar);
        return getPixel(x, y) == original + value;
    }
    public void fillHorizontalLine(int y, char c) {
        if (!isValidCoordinate(0, y)) {
            System.out.println("Invalid input y");
            return;
        }
        if (!isValidChar(c)) {
            System.out.println("Invalid input char");
            return;
        }
        for (int x = 0; x < this.getWidth(); x++) {
            drawPixel(x, y, c);
        }
    }
    public void fillVerticalLine(int x, char c) {
        if (!isValidCoordinate(x, 0)) {
            System.out.println("Invalid input x");
            return;
        }
        if (!isValidChar(c)) {
            System.out.println("Invalid input char");
            return;
        }
        for (int y = 0; y < this.getHeight(); y++) {
            drawPixel(x, y, c);
        }
    }
    public void clear() {
        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                drawPixel(x, y, DEFAULT);
            }
        }
    }
    private boolean isValidChar(char c) {
        return c >= 32 && c <= 126;
    }
    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight();
    }
    private Character[][] getCanvas() {
        return this.canvas;
    }
    public String getDrawing() {
        final int FRAME_WIDTH = 1;
        final char HORIZONTAL_LINE = '-';
        final char VERTICAL_LINE = '|';
        final char VERTEX = '+';
        Canvas drawing = new Canvas(this.getWidth() + 2 * FRAME_WIDTH, this.getHeight() + 2 * FRAME_WIDTH);
        int xOffset = FRAME_WIDTH;
        int yOffset = FRAME_WIDTH;
        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                drawing.drawPixel(x + xOffset, y + yOffset, this.getPixel(x, y));
            }
        }
        drawing.fillHorizontalLine(0, HORIZONTAL_LINE);
        drawing.fillHorizontalLine(drawing.getHeight() - 1, HORIZONTAL_LINE);
        drawing.fillVerticalLine(0, VERTICAL_LINE);
        drawing.fillVerticalLine(drawing.getWidth() - 1, VERTICAL_LINE);
        drawing.drawPixel(0, 0, VERTEX); 
        drawing.drawPixel(drawing.getWidth() - 1, 0, VERTEX);
        drawing.drawPixel(drawing.getWidth() - 1, drawing.getHeight() - 1, VERTEX);
        drawing.drawPixel(0, drawing.getHeight() - 1, VERTEX);
        Character[][] canvasWithFrame = drawing.getCanvas();
        StringBuilder sb = new StringBuilder((drawing.getWidth()) * (drawing.getHeight()));
        for (int y = 0; y < drawing.getHeight(); y++) {
            for (int x = 0; x < drawing.getWidth(); x++) {
                sb.append(canvasWithFrame[y][x]);
                if (x == drawing.getWidth() - 1) {
                    sb.append(System.lineSeparator());
                }
            }
        }
        return sb.toString();
    }
}
// ChangeOnePixel.java
package academy.pocu.comp2500.assignment4;
public abstract class ChangeOnePixel extends Command {
    private final int X;
    private final int Y;
    private char originalCharacter;
    private char newCharacter;
    protected ChangeOnePixel(int x, int y) {
        this.X = x;
        this.Y = y;
    }
    public int getX() {
        return this.X;
    }
    public int getY() {
        return this.Y;
    }
    protected char getNewCharacter() {
        return this.newCharacter;
    }
    protected void setNewCharacter(char newCharacter) {
        this.newCharacter = newCharacter;
    }
    public char getOriginalCharacter() {
        return this.originalCharacter;
    }
    protected void setOriginalCharacter(char originalCharacter) {
        this.originalCharacter = originalCharacter;
    }
    @Override
    public boolean execute(Canvas canvas) {
        if (!super.execute(canvas)) {
            return false;
        }
        if (!isValidCoordinate(getX(), getY(), getCanvas())) {
            return false;
        }
        setOriginalCharacter(canvas.getPixel(getX(), getY()));
        return true;
    }
    @Override
    public boolean undo() {
        if (!super.undo()) {
            return false;
        }
        getCanvas().drawPixel(getX(), getY(), getOriginalCharacter());
        finallyUndo();
        return true;
    }
    protected boolean isValidCoordinate(int x, int y, Canvas canvas) {
        return x >= 0 && x < canvas.getWidth() && y >= 0 && y < canvas.getHeight();
    }
    @Override
    public String getCommandInfo() {
        return String.format("%s(%d, %d)", getClass().getSimpleName(), getX(), getY());
    }
}