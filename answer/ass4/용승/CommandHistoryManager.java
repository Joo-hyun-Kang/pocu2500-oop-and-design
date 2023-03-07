package academy.pocu.comp2500.assignment4;

import java.util.ArrayList;
import java.util.List;

public class CommandHistoryManager {
    private final Canvas canvas;
    private final List<ICommand> executedCommands;
    private final List<ICommand> undoneCommands;

    public CommandHistoryManager(Canvas canvas) {
        this.canvas = canvas;
        this.executedCommands = new ArrayList<>();
        this.undoneCommands = new ArrayList<>();
    }

    public boolean execute(ICommand command) {
        if (command.execute(this.canvas)) {
            this.executedCommands.add(command);
            this.undoneCommands.clear();
            return true;
        }
        return false;
    }

    public boolean canUndo() {
        return this.executedCommands.size() != 0;
    }

    public boolean canRedo() {
        return this.undoneCommands.size() != 0;
    }

    public boolean undo() {
        if (canUndo()) {
            int lastIndex = this.executedCommands.size() - 1;
            ICommand lastExecutedCommand = this.executedCommands.get(lastIndex);
            if (lastExecutedCommand.undo()) {
                this.executedCommands.remove(lastIndex);
                this.undoneCommands.add(lastExecutedCommand);
                return true;
            }
        }
        return false;
    }

    public boolean redo() {
        if (canRedo()) {
            int lastIndex = this.undoneCommands.size() - 1;
            ICommand lastUndoneCommand = this.undoneCommands.get(lastIndex);
            if (lastUndoneCommand.redo()) {
                this.undoneCommands.remove(lastIndex);
                this.executedCommands.add(lastUndoneCommand);
                return true;
            }
        }
        return false;
    }
}
