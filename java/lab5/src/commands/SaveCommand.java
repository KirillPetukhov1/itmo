package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to save the collection to a file.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class SaveCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {
        
    /**
     * Constructs a SaveCommand with the specified collection manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public SaveCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the save command.
     * Saves the current state of the collection to a file.
     *
     * @param args command arguments (expects exactly one argument - the command name)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 1) {
            getCollectionManager().save();
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "save : сохранить коллекцию в файл" - command description in Russian
     */
    public String getDescription() {
        return "save : сохранить коллекцию в файл";
    }
}