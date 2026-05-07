import commands.CommandManager
import connection.TcpClient
import console.ConsoleReaderWriter

private const val DEFAULT_HOST = "localhost"
private const val DEFAULT_PORT = 8080
private const val ENV_HOST = "LAB6_HOST"
private const val ENV_PORT = "LAB6_PORT"

/**
 * Application entry point for the client module.
 *
 * Reads the server host from the [ENV_HOST] environment variable (default [DEFAULT_HOST]).
 * Reads the server port from the [ENV_PORT] environment variable (default [DEFAULT_PORT]).
 * Starts the interactive command loop and handles graceful shutdown on EOF.
 */
fun main() {
    val host = System.getenv(ENV_HOST) ?: System.getProperty(ENV_HOST) ?: DEFAULT_HOST
    val port = (System.getenv(ENV_PORT) ?: System.getProperty(ENV_PORT))?.toIntOrNull() ?: DEFAULT_PORT

    val readerWriter = ConsoleReaderWriter()
    val tcpClient = TcpClient(host, port)
    val commandManager = CommandManager(tcpClient, readerWriter)

    readerWriter.write("Connected to server at $host:$port")
    readerWriter.write("Type 'help' to see available commands.")

    try {
        commandManager.run()
    } catch (e: Exception) {
        readerWriter.write("Unexpected error: ${e.message}")
    }
}
