package src.baseabstractions;

public abstract class AbstractFileManager<T> {
    private String fileName;

    public AbstractFileManager() {
    }

    public AbstractFileManager(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public abstract void save(T objects) throws Exception;

    public abstract T load() throws Exception;
}
