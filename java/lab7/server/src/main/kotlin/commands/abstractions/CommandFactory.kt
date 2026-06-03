package commands.abstractions

/**
 * Functional interface that creates a [ServerCommand] from a raw serialized payload
 * and the authenticated user's login.
 *
 * Separates command instantiation from command execution following the Single Responsibility
 * Principle. Each concrete factory is registered in [commands.CommandRegistry] under a command
 * name string.
 */
fun interface CommandFactory {

    /**
     * Creates a [ServerCommand] ready to be executed.
     *
     * @param payload the serialized JSON arguments string from the incoming [connection.Request]
     * @param login the authenticated user's login, forwarded to commands that enforce ownership
     * @return the constructed [ServerCommand]
     */
    fun create(payload: String, login: String): ServerCommand
}