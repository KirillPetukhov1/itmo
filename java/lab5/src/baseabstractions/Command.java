package src.baseabstractions;

import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Abstract base class for commands in the application.
 * Provides common functionality for executing commands on a collection manager.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public abstract class Command<K extends Comparable<K>, V extends Product> {
    CollectionManager<K, V> collectionManager;

    /**
     * Constructs a Command with the specified collection manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public Command(CollectionManager<K, V> collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Returns the collection manager associated with this command.
     *
     * @return the collection manager instance
     */
    public CollectionManager<K, V> getCollectionManager() {
        return collectionManager;
    }

    /**
     * Executes the command with the given arguments.
     *
     * @param args the arguments for command execution
     */
    public abstract void execute(String[] args);

    /**
     * Returns a description of what this command does.
     *
     * @return the command description string
     */
    public abstract String getDescription();
}