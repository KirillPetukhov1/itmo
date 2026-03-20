package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

/**
 * Command to display help information for all available commands.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class HelpCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {

    /**
     * Constructs a HelpCommand with the specified collection manager.
     *
     * @param collectionManager the collection manager to operate on
     */
    public HelpCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    /**
     * Executes the help command.
     * Displays descriptions of all available commands.
     *
     * @param args command arguments (expects exactly one argument - the command
     *             name)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 1) {
            getCollectionManager().help();
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "help : вывести справку по доступным командам" - command description
     *         in Russian
     */
    public String getDescription() {
        return "help : вывести справку по доступным командам";
    }
}