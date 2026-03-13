package src.baseabstractions;

public abstract class AbstractClientManager {
    private ReaderWriter readerWriter;

    public AbstractClientManager(ReaderWriter readerWriter) {
        this.readerWriter = readerWriter;
    }

    public ReaderWriter getReaderWriter() {
        return readerWriter;
    }

    public void setReaderWriter(ReaderWriter readerWriter) {
        this.readerWriter = readerWriter;
    }
}
