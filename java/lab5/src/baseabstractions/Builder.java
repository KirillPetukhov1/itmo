package src.baseabstractions;

/**
 * Interface for the builder pattern, providing methods to construct objects.
 *
 * @param <T> the type of object that this builder creates
 */
public interface Builder<T> {

    /**
     * Resets the builder to its initial state.
     * Clears all previously set fields and prepares for building a new object.
     */
    void reset();

    /**
     * Creates and returns the built object.
     *
     * @return the constructed object of type T
     */
    T create();
}