package src.baseabstractions;

/**
 * Abstract base class for file management operations.
 * Provides common file handling functionality for saving and loading objects.
 *
 * @param <T> the type of objects to be saved to or loaded from file
 */
public abstract class AbstractFileManager<T> {
    private String fileName;

    /**
     * Default constructor for AbstractFileManager.
     */
    public AbstractFileManager() {
    }

    /**
     * Constructs an AbstractFileManager with the specified file name.
     *
     * @param fileName the name of the file to manage
     */
    public AbstractFileManager(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the current file name.
     *
     * @return the name of the managed file
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Sets the file name.
     *
     * @param fileName the new file name to manage
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Abstract method to save objects to the file.
     *
     * @param objects the objects to be saved
     * @throws Exception if an error occurs during save operation
     */
    public abstract void save(T objects) throws Exception;

    /**
     * Abstract method to load objects from the file.
     *
     * @return the loaded objects of type T
     * @throws Exception if an error occurs during load operation
     */
    public abstract T load() throws Exception;
}