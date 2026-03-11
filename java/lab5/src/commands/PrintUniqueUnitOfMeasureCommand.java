package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

public class PrintUniqueUnitOfMeasureCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {
        
    public PrintUniqueUnitOfMeasureCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    public void execute(String[] args) {
        if (args.length == 1) {
            getCollectionManager().printUniqueUnitOfMeasure();
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    public String getDescription() {
        return "print_unique_unit_of_measure : вывести уникальные значения поля unitOfMeasure всех элементов в коллекции";
    }
}
