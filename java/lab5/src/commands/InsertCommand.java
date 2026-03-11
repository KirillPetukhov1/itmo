package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

public class InsertCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    public InsertCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    public void execute(String[] args) {
        if (args.length == 2) {
            K key = getCollectionManager().getKeyParser().getParsedObject(args[1]);
            getCollectionManager().insert(key);
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    public String getDescription() {
        return "insert null {element} : добавить новый элемент с заданным ключом";
    }
}
