package src.baseabstractions;

public abstract class AbstractDirector<T extends Builder<?>> {
    private T builder;

    public AbstractDirector(T builder) {
        this.builder = builder;
    }

    public void changeBuilder(T builder) {
        this.builder = builder;
    }

    public T getBuilder() {
        return builder;
    }
}
