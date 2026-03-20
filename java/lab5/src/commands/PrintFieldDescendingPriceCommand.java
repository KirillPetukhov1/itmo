package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to print all price values in descending order.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class PrintFieldDescendingPriceCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs a PrintFieldDescendingPriceCommand with the specified collection
     * manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public PrintFieldDescendingPriceCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the print field descending price command.
     * Displays all price values from the collection in descending order.
     *
     * @param args command arguments (expects exactly one argument - the command
     *             name)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 1) {
            getCollectionManager().printFieldDescendingPrice();
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "print_field_descending_price : вывести значения поля price всех
     *         элементов в порядке убывания" - command description in Russian
     */
    public String getDescription() {
        return "print_field_descending_price : вывести значения поля price всех элементов в порядке убывания";
    }
}