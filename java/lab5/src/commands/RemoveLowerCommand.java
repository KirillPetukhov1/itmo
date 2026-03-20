package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to remove all elements smaller than the specified element.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class RemoveLowerCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs a RemoveLowerCommand with the specified collection manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public RemoveLowerCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the remove lower command.
     * Removes all elements from the collection that are smaller than the specified
     * element.
     *
     * @param args command arguments (expects exactly one argument - the command
     *             name)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 1) {
            getCollectionManager().removeLower();
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "remove_lower {element} : удалить из коллекции все элементы, меньшие,
     *         чем заданный" - command description in Russian
     */
    public String getDescription() {
        return "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный";
    }
}