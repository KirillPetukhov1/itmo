package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to print all unique unit of measure values from the collection.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class PrintUniqueUnitOfMeasureCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs a PrintUniqueUnitOfMeasureCommand with the specified collection
     * manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public PrintUniqueUnitOfMeasureCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the print unique unit of measure command.
     * Displays all unique unitOfMeasure values present in the collection.
     *
     * @param args command arguments (expects exactly one argument - the command
     *             name)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 1) {
            getCollectionManager().printUniqueUnitOfMeasure();
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "print_unique_unit_of_measure : вывести уникальные значения поля
     *         unitOfMeasure всех элементов в коллекции" - command description in
     *         Russian
     */
    public String getDescription() {
        return "print_unique_unit_of_measure : вывести уникальные значения поля unitOfMeasure всех элементов в коллекции";
    }
}