package src.baseabstractions;

/**
 * Abstract base class for reading and constructing objects.
 * Combines a builder with a reader/writer for object creation.
 *
 * @param <T> the type of object to be read and constructed
 */
public abstract class ObjectReader<T> {
    private Builder<T> builder;
    private ReaderWriter readerWriter;

    /**
     * Constructs an ObjectReader with the specified builder and reader/writer.
     *
     * @param builder the builder used to construct objects of type T
     * @param readerWriter the reader/writer used for input/output operations
     */
    public ObjectReader(Builder<T> builder, ReaderWriter readerWriter) {
        this.builder = builder;
        this.readerWriter = readerWriter;
    }

    /**
     * Returns the builder used for object construction.
     *
     * @return the builder instance
     */
    public Builder<T> getBuilder() {
        return builder;
    }

    /**
     * Returns the reader/writer used for I/O operations.
     *
     * @return the reader/writer instance
     */
    public ReaderWriter getReaderWriter() {
        return readerWriter;
    }
}