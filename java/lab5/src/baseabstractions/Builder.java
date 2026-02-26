package src.baseabstractions;

public interface Builder<T> {

    void reset();

    T create();
}
