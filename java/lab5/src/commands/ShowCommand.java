package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to display all elements of the collection in string representation.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class ShowCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs a ShowCommand with the specified collection manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public ShowCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the show command.
     * Displays all elements of the collection in string representation.
     *
     * @param args command arguments (expects exactly one argument - the command
     *             name)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 1) {
            getCollectionManager().show();
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "show : вывести в стандартный поток вывода все элементы коллекции в
     *         строковом представлении" - command description in Russian
     */
    public String getDescription() {
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}