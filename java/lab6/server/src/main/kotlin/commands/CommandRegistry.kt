package commands

import collection.CollectionManager
import commands.abstractions.CommandFactory
import commands.abstractions.ServerCommand
import commands.implementations.*

/**
 * Registry that maps command name strings to [CommandFactory] instances.
 * Each factory is responsible for creating one type of [ServerCommand].
 *
 * New commands are registered via [register] and looked up via [create].
 * Open for extension (new register calls), closed for modification -- OCP.
 *
 * @property collectionManager the collection passed to every command factory
 */
class CommandRegistry(private val collectionManager: CollectionManager) {

    private val factories: MutableMap<String, CommandFactory> = mutableMapOf()

    init {
        register("info",                       CommandFactory { InfoServerCommand(collectionManager) })
        register("show",                       CommandFactory { ShowServerCommand(collectionManager) })
        register("insert",                     CommandFactory { InsertServerCommand(collectionManager, it) })
        register("update",                     CommandFactory { UpdateServerCommand(collectionManager, it) })
        register("remove_key",                 CommandFactory { RemoveKeyServerCommand(collectionManager, it) })
        register("clear",                      CommandFactory { ClearServerCommand(collectionManager) })
        register("exit",                       CommandFactory { ExitServerCommand() })
        register("remove_lower",               CommandFactory { RemoveLowerServerCommand(collectionManager, it) })
        register("replace_if_greater",         CommandFactory { ReplaceIfGreaterServerCommand(collectionManager, it) })
        register("remove_greater_key",         CommandFactory { RemoveGreaterKeyServerCommand(collectionManager, it) })
        register("count_greater_than_price",   CommandFactory { CountGreaterThanPriceServerCommand(collectionManager, it) })
        register("print_unique_unit_of_measure", CommandFactory { PrintUniqueUnitOfMeasureServerCommand(collectionManager) })
        register("print_field_descending_price", CommandFactory { PrintFieldDescendingPriceServerCommand(collectionManager) })
    }

    /**
     * Registers a [factory] for [commandName], replacing any existing registration.
     *
     * @param commandName the name clients use to identify the command
     * @param factory the factory that creates the [ServerCommand] for this name
     */
    fun register(commandName: String, factory: CommandFactory) {
        factories[commandName] = factory
    }

    /**
     * Creates a [ServerCommand] for [commandName] by invoking the registered [CommandFactory]
     * with [payload].
     *
     * @param commandName the command identifier from the incoming [connection.Request]
     * @param payload the serialized arguments string from the [connection.Request]
     * @return the constructed [ServerCommand], ready to execute
     * @throws IllegalArgumentException if [commandName] has no registered factory
     */
    fun create(commandName: String, payload: String): ServerCommand =
        (factories[commandName] ?: throw IllegalArgumentException("Unknown command: $commandName"))
            .create(payload)
}
