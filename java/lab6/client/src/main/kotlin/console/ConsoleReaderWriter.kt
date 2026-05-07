package console

import abstractions.AbstractReaderWriter

/**
 * Reads from [System.in] and writes to [System.out].
 * Used during interactive console sessions.
 */
class ConsoleReaderWriter : AbstractReaderWriter() {

    override fun readLine(): String = kotlin.io.readLine() ?: ""

    override fun write(text: String) = println(text)
}
