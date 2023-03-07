package academy.pocu.comp2500.assignment4;

import java.util.Stack;

public class CommandHistoryManager {
    private final Canvas canvas;
    private final Stack<ICommand> undoStack = new Stack<>();
    private final Stack<ICommand> redoStack = new Stack<>();

    public CommandHistoryManager(Canvas canvas) {
        this.canvas = canvas;
    }

    public boolean execute(ICommand command) {
        boolean isExecuted = command.execute(canvas);

        if (isExecuted) {
            undoStack.push(command);
            redoStack.clear();
        }

        return isExecuted;
    }

    public boolean canUndo() {
        return !undoStack.empty();
    }

    public boolean canRedo() {
        return !redoStack.empty();
    }

    public boolean undo() {
        if (!canUndo()) {
            return false;
        }

        ICommand command = undoStack.pop();
        boolean isSuccess = command.undo();

        if (isSuccess) {
            redoStack.push(command);
        }

        return isSuccess;
    }

    public boolean redo() {
        if (!canRedo()) {
            return false;
        }

        ICommand command = redoStack.pop();
        boolean isSuccess = command.redo();

        if (isSuccess) {
            undoStack.push(command);
        }

        return isSuccess;
    }
}
