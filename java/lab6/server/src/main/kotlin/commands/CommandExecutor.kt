package commands

import connection.Request
import connection.Response

/**
 * Invoker in the Command pattern.
 * Delegates request dispatch entirely to [CommandRegistry] and executes the resulting command.
 * Has no knowledge of concrete command types, payload structure, or collection operations -- ISP, SRP.
 *
 * @property registry the registry that maps command names to [abstractions.CommandFactory] instances
 */
class CommandExecutor(private val registry: CommandRegistry) {

    /**
     * Creates the appropriate [abstractions.ServerCommand] for [request] via [registry]
     * and executes it.
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
