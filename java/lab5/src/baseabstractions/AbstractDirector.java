package src.baseabstractions;

/**
 * Abstract base class for directors in the builder pattern.
 * Manages a builder instance of a specific type.
 *
 * @param <T> the type of builder this director works with, extending Builder
 */
public abstract class AbstractDirector<T extends Builder<?>> {
    private T builder;

    /**
     * Constructs an AbstractDirector with the specified builder.
     *
     * @param builder the builder instance to be used by this director
     */
    public AbstractDirector(T builder) {
        this.builder = builder;
    }

    /**
     * Changes the current builder instance.
     *
     * @param builder the new builder instance to be used
     */
    public void changeBuilder(T builder) {
        this.builder = builder;
    }

    /**
     * Returns the current builder instance.
     *
     * @return the builder used by this director
     */
    public T getBuilder() {
        return builder;
    }
}