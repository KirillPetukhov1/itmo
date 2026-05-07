package console

import abstractions.AbstractReaderWriter
import java.io.BufferedReader

/**
 * Reads script lines from a [BufferedReader] and writes output to [System.out].
 *
 * In script mode two kinds of lines are distinguished:
 *
 * Field value lines — start with the '>' character. The leading '>' is stripped before
 * the value is returned to the caller. These lines supply field values to object-building
 * forms such as [ProductConsoleForm].
 *
 * Command lines — any line that does NOT start with '>'. These are dispatched by
 * [commands.CommandManager] as ordinary commands.
 *
 * When a form calls [readLine] to obtain the next field value and the next available line
 * is a command line (no leading '>'), [CommandInterruptedException] is thrown carrying
 * that command line. The command line is placed into an internal lookahead buffer so
 * [commands.CommandManager] can read it on the next iteration via [readCommandLine].
 *
 * @property reader the source of script lines
 */
class FileReaderWriter(private val reader: BufferedReader) : AbstractReaderWriter() {

    private var lookahead: String? = null

    /**
     * Reads the next field value line.
     * The line must start with '>'; the prefix is stripped and the remainder is returned.
     *
     * @return the field value with the leading '>' removed
     * @throws CommandInterruptedException if the next line is a command line or EOF is reached
     */
    override fun readLine(): String {
        val raw = lookahead?.also { lookahead = null } ?: reader.readLine()
        return when {
            raw == null -> throw CommandInterruptedException(null)
            raw.startsWith(">") -> raw.removePrefix(">").trim()
            else -> {
                lookahead = raw
                throw CommandInterruptedException(raw)
            }
        }
    }

    override fun write(text: String) = println(text)

    /**
     * Returns the next command line, consuming the lookahead buffer first if non-empty.
     * Blank lines are skipped automatically.
     *
     * @return the next non-blank command line, or null at EOF
     */
    fun readCommandLine(): String? {
        while (true) {
            val raw = lookahead?.also { lookahead = null } ?: reader.readLine() ?: return null
            if (raw.isBlank()) continue
            if (!raw.startsWith(">")) return raw
        }
    }

    /**
     * Returns true if there are more lines available from the underlying reader
     * or from the lookahead buffer.
     *
     * @return true when further input can be read
     */
    fun hasNextLine(): Boolean = lookahead != null || reader.ready()
}
