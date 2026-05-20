package abstractions

/**
 * Abstraction over the input/output channel used by commands and forms.
 * Concrete implementations may read from the console or from a script file.
 */
abstract class AbstractReaderWriter {

    /**
     * Reads a non-null string from the underlying source.
     *
     * @return the next line of input
     */
    abstract fun readLine(): String

    /**
     * Reads a [Long] value, throwing [NumberFormatException] on invalid input.
     *
     * @return the parsed long
     */
    fun readLong(): Long = readLine().trim().toLong()

    /**
     * Reads a [Double] value, throwing [NumberFormatException] on invalid input.
     *
     * @return the parsed double
     */
    fun readDouble(): Double = readLine().trim().toDouble()

    /**
     * Writes [text] to the underlying output.
     *
     * @param text the message to display
     */
    abstract fun write(text: String)
}
