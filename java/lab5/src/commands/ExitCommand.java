package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

public class ExitCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {
        
    public ExitCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    public void execute(String[] args) {
        if (args.length == 1) {
            getCollectionManager().exit();
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    public String getDescription() {
        return "exit : завершить программу (без сохранения в файл)";
    }
}
