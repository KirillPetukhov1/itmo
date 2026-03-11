package src.baseabstractions;

import src.basecollection.CollectionManager;
import src.baseobjects.Product;

public abstract class Command<K extends Comparable<K>, V extends Product> {
    CollectionManager<K, V> collectionManager;

    public Command(CollectionManager<K, V> collectionManager) {
        this.collectionManager = collectionManager;
    }

    public CollectionManager<K, V> getCollectionManager() {
        return collectionManager;
    }

    public abstract void execute(String[] args);

    public abstract String getDescription();
}
