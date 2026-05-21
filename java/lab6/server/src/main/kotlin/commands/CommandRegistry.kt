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
        register("info", object : CommandFactory {
            override fun create(payload: String): ServerCommand =
                InfoServerCommand(collectionManager)
        })
        register("show", object : CommandFactory {
            override fun create(payload: String): ServerCommand =
                ShowServerCommand(collectionManager)
        })
        register("insert", object : CommandFactory {
            override fun create(payload: String): ServerCommand =
                InsertServerCommand(collectionManager, payload)
        })
        register("update", object : CommandFactory {
            override fun create(payload: String): ServerCommand =
                UpdateServerCommand(collectionManager, payload)
        })
        register("remove_key", object : CommandFactory {
            override fun create(payload: String): ServerCommand =
                RemoveKeyServerCommand(collectionManager, payload)
        })
        register("clear", object : CommandFactory {
            override fun create(payload: String): ServerCommand =
                ClearServerCommand(collectionManager)
        })
        register("exit", object : CommandFactory {
            override fun create(payload: String): ServerCommand =
                ExitServerCommand()
        })
        register("remove_lower", object : CommandFactory {
            override fun create(payload: String): ServerCommand =
                RemoveLowerServerCommand(collectionManager, payload)
        })
        register("replace_if_greater", object : CommandFactory {
            override fun create(payload: String): ServerCommand =
                ReplaceIfGreaterServerCommand(collectionManager, payload)
        })
        register("remove_greater_key", object : CommandFactory {
            override fun create(payload: String): ServerCommand =
                RemoveGreaterKeyServerCommand(collectionManager, payload)
        })
        register("count_greater_than_price", object : CommandFactory {
            override fun create(payload: String): ServerCommand =
                CountGreaterThanPriceServerCommand(collectionManager, payload)
        })
        register("print_unique_unit_of_measure", object : CommandFactory {
            override fun create(payload: String): ServerCommand =
                PrintUniqueUnitOfMeasureServerCommand(collectionManager)
        })
        register("print_field_descending_price", object : CommandFactory {
            override fun create(payload: String): ServerCommand =
                PrintFieldDescendingPriceServerCommand(collectionManager)
        })
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
        (factories[commandName]
            ?: throw IllegalArgumentException("Unknown command: $commandName"))
            .create(payload)
}