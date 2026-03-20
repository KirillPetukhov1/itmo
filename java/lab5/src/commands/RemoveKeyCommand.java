package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to remove an element from the collection by its key.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class RemoveKeyCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs a RemoveKeyCommand with the specified collection manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public RemoveKeyCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the remove key command.
     * Removes the element with the specified key from the collection.
     *
     * @param args command arguments (expects command name and key value)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 2) {
            K key = getCollectionManager().getKeyParser().getParsedObject(args[1]);
            getCollectionManager().removeKey(key);
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "remove_key null : удалить элемент из коллекции по его ключу" -
     *         command description in Russian
     */
    public String getDescription() {
        return "remove_key null : удалить элемент из коллекции по его ключу";
    }
}