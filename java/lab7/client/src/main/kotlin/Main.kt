import commands.ExecuteScriptCommand
import manager.CommandManager
import connection.TcpClient
import console.ConsoleReaderWriter
import manager.CommandClient
import manager.Session
import kotlin.collections.set

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
    val session = Session()
    val commandManager = CommandManager(session, readerWriter)
    val tcpClient = TcpClient(host, port)
    val commandClient = CommandClient(session, tcpClient, readerWriter, commandManager)
    commandManager.commands["execute_script"] = ExecuteScriptCommand(commandClient)

    readerWriter.write("Connected to server at $host:$port")
    readerWriter.write("Type 'help' to see available commands.")

    try {
        commandClient.run()
    } catch (e: Exception) {
        readerWriter.write("Unexpected error: ${e.message}")
    }
}
