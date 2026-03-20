package src.baseabstractions;

/**
 * Interface for parsing string values into objects of a specific type.
 *
 * @param <T> the type of object resulting from parsing
 */
public interface ParseKey<T> {
    /**
     * Parses a string value and returns the corresponding object.
     *
     * @param value the string value to be parsed
     * @return the parsed object of type T
     */
    T getParsedObject(String value);
}