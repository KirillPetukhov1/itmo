package console

/**
 * Thrown by [FileReaderWriter] when a field value is expected (the next line should start with '>')
 * but the next line is a command (does not start with '>') or the file has ended.
 * Signals that the object being constructed is incomplete and the command must be aborted.
 *
 * @param commandLine the line that interrupted the field input, or null if EOF was reached
 */
class CommandInterruptedException(val commandLine: String?) :
    Exception("Object input interrupted: expected a field value line starting with '>'")
