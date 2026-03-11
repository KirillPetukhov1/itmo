package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

public class UpdateCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {
        
    public UpdateCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    public void execute(String[] args) {
        if (args.length == 2) {
            long id = Long.parseLong(args[1]);
            getCollectionManager().update(id);
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    public String getDescription() {
        return "update id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }
}
