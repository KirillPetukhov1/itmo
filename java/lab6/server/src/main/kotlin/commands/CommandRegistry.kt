package commands

import collection.CollectionManager

/**
 * Registry that maps command name strings to factory functions.
 * Each factory receives the raw serialized payload string from the [connection.Request]
 * and produces a ready-to-execute [ServerCommand] instance.
 *
 * New commands are registered via [register] and looked up via [create].
 *
 * @property collectionManager the collection passed to every command factory
 */
class CommandRegistry(private val collectionManager: CollectionManager) {

    private val factories: MutableMap<String, (String) -> ServerCommand> = mutableMapOf()

    init {
        register("info") { InfoServerCommand(collectionManager) }
        register("show") { ShowServerCommand(collectionManager) }
        register("insert") { payload -> InsertServerCommand(collectionManager, payload) }
        register("update") { payload -> UpdateServerCommand(collectionManager, payload) }
        register("remove_key") { payload -> RemoveKeyServerCommand(collectionManager, payload) }
        register("clear") { ClearServerCommand(collectionManager) }
        register("exit") { ExitServerCommand() }
        register("remove_lower") { payload -> RemoveLowerServerCommand(collectionManager, payload) }
        register("replace_if_greater") { payload -> ReplaceIfGreaterServerCommand(collectionManager, payload) }
        register("remove_greater_key") { payload -> RemoveGreaterKeyServerCommand(collectionManager, payload) }
        register("count_greater_than_price") { payload -> CountGreaterThanPriceServerCommand(collectionManager, payload) }
        register("print_unique_unit_of_measure") { PrintUniqueUnitOfMeasureServerCommand(collectionManager) }
        register("print_field_descending_price") { PrintFieldDescendingPriceServerCommand(collectionManager) }
    }

    /**
     * Registers a factory for [commandName], replacing any existing registration.
     *
     * @param commandName the name clients use to identify the command
     * @param factory a lambda that receives the serialized payload and returns a [ServerCommand]
     */
    fun register(commandName: String, factory: (String) -> ServerCommand) {
        factories[commandName] = factory
    }

    /**
     * Creates a [ServerCommand] for [commandName] by invoking the registered factory with [payload].
     *
     * @param commandName the command identifier from the incoming [connection.Request]
     * @param payload the serialized arguments string from the [connection.Request]
     * @return the constructed [ServerCommand], ready to [ServerCommand.execute]
     * @throws IllegalArgumentException if [commandName] has no registered factory
     */
    fun create(commandName: String, payload: String): ServerCommand {
        val factory = factories[commandName]
            ?: throw IllegalArgumentException("Unknown command: $commandName")
        return factory(payload)
    }
}
