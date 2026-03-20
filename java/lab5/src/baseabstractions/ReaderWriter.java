package src.baseabstractions;

import java.util.Scanner;

/**
 * Interface for basic input/output operations.
 * Provides methods for reading various data types and writing text.
 */
public interface ReaderWriter {
    /**
     * Returns the scanner used for reading input.
     *
     * @return the Scanner instance
     */
    Scanner getScanner();

    /**
     * Reads an integer value from input.
     *
     * @return the read Integer, or null if input is invalid
     */
    Integer readInt();

    /**
     * Reads a long value from input.
     *
     * @return the read Long, or null if input is invalid
     */
    Long readLong();

    /**
     * Reads a double value from input.
     *
     * @return the read Double, or null if input is invalid
     */
    Double readDouble();

    /**
     * Reads a line of text from input.
     *
     * @return the read line as a String
     */
    String readLine();

    /**
     * Writes the specified text to output.
     *
     * @param text the text to be written
     */
    void write(String text);
}