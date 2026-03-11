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

    public CommandManager(CollectionManager<K, V> collectionManager) {
        commands.put("help", new HelpCommand<K, V>(collectionManager));
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
        Scanner scanner = new Scanner(System.in);
        try (scanner) {
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
