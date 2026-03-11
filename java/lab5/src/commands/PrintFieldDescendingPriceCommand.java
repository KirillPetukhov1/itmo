package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

public class PrintFieldDescendingPriceCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {
        
    public PrintFieldDescendingPriceCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    public void execute(String[] args) {
        if (args.length == 1) {
            getCollectionManager().printFieldDescendingPrice();
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    public String getDescription() {
        return "print_field_descending_price : вывести значения поля price всех элементов в порядке убывания";
    }
}
