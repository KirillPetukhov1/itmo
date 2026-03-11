package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

public class RemoveGreaterKeyCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {
        
    public RemoveGreaterKeyCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    public void execute(String[] args) {
        if (args.length == 2) {
            K key = getCollectionManager().getKeyParser().getParsedObject(args[1]);
            getCollectionManager().removeGreaterKey(key);
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    public String getDescription() {
        return "";
    }
}
