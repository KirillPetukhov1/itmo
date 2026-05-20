package abstractions

/**
 * Thrown when [AbstractReaderWriter.readLine] returns null during field input,
 * signalling that the input stream has ended (EOF or Ctrl+D).
 * Propagates up to [commands.CommandManager] which treats it as a graceful shutdown signal.
 */
class EofException : Exception("End of input stream reached")
