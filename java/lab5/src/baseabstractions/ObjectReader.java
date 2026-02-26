package src.baseabstractions;

public abstract class ObjectReader<T> {
    private Builder<T> builder;
    private ReaderWriter readerWriter;

    public ObjectReader(Builder<T> builder, ReaderWriter readerWriter) {
        this.builder = builder;
        this.readerWriter = readerWriter;
    }

    public Builder<T> getBuilder() {
        return builder;
    }

    public ReaderWriter getReaderWriter() {
        return readerWriter;
    }
}
