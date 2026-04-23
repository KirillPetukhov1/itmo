package abstractions

abstract class AbstractComplicatedObjectReader<T> {
    abstract fun readObject(): T
}