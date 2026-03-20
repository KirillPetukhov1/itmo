package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to display information about the collection.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class InfoCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs an InfoCommand with the specified collection manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public InfoCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the info command.
     * Displays information about the collection (type, initialization date, size,
     * etc.).
     *
     * @param args command arguments (expects exactly one argument - the command
     *             name)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 1) {
            getCollectionManager().info();
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "info : вывести в стандартный поток вывода информацию о коллекции
     *         (тип, дата инициализации, количество элементов и т.д.)" - command
     *         description in Russian
     */
    public String getDescription() {
        return "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}