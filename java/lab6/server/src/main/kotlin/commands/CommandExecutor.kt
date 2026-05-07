package commands

import connection.Request
import connection.Response

/**
 * Dispatches incoming [Request] objects to [ServerCommand] instances created by [CommandRegistry].
 *
 * This class is the Invoker in the Command pattern: it knows nothing about the concrete
 * commands, their arguments, or the collection — that logic lives entirely in the
 * [ServerCommand] implementations and [CommandRegistry].
 *
 * @property registry the registry that maps command names to factory functions
 */
class CommandExecutor(private val registry: CommandRegistry) {

    /**
     * Creates the appropriate [ServerCommand] for [request] via [registry] and executes it.
     *
     * @param request the request received from a client
     * @return a [Response] carrying the result or an error message
     */
    fun execute(request: Request): Response =
        try {
            registry.create(request.commandName, request.serializedCommand).execute()
        } catch (e: Exception) {
            Response(
                commandName = request.commandName,
                serializedResult = "{}",
                errorMessage = e.message ?: "Unknown error"
            )
        }
}
