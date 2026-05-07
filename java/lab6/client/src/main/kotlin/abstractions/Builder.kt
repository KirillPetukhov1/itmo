package abstractions

/**
 * Generic builder pattern interface.
 *
 * @param T the type of object to build
 */
interface Builder<T> {

    /**
     * Resets the builder to its initial state so a new object can be constructed.
     */
    fun reset()

    /**
     * Constructs and returns the built object.
     *
     * @return the newly created object
     */
    fun build(): T
}
