package commands

import auth.AuthService
import collection.CollectionManager
import commands.abstractions.CommandFactory
import commands.abstractions.ServerCommand
import commands.implementations.ClearServerCommand
import commands.implementations.CountGreaterThanPriceServerCommand
import commands.implementations.ExitServerCommand
import commands.implementations.InfoServerCommand
import commands.implementations.InsertServerCommand
import commands.implementations.LoginServerCommand
import commands.implementations.PrintFieldDescendingPriceServerCommand
import commands.implementations.PrintUniqueUnitOfMeasureServerCommand
import commands.implementations.RegisterServerCommand
import commands.implementations.RemoveGreaterKeyServerCommand
import commands.implementations.RemoveKeyServerCommand
import commands.implementations.RemoveLowerServerCommand
import commands.implementations.ReplaceIfGreaterServerCommand
import commands.implementations.ShowServerCommand
import commands.implementations.UpdateServerCommand
import database.DatabaseManager

/**
 * Registry that maps command name strings to [CommandFactory] instances.
 * Open for extension via [register], closed for modification.
 *
 * @property collectionManager the collection passed to every command factory
 * @property databaseManager injected into [CommandRegistry] for future extensibility
 * @property authService the authentication service passed to the register command factory
 */
class CommandRegistry(
    private val collectionManager: CollectionManager,
    private val databaseManager: DatabaseManager,
    private val authService: AuthService
) {
    private val factories: MutableMap<String, CommandFactory> = mutableMapOf()

    init {
        register("info") { _, _ -> InfoServerCommand(collectionManager) }
        register("show") { _, _ -> ShowServerCommand(collectionManager) }
        register("exit") { _, _ -> ExitServerCommand() }
        register("print_unique_unit_of_measure") { _, _ -> PrintUniqueUnitOfMeasureServerCommand(collectionManager) }
        register("print_field_descending_price") { _, _ -> PrintFieldDescendingPriceServerCommand(collectionManager) }
        register("login") { _, _ -> LoginServerCommand() }
        register("count_greater_than_price") { payload, _ ->
            CountGreaterThanPriceServerCommand(collectionManager, payload)
        }
        register("register") { payload, _ -> RegisterServerCommand(authService, payload) }
        register("insert") { payload, login -> InsertServerCommand(collectionManager, payload, login) }
        register("update") { payload, login -> UpdateServerCommand(collectionManager, payload, login) }
        register("remove_key") { payload, login -> RemoveKeyServerCommand(collectionManager, payload, login) }
        register("clear") { _, login -> ClearServerCommand(collectionManager, login) }
        register("remove_lower") { payload, login -> RemoveLowerServerCommand(collectionManager, payload, login) }
        register("replace_if_greater") { payload, login ->
            ReplaceIfGreaterServerCommand(collectionManager, payload, login)
        }
        register("remove_greater_key") { payload, login ->
            RemoveGreaterKeyServerCommand(collectionManager, payload, login)
        }
    }

    /**
     * Registers a [factory] for [commandName], replacing any existing registration.
     *
     * @param commandName the name clients use to identify the command
     * @param factory the [CommandFactory] that creates the [ServerCommand] for this name
     */
    fun register(commandName: String, factory: CommandFactory) {
        factories[commandName] = factory
    }

    /**
     * Creates a [ServerCommand] for [commandName] by invoking the registered [CommandFactory].
     *
     * @param commandName the command identifier from the incoming [connection.Request]
     * @param payload the serialized arguments string from the [connection.Request]
     * @param login the authenticated user's login
     * @return the constructed [ServerCommand], ready to execute
     * @throws IllegalArgumentException if [commandName] has no registered factory
     */
    fun create(commandName: String, payload: String, login: String): ServerCommand =
        (factories[commandName]
            ?: throw IllegalArgumentException("Unknown command: $commandName"))
            .create(payload, login)
}