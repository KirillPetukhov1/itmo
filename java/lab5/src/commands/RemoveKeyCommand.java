package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

public class RemoveKeyCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {
        
    public RemoveKeyCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    public void execute(String[] args) {
        if (args.length == 2) {
            K key = getCollectionManager().getKeyParser().getParsedObject(args[1]);
            getCollectionManager().removeKey(key);
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    public String getDescription() {
        return "remove_key null : удалить элемент из коллекции по его ключу";
    }
}
