package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to exit the program without saving.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class ExitCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs an ExitCommand with the specified collection manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public ExitCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the exit command.
     * Terminates the program without saving the collection to file.
     *
     * @param args command arguments (expects exactly one argument - the command
     *             name)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 1) {
            getCollectionManager().exit();
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "exit : завершить программу (без сохранения в файл)" - command
     *         description in Russian
     */
    public String getDescription() {
        return "exit : завершить программу (без сохранения в файл)";
    }
}