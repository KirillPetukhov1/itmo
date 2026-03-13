package src.workwithfiles;

import java.util.Scanner;

import src.baseabstractions.ReaderWriter;
import src.console.CommandManager;

public class ConsoleFileManager implements ReaderWriter {
    private final Scanner scanner;

    public ConsoleFileManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public Scanner getScanner() {
        return this.scanner;
    }

    public Integer readInt() {
        return getScanner().nextInt();
    }

    public Long readLong() {
        return getScanner().nextLong();
    }

    public Double readDouble() {
        return getScanner().nextDouble();
    }

    public String readLine() {
        return getScanner().next().trim();
    }

    public void write(String text) {
        System.out.print(text);
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
}
