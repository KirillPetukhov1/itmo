package manager

import abstractions.AbstractReaderWriter
import abstractions.Command
import commands.*
import console.ProductConsoleForm
import kotlinx.serialization.json.Json

/**
 * Registers all commands, drives the interactive read-execute loop, and manages
 * the authenticated user session.
 *
 * Commands that require server interaction are blocked with an error message until the user
 * authenticates via 'login' or 'register'. The commands 'register', 'login', 'help', and 'exit'
 * are always available without prior authentication.
 *
 * When running a script via [runFromReaderWriter], form readers and auth-exempt commands
 * are patched to use the file reader writer.
 *
 * @property session the client session
 * @property readerWriter the primary I/O channel (console)
 */
class CommandManager(
    private val session: Session,
    private val readerWriter: AbstractReaderWriter
) {
    @Suppress("unused")
    private val json = Json { ignoreUnknownKeys = true }

    /** All registered commands, keyed by their name. */
    val commands: MutableMap<String, Command> = mutableMapOf()

    init {
        val form = ProductConsoleForm(readerWriter)
        commands["help"] = HelpCommand(commands)
        commands["exit"] = ExitCommand()
        commands["info"] = InfoCommand()
        commands["show"] = ShowCommand()
        commands["insert"] = InsertCommand(form)
        commands["update"] = UpdateCommand(form)
        commands["remove_key"] = RemoveKeyCommand()
        commands["clear"] = ClearCommand()
        commands["remove_lower"] = RemoveLowerCommand(form)
        commands["replace_if_greater"] = ReplaceIfGreaterCommand(form)
        commands["remove_greater_key"] = RemoveGreaterKeyCommand()
        commands["count_greater_than_price"] = CountGreaterThanPriceCommand()
        commands["print_unique_unit_of_measure"] = PrintUniqueUnitOfMeasureCommand()
        commands["print_field_descending_price"] = PrintFieldDescendingPriceCommand()
        commands["register"] = RegisterCommand(readerWriter)
        commands["login"] = LoginCommand(readerWriter)
    }

    fun patchFormReaderWriter(rw: AbstractReaderWriter) {
        val form = ProductConsoleForm(rw)
        commands["insert"] = InsertCommand(form)
        commands["update"] = UpdateCommand(form)
        commands["remove_lower"] = RemoveLowerCommand(form)
        commands["replace_if_greater"] = ReplaceIfGreaterCommand(form)
        commands["register"] = RegisterCommand(rw)
        commands["login"] = LoginCommand(rw)
    }

    fun extractPayload(command: Command): String = when (command) {
        is InsertCommand -> command.toPayload()
        is UpdateCommand -> command.toPayload()
        is RemoveKeyCommand -> command.toPayload()
        is RemoveLowerCommand -> command.toPayload()
        is ReplaceIfGreaterCommand -> command.toPayload()
        is RemoveGreaterKeyCommand -> command.toPayload()
        is CountGreaterThanPriceCommand -> command.toPayload()
        is RegisterCommand -> command.toPayload()
        is LoginCommand -> command.toPayload()
        else -> "{}"
    }

    fun applyResult(command: Command, resultJson: String) {
        when (command) {
            is InfoCommand -> command.applyResult(resultJson)
            is ShowCommand -> command.applyResult(resultJson)
            is InsertCommand -> command.applyResult(resultJson)
            is UpdateCommand -> command.applyResult(resultJson)
            is RemoveKeyCommand -> command.applyResult(resultJson)
            is RemoveLowerCommand -> command.applyResult(resultJson)
            is ReplaceIfGreaterCommand -> command.applyResult(resultJson)
            is RemoveGreaterKeyCommand -> command.applyResult(resultJson)
            is CountGreaterThanPriceCommand -> command.applyResult(resultJson)
            is PrintUniqueUnitOfMeasureCommand -> command.applyResult(resultJson)
            is PrintFieldDescendingPriceCommand -> command.applyResult(resultJson)
            is RegisterCommand -> command.applyResult(resultJson)
            is LoginCommand -> {
                command.applyResult(resultJson)
                if (command.isSuccess()) {
                    session.currentLogin = command.login
                    session.currentPassword = command.password
                    session.isAuthenticated = true
                }
            }

            else -> {}
        }
    }
}