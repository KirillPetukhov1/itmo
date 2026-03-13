package src.console;

import java.util.HashMap;
import java.util.Scanner;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;
import src.commands.*;

public class CommandManager<K extends Comparable<K>, V extends Product> {
    private boolean isWorking = true;
    private static HashMap<String, Command<?, ? extends Product>> commands = new HashMap<>();
    Scanner scanner;

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

    public static HashMap<String, Command<?, ? extends Product>> getCommands() {
        return commands;
    }

    public static <K extends Comparable<K>, V extends Product> void addCommand(String key, Command<K, V> value) {
        commands.put(key, value);
    }

    public static void removeCommand(String key) {
        commands.remove(key);
    }

    public boolean getWork() {
        return this.isWorking;
    }

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
