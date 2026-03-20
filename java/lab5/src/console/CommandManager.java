package src.console;

import java.util.HashMap;
import java.util.Scanner;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;
import src.commands.*;

/**
 * Manages the registration and execution of console commands.
 * Provides a command registry and handles user input processing.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class CommandManager<K extends Comparable<K>, V extends Product> {
    private boolean isWorking = true;
    private static HashMap<String, Command<?, ? extends Product>> commands = new HashMap<>();
    Scanner scanner;

    /**
     * Constructs a CommandManager with the specified collection manager and
     * scanner.
     * Initializes and registers all available commands.
     *
     * @param collectionManager the collection manager to operate on
     * @param scanner           the scanner for reading user input
     */
    public CommandManager(CollectionManager<K, V> collectionManager, Scanner scanner) {
        this.scanner = scanner;

        commands.put("help", new HelpCommand<K, V>(collectionManager));
        commands.put("info", new InfoCommand<K, V>(collectionManager));
        commands.put("show", new ShowCommand<K, V>(collectionManager));
        commands.put("insert", new InsertCommand<K, V>(collectionManager));
        commands.put("update", new UpdateCommand<K, V>(collectionManager));
        commands.put("remove_key", new RemoveKeyCommand<K, V>(collectionManager));
        commands.put("clear", new ClearCommand<K, V>(collectionManager));
        commands.put("save", new SaveCommand<K, V>(collectionManager));
        commands.put("exit", new ExitCommand<K, V>(collectionManager));
        commands.put("remove_lower", new RemoveLowerCommand<K, V>(collectionManager));
        commands.put("replace_if_greater", new ReplaceIfGreaterCommand<K, V>(collectionManager));
        commands.put("remove_greater_key", new RemoveGreaterKeyCommand<K, V>(collectionManager));
        commands.put("count_greater_than_price", new CountGreaterThanPriceCommand<K, V>(collectionManager));
        commands.put("print_unique_unit_of_measure", new PrintUniqueUnitOfMeasureCommand<K, V>(collectionManager));
        commands.put("print_field_descending_price", new PrintFieldDescendingPriceCommand<K, V>(collectionManager));
    }

    /**
     * Returns the map of registered commands.
     *
     * @return HashMap containing command names as keys and Command objects as
     *         values
     */
    public static HashMap<String, Command<?, ? extends Product>> getCommands() {
        return commands;
    }

    /**
     * Adds a new command to the registry.
     *
     * @param key   the command name
     * @param value the Command object to register
     * @param <K>   the key type of the collection
     * @param <V>   the value type of the collection
     */
    public static <K extends Comparable<K>, V extends Product> void addCommand(String key, Command<K, V> value) {
        commands.put(key, value);
    }

    /**
     * Removes a command from the registry.
     *
     * @param key the command name to remove
     */
    public static void removeCommand(String key) {
        commands.remove(key);
    }

    /**
     * Returns the working state of the command manager.
     *
     * @return true if the manager is working, false otherwise
     */
    public boolean getWork() {
        return this.isWorking;
    }

    /**
     * Processes a single user command.
     * Reads input from the scanner, parses it, and executes the corresponding
     * command.
     * Handles command not found errors and execution exceptions.
     */
    public void existCommand() {
        try {
            System.out.flush();
            System.out.println("Введите команду: ");
            String command = scanner.nextLine().trim().toLowerCase();
            String[] args = command.split(" ");
            if (commands.containsKey(args[0])) {
                try {
                    commands.get(args[0]).execute(args);
                } catch (IllegalArgumentException e) {
                    System.out.println("Что-то пошло не так. " + e.getMessage() + "Попробуйте еще раз.");
                }
            } else {
                System.out.println("Команда \"" + args[0] + "\" не найдена.");
            }
        } catch (Exception e) {
            System.out.println("Something went wrong. " + e.getMessage());
            this.isWorking = false;
            System.exit(0);
        }
    }
}