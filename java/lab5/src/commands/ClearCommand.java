package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to clear all elements from the collection.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class ClearCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs a ClearCommand with the specified collection manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public ClearCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the clear command.
     * Removes all elements from the collection.
     *
     * @param args command arguments (expects exactly one argument - the command
     *             name)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 1) {
            getCollectionManager().clear();
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "clear : очистить коллекцию" - command description in Russian
     */
    public String getDescription() {
        return "clear : очистить коллекцию";
    }
}