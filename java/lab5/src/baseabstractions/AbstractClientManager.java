package src.baseabstractions;

/**
 * Abstract base class for client managers.
 * Provides common functionality for managing ReaderWriter instances.
 */
public abstract class AbstractClientManager {
    private ReaderWriter readerWriter;

    /**
     * Constructs an AbstractClientManager with the specified ReaderWriter.
     *
     * @param readerWriter the ReaderWriter instance to be used for I/O operations
     */
    public AbstractClientManager(ReaderWriter readerWriter) {
        this.readerWriter = readerWriter;
    }

    /**
     * Returns the current ReaderWriter instance.
     *
     * @return the ReaderWriter used for I/O operations
     */
    public ReaderWriter getReaderWriter() {
        return readerWriter;
    }

    /**
     * Sets the ReaderWriter instance.
     *
     * @param readerWriter the new ReaderWriter to be used for I/O operations
     */
    public void setReaderWriter(ReaderWriter readerWriter) {
        this.readerWriter = readerWriter;
    }
}