// Clear.java
package academy.pocu.comp2500.assignment4;

public class Clear extends Command {
    private String originalDrawing;

    @Override
    public boolean execute(Canvas canvas) {
        if (!super.execute(canvas)) {
            return false;
        }

        setOriginalDrawing(canvas.getDrawing());
        canvas.clear();
        finallyExecute();

        return isExecuted();
    }

    @Override
    public boolean undo() {
        if (!super.undo()) {
            return false;
        }

        reDraw(getCanvas(), getOriginalDrawing());
        finallyUndo();

        return true;
    }

    @Override
    public boolean redo() {
        if (!super.redo()) {
            return false;
        }

        getCanvas().clear();
        finallyRedo();

        return true;
    }

    private String getOriginalDrawing() {
        return this.originalDrawing;
    }

    private void setOriginalDrawing(String originalDrawing) {
        this.originalDrawing = originalDrawing;
    }

    private void reDraw(Canvas canvas, String originalDrawing) {
        Os os = getOs();
        char comparedChar = 0;
        int lineSeparatorLength = 0;
        switch (os) {
            case WINDOWS:
                comparedChar = '\r';
                lineSeparatorLength = 2;
                break;
            case MAC:
                comparedChar = '\r';
                lineSeparatorLength = 1;
                break;
            case LINUX:
                comparedChar = '\n';
                lineSeparatorLength = 1;
                break;
            default:
                assert false : "Warning : undefined OS";

        }
        assert (lineSeparatorLength == System.lineSeparator().length());

        char[] original = originalDrawing.toCharArray();
        int drawingWidth = 0;
        for (int i = 0; i < originalDrawing.length(); i++) {
            if (original[i] == comparedChar) {
                drawingWidth = i;
                break;
            }
        }

        int drawingHeight = originalDrawing.length() / (drawingWidth + lineSeparatorLength);

        for (int i = 0; i < originalDrawing.length(); i++) {
            int x = i % (drawingWidth + 2) - 1;
            int y = i / (drawingWidth + 2) - 1;

            if (x == -1 || x == drawingWidth - 1 || x == drawingWidth || y == -1 || y == drawingHeight - 2) {
                continue;
            }

            if (getOs().equals(Os.WINDOWS)) {
                if (x == drawingWidth - 2) {
                    continue;
                }
            }

            getCanvas().drawPixel(x, y, original[i]);
        }
    }

    private Os getOs() {
        String lineSeparator = System.lineSeparator();
        if (lineSeparator.equals("\r\n")) {
            return Os.WINDOWS;
        } else if (lineSeparator.equals("\r")) {
            return Os.MAC;
        } else if (lineSeparator.equals("\n")) {
            return Os.LINUX;
        } else {
            return Os.UNDEFINED;
        }
    }

    @Override
    public String getCommandInfo() {
        return String.format("%s", getClass().getSimpleName());
    }
}

// Command.java
package academy.pocu.comp2500.assignment4;

public abstract class Command implements ICommand {

    private boolean isExecuted;
    private Canvas canvas;
    private boolean isOnUndoState;

    private int lastDrawingId;

    public Canvas getCanvas() {
        return this.canvas;
    }

    protected void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    protected boolean isExecuted() {
        return this.isExecuted;
    }

    protected void setExecutedOn() {
        isExecuted = true;
    }

    public boolean isOnUndoState() {
        return this.isOnUndoState;
    }

    protected void setToUndoState() {
        this.isOnUndoState = true;
    }

    protected void setToRedoState() {
        this.isOnUndoState = false;
    }

    @Override
    public boolean execute(Canvas canvas) {
        if (isExecuted) {
            System.out.println("already executed");

            return false;
        }

        setCanvas(canvas);

        return true;
    }

    public void finallyExecute() {
        updateLastDrawingId();
        setExecutedOn();
    }

    @Override
    public boolean undo() {
        if (!isExecuted) {
            System.out.println("execute first");

            return false;
        }

        if (isOnUndoState()) {
            System.out.println("already in undo state");

            return false;
        }

        if (isContentChanged(getCanvas())) {
            System.out.println("canvas drawing has changed");

            return false;
        }

        return true;
    }

    public void finallyUndo() {
        updateLastDrawingId();
        setToUndoState();
    }

    @Override
    public boolean redo() {
        if (!isExecuted) {
            System.out.println("execute first");

            return false;
        }

        if (isContentChanged(getCanvas())) {
            System.out.println("canvas drawing has changed");

            return false;
        }

        if (!isOnUndoState()) {
            System.out.println("undo first");

            return false;
        }

        return true;
    }

    public void finallyRedo() {
        updateLastDrawingId();
        setToRedoState();
    }

    public abstract String getCommandInfo();

    protected int getDrawingId(Canvas canvas) {
        return canvas.getDrawing().hashCode();
    }

    protected int getLastDrawingId() {
        return this.lastDrawingId;
    }

    protected void updateLastDrawingId() {
        this.lastDrawingId = getCanvas().getDrawing().hashCode();
    }

    protected boolean isContentChanged(Canvas canvas) {
        if (getLastDrawingId() != getDrawingId(canvas)) {
            return true;
        }

        return false;
    }
}

// CommandHistoryManager.java
package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;

public class CommandHistoryManager {
    private Canvas canvas;
    private ArrayList<ICommand> commandHistory = new ArrayList<>();
    private int currentStreamIndex = -1;

    public CommandHistoryManager(Canvas canvas) {
        this.canvas = canvas;
    }


    private ICommand getPreviousCommand() {
        return getCommandHistory().get(currentStreamIndex - 1);
    }

    private ICommand getCurrentCommand() {
        return getCommandHistory().get(getCurrentStreamIndex());
    }

    private ICommand getNextCommand() {
        return getCommandHistory().get(currentStreamIndex + 1);
    }

    public int getCurrentStreamIndex() {
        return this.currentStreamIndex;
    }

    private int getLastStreamIndex() {
        return getCommandHistory().size() - 1;
    }

    private boolean decreaseCurrentStreamIndex() {
        this.currentStreamIndex--;

        return true;
    }

    private boolean increaseCurrentStreamIndex() {
        if (getCurrentStreamIndex() + 1 > getCommandHistory().size() - 1) {
            System.out.println("already on last stream");

            return false;
        }

        this.currentStreamIndex++;

        return true;
    }



    private void goToPreviousStream() {
        if (getCurrentStreamIndex() < 0) {
            System.out.println("cannot go to previous stream");

            return;
        }

        if (!decreaseCurrentStreamIndex()) {
            return;
        }
    }

    private void goToNextStream() {
        if (!increaseCurrentStreamIndex()) {
            return;
        }

    }



    public ArrayList<ICommand> getCommandHistory() {
        return this.commandHistory;
    }

    private void addCommandHistory(ICommand command) {
        getCommandHistory().add(command);
    }


    public Canvas getCanvas() {
        return this.canvas;
    }

    public boolean execute(ICommand command) {
        if (!command.execute(getCanvas())) {
            System.out.println("execution fails");

            return false;
        }

        if (getCurrentStreamIndex() < getCommandHistory().size() - 1) {
            while (getCurrentStreamIndex() < getCommandHistory().size() - 1) {
                getCommandHistory().remove(getCurrentStreamIndex() + 1);
            }
        }

        addCommandHistory(command);
        goToNextStream();

        return true;
    }

    public boolean canUndo() {
        if (getCurrentStreamIndex() <= -1) {
            return false;
        }

        Command com = (Command) getCurrentCommand();

        return com.isExecuted() && !com.isOnUndoState();
    }

    public boolean canRedo() {
        if (getCurrentStreamIndex() == getLastStreamIndex()) {
            return false;
        }

        return ((Command) getNextCommand()).isOnUndoState();
    }

    public boolean undo() {
        if (!canUndo()) {
            return false;
        }

        getCurrentCommand().undo();
        goToPreviousStream();

        return true;
    }

    public boolean redo() {
        if (!canRedo()) {
            return false;
        }

        getNextCommand().redo();
        goToNextStream();

        return true;
    }
}