package console

import abstractions.AbstractReaderWriter

/**
 * Reads from [System.in] and writes to [System.out].
 * Used during interactive console sessions.
 * Both requests and errors are printed normally.
 * Returns null from [readLine] when Ctrl+D (EOF) is received.
 */
class ConsoleReaderWriter : AbstractReaderWriter() {

    override fun readLine(): String? = kotlin.io.readLine()

    override fun writeRequest(text: String) = println(text)

    override fun writeError(text: String) = println(text)
}
