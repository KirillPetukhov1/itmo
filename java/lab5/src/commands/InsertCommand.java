package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to insert a new element with the specified key.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class InsertCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs an InsertCommand with the specified collection manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public InsertCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the insert command.
     * Adds a new element with the specified key to the collection.
     *
     * @param args command arguments (expects command name and key value)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 2) {
            K key = getCollectionManager().getKeyParser().getParsedObject(args[1]);
            getCollectionManager().insert(key);
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "insert null {element} : добавить новый элемент с заданным ключом" -
     *         command description in Russian
     */
    public String getDescription() {
        return "insert null {element} : добавить новый элемент с заданным ключом";
    }
}