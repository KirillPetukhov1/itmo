package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to count elements with price greater than the specified value.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class CountGreaterThanPriceCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs a CountGreaterThanPriceCommand with the specified collection
     * manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public CountGreaterThanPriceCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the count greater than price command.
     * Counts and displays products with price greater than the specified value.
     *
     * @param args command arguments (expects command name and price value)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 2) {
            long price = Long.parseLong(args[1]);
            getCollectionManager().countGreaterThanPrice(price);
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "count_greater_than_price price : вывести количество элементов,
     *         значение поля price которых больше заданного" - command description
     *         in Russian
     */
    public String getDescription() {
        return "count_greater_than_price price : вывести количество элементов, значение поля price которых больше заданного";
    }
}