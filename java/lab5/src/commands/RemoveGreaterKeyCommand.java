package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to remove all elements with keys greater than the specified key.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class RemoveGreaterKeyCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs a RemoveGreaterKeyCommand with the specified collection manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public RemoveGreaterKeyCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the remove greater key command.
     * Removes all elements whose keys are greater than the specified key.
     *
     * @param args command arguments (expects command name and key value)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 2) {
            K key = getCollectionManager().getKeyParser().getParsedObject(args[1]);
            getCollectionManager().removeGreaterKey(key);
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "remove_greater_key null : удалить из коллекции все элементы, ключ
     *         которых превышает заданный" - command description in Russian
     */
    public String getDescription() {
        return "remove_greater_key null : удалить из коллекции все элементы, ключ которых превышает заданный";
    }
}