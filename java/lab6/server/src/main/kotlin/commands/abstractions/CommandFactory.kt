package commands.abstractions

/**
 * Factory that creates a [ServerCommand] from a raw serialized payload string.
 *
 * Separates command instantiation (parsing, wiring) from command execution,
 * following the Single Responsibility Principle.
 * Each concrete factory is registered in [commands.CommandRegistry] under a command name.
 */
interface CommandFactory {

    /**
     * Creates a [ServerCommand] ready to be executed.
     *
     * @param payload the serialized JSON arguments string from the incoming [connection.Request]
     * @return the constructed [ServerCommand]
     */
    fun create(payload: String): ServerCommand
}
