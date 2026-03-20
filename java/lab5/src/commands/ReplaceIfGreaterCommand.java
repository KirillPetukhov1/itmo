package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to replace an element by key if the new value is greater than the old
 * one.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class ReplaceIfGreaterCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs a ReplaceIfGreaterCommand with the specified collection manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public ReplaceIfGreaterCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the replace if greater command.
     * Replaces the element with the specified key if the new element is greater
     * than the existing one.
     *
     * @param args command arguments (expects command name and key value)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 2) {
            K key = getCollectionManager().getKeyParser().getParsedObject(args[1]);
            getCollectionManager().replaceIfGreater(key);
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "replace_if_greater null {element} : заменить значение по ключу, если
     *         новое значение больше старого" - command description in Russian
     */
    public String getDescription() {
        return "replace_if_greater null {element} : заменить значение по ключу, если новое значение больше старого";
    }
}