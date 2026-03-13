package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

public class ClearCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {
        
    public ClearCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    public void execute(String[] args) {
        if (args.length == 1) {
            getCollectionManager().clear();
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    public String getDescription() {
        return "clear : очистить коллекцию";
    }
}
