package academy.pocu.comp2500.assignment4;

public interface ICommand {
    boolean execute(Canvas canvas);

    boolean undo();

    boolean redo();
}
