package src.console;

import java.util.Scanner;

import src.baseabstractions.ReaderWriter;

/**
 * Console implementation of the ReaderWriter interface.
 * Provides standard console input/output operations using Scanner and System.out.
 */
public class ConsoleManager implements ReaderWriter {
    private final Scanner scanner;

    /**
     * Constructs a ConsoleManager with the specified scanner.
     *
     * @param scanner the scanner to use for reading console input
     */
    public ConsoleManager(Scanner scanner) {
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
     * Reads an integer value from console input.
     *
     * @return the read Integer value
     */
    public Integer readInt() {
        Integer result = Integer.valueOf(getScanner().nextLine().trim());
        return result;
    }

    /**
     * Reads a long value from console input.
     *
     * @return the read Long value
     */
    public Long readLong() {
        long result = Long.valueOf(getScanner().nextLine().trim());
        return result;
    }

    /**
     * Reads a double value from console input.
     *
     * @return the read Double value
     */
    public Double readDouble() {
        Double result = Double.valueOf(getScanner().nextLine().trim());
        return result;
    }

    /**
     * Reads a line of text from console input.
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
}