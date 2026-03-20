package src.baseabstractions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import src.console.CommandManager;

/**
 * Abstract base class for reading commands from a file.
 * Provides file reading functionality and command parsing for console
 * operations.
 *
 * @param <T> the type of object that will be built from the parsed commands
 */
public abstract class AbstractFileConsoleReader<T> {
    private final Scanner scanner;

    /**
     * Constructs an AbstractFileConsoleReader for the specified file.
     *
     * @param fileName the name of the file to read from
     * @throws FileNotFoundException if the specified file does not exist
     */
    public AbstractFileConsoleReader(String fileName) throws FileNotFoundException {
        this.scanner = new Scanner(new File(fileName));
    }

    /**
     * Returns the scanner used for file reading.
     *
     * @return the scanner instance for this file reader
     */
    public Scanner getScanner() {
        return this.scanner;
    }

    /**
     * Reads and parses the next command and its argument from the file.
     * Validates the command against available commands and checks argument
     * requirements.
     *
     * @return a String array containing the command and optionally its argument
     * @throws IllegalArgumentException if the command is unknown or argument count
     *                                  is incorrect
     */
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

    /**
     * Abstract method to be implemented by subclasses for building the main object.
     *
     * @return the built object of type T
     */
    public abstract T getMainBuilder();
}