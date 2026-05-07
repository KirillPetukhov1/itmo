package abstractions

/**
 * Abstraction over the input/output channel used by commands and forms.
 * Concrete implementations may read from the console or from a script file.
 *
 * Output is split into two categories:
 * prompts ([writeRequest]) are shown only in interactive mode and suppressed in script mode;
 * errors ([writeError]) are always shown regardless of the source.
 */
abstract class AbstractReaderWriter {

    /**
     * Reads the next line of input from the underlying source.
     * Returns null when the end of input is reached (EOF or Ctrl+D on the console).
     *
     * @return the next line, or null on EOF
     */
    abstract fun readLine(): String?

    /**
     * Reads a [Long] value, throwing [NumberFormatException] on invalid input
     * or [EofException] when EOF is reached.
     *
     * @return the parsed long
     */
    fun readLong(): Long = readLine()?.trim()?.toLong() ?: throw EofException()

    /**
     * Reads a [Double] value, throwing [NumberFormatException] on invalid input
     * or [EofException] when EOF is reached.
     *
     * @return the parsed double
     */
    fun readDouble(): Double = readLine()?.trim()?.toDouble() ?: throw EofException()

    /**
     * Writes a prompt or informational message.
     * In script mode this output is suppressed; use [writeError] for messages that must always appear.
     *
     * @param text the prompt text to display
     */
    abstract fun writeRequest(text: String)

    /**
     * Writes an error or diagnostic message that must always be shown,
     * even when reading from a script file.
     *
     * @param text the error message to display
     */
    abstract fun writeError(text: String)

    /**
     * Convenience alias for [writeRequest].
     *
     * @param text the message to display
     */
    fun write(text: String) = writeRequest(text)
}
