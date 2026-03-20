package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to update an element in the collection by its ID.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class UpdateCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs an UpdateCommand with the specified collection manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public UpdateCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the update command.
     * Updates the value of the collection element with the specified ID.
     *
     * @param args command arguments (expects command name and ID value)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 2) {
            long id = Long.parseLong(args[1]);
            getCollectionManager().update(id);
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "update id {element} : обновить значение элемента коллекции, id
     *         которого равен заданному" - command description in Russian
     */
    public String getDescription() {
        return "update id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }
}