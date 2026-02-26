package src.baseabstractions;

public abstract class Director<T extends Builder<?>> {
    private T builder;

    public Director(T builder) {
        this.builder = builder;
    }

    public void changeBuilder(T builder) {
        this.builder = builder;
    }

    public T getBuilder() {
        return builder;
    }
}
