package src.baseabstractions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import src.console.CommandManager;

public abstract class AbstractFileConsoleReader<T> {
    private final Scanner scanner;

    public AbstractFileConsoleReader(String fileName) throws FileNotFoundException {
        this.scanner = new Scanner(new File(fileName));
    }

    public Scanner getScanner() {
        return this.scanner;
    }

    public String[] readCommandAndArgument() throws IllegalArgumentException {
        String[] commandAndArgument = scanner.nextLine().trim().toLowerCase().split(" ");
        String command = commandAndArgument[0].trim();
        if (CommandManager.getCommands().containsKey(command)) {
            if (command.equals("insert") || command.equals("update")
                    || command.equals("remove_key") || command.equals("execute_script")
                    || command.equals("replace_if_greater") || command.equals("remove_greater_key")
                    || command.equals("count_greater_than_price")) {
                if (commandAndArgument.length == 2) {
                    return new String[] { command, commandAndArgument[1].trim() };
                } else
                    throw new IllegalArgumentException("");
            } else {
                if (commandAndArgument.length == 1) {
                    return new String[] { command };
                } else
                    throw new IllegalArgumentException("");
            }
        } else
            throw new IllegalArgumentException("");
    }

    public abstract T getMainBuilder();  
}
