package academy.pocu.comp2500.assignment4;

public class ToUppercaseCommand extends Command implements ICommand {
    private char previousCharacter;

    public ToUppercaseCommand(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (!this.wasExecuted) {
            try {
                saveOriginalState(canvas);
                canvas.toUpper(this.x, this.y);
                this.canvasReference = canvas;
                this.wasExecuted = true;
                return true;
            } catch (IllegalArgumentException exception) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean undo() {
        if (canUndo()) {
            restoreOriginalState();
            this.wasUndone = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean redo() {
        if (canRedo()) {
            this.canvasReference.toUpper(this.x, this.y);
            this.wasUndone = false;
            return true;
        }
        return false;
    }

    @Override
    protected void saveOriginalState(Canvas canvas) {
        this.previousCharacter = canvas.getPixel(this.x, this.y);
    }

    @Override
    protected boolean canCurrentStateBeUndone() {
        if (!isCharNotEnglishAlphabet(this.previousCharacter)) {
            return (this.previousCharacter & (~0x20)) == this.canvasReference.getPixel(this.x, this.y);
        }
        return true;
    }

    @Override
    protected void restoreOriginalState() {
        this.canvasReference.drawPixel(this.x, this.y, this.previousCharacter);

    }

    @Override
    protected boolean canCurrentStateBeRedone() {
        return this.canvasReference.getPixel(this.x, this.y) == this.previousCharacter;
    }

    private boolean isCharNotEnglishAlphabet(char character) {
        return character < 'A'
                || ('Z' < character && character < 'a')
                || 'z' < character;
    }
}
