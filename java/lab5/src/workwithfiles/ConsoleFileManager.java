package src.workwithfiles;

import java.util.Scanner;

import src.baseabstractions.ReaderWriter;
import src.console.CommandManager;

/**
 * File-based implementation of the ReaderWriter interface.
 * Provides file input/output operations using Scanner and validates commands
 * for script execution.
 */
public class ConsoleFileManager implements ReaderWriter {
    private final Scanner scanner;

    /**
     * Constructs a ConsoleFileManager with the specified scanner.
     *
     * @param scanner the scanner to use for reading file input
     */
    public ConsoleFileManager(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Returns the scanner used for reading input.
     *
     * @return the Scanner instance
     */
    public Scanner getScanner() {
        return this.scanner;
    }

    /**
     * Reads an integer value from file input.
     *
     * @return the read Integer value
     */
    public Integer readInt() {
        Integer result = Integer.valueOf(getScanner().nextLine().trim());
        return result;
    }

    /**
     * Reads a long value from file input.
     *
     * @return the read Long value
     */
    public Long readLong() {
        Long result = Long.valueOf(getScanner().nextLine().trim());
        return result;
    }

    /**
     * Reads a double value from file input.
     *
     * @return the read Double value
     */
    public Double readDouble() {
        Double result = Double.valueOf(getScanner().nextLine().trim());
        return result;
    }

    /**
     * Reads a line of text from file input.
     *
     * @return the read line as a String, trimmed of leading/trailing whitespace
     */
    public String readLine() {
        String result = getScanner().nextLine().trim();
        return result;
    }

    /**
     * Writes the specified text to console output.
     *
     * @param text the text to be written to System.out
     */
    public void write(String text) {
        System.out.print(text);
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
        String[] commandAndArgument = getScanner().nextLine().trim().toLowerCase().split(" ");
        String command = commandAndArgument[0].trim();
        if (CommandManager.getCommands().containsKey(command)) {
            if (command.equals("insert") || command.equals("update")
                    || command.equals("remove_key") || command.equals("execute_script")
                    || command.equals("replace_if_greater") || command.equals("remove_greater_key")
                    || command.equals("count_greater_than_price")) {
                if (commandAndArgument.length == 2) {
                    return new String[] { command, commandAndArgument[1].trim() };
                } else
                    throw new IllegalArgumentException("Number of arguments is wrong.");
            } else {
                if (commandAndArgument.length == 1) {
                    return new String[] { command };
                } else
                    throw new IllegalArgumentException("Number of arguments is wrong.");
            }
        } else
            throw new IllegalArgumentException("Command not found.");
    }
}