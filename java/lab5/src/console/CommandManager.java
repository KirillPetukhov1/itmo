package src.console;

import java.util.HashMap;
import java.util.Scanner;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;
import src.commands.*;

public class CommandManager<T extends Product> {
    private boolean isWorking = true;
    private static HashMap<String, Command<? extends Product>> commands = new HashMap<>();

    public CommandManager(CollectionManager<T> collectionManager) {
        commands.put("help", new HelpCommand(collectionManager));
        commands.put("info", new InfoCommand(collectionManager));
        commands.put("show", new ShowCommand(collectionManager));
        commands.put("add", new AddCommand(collectionManager));
        commands.put("update", new UpdateIdCommand(collectionManager));
        commands.put("remove_by_id", new RemoveByIdCommand(collectionManager));
        commands.put("clear", new ClearCommand(collectionManager));
        commands.put("save", new SaveCommand(collectionManager));
        commands.put("execute_script", new ExecuteScriptCommand(collectionManager));
        commands.put("exit", new ExitCommand(collectionManager));
        commands.put("add_if_max", new AddIfMaxCommand(collectionManager));
        commands.put("add_if_min", new AddIfMinCommand(collectionManager));
        commands.put("remove_lower", new RemoveLowerCommand(collectionManager));
        commands.put("sum_of_number_of_participants", new SumOfNumberOfParticipantsCommand(collectionManager));
        commands.put("filter_less_than_number_of_participants",
                new FilterLessThanNumberOfParticipantsCommand(collectionManager));
        commands.put("print_descending", new PrintDescendingCommand(collectionManager));
    }

    public static HashMap<String, Command<? extends Product>> getCommands() {
        return commands;
    }

    public boolean getWork() {
        return this.isWorking;
    }

    public void existCommand() {
        Scanner scanner = new Scanner(System.in);
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
            System.out.println("Something went wrong. " + e.getMessage() + ". See you next time.");
            this.isWorking = false;
            System.exit(0);
        }
    }
}
