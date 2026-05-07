package console

import abstractions.AbstractReaderWriter
import abstractions.Command
import commands.ClearCommand
import commands.CountGreaterThanPriceCommand
import commands.ExecuteScriptCommand
import commands.ExitCommand
import commands.HelpCommand
import commands.InfoCommand
import commands.InsertCommand
import commands.PrintFieldDescendingPriceCommand
import commands.PrintUniqueUnitOfMeasureCommand
import commands.RemoveGreaterKeyCommand
import commands.RemoveKeyCommand
import commands.RemoveLowerCommand
import commands.ReplaceIfGreaterCommand
import commands.ShowCommand
import commands.UpdateCommand
import connection.Request
import connection.Response
import connection.TcpClient
import kotlinx.serialization.json.Json

/**
 * Registers all commands, drives the interactive read-execute loop, and coordinates
 * serialization and network communication.
 *
 * In script mode field value lines must start with '>'. If a form expects a field
 * value but the next line is a command (no leading '>'), a [CommandInterruptedException]
 * is thrown by [FileReaderWriter]. [runFromFileReaderWriter] catches it, prints an error,
 * and resumes execution from the interrupting command line.
 *
 * @property tcpClient the TCP client used to send requests to the server
 * @property readerWriter the primary I/O channel (console)
 */
class CommandManager(
    private val tcpClient: TcpClient,
    private val readerWriter: AbstractReaderWriter
) {
    private val json = Json { ignoreUnknownKeys = true }

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
        commands["execute_script"] = ExecuteScriptCommand(this)
    }

    /**
     * Runs the interactive command loop, reading from [readerWriter] until EOF.
     */
    fun run() {
        while (true) {
            readerWriter.write("Enter the command:")
            val line = readerWriter.readLine()
            if (line.isEmpty()) continue
            processLine(line)
        }
    }

    /**
     * Runs commands from the provided [fileReaderWriter], used by [ExecuteScriptCommand].
     * Command lines are read via [FileReaderWriter.readCommandLine].
     * When a [CommandInterruptedException] surfaces from a form, the interrupting command
     * line is extracted and execution continues from it.
     *
     * @param fileReaderWriter the file-backed I/O channel to read commands from
     */
    fun runFromFileReaderWriter(fileReaderWriter: FileReaderWriter) {
        patchFormReaderWriter(fileReaderWriter)
        try {
            var nextLine: String? = fileReaderWriter.readCommandLine()
            while (nextLine != null) {
                val line = nextLine
                nextLine = null
                try {
                    processLine(line)
                } catch (e: CommandInterruptedException) {
                    val commandName = line.trim().split(Regex("\\s+")).firstOrNull() ?: line
                    println("Command \"$commandName\" was not executed: object input was incomplete.")
                    nextLine = e.commandLine
                    continue
                }
                nextLine = fileReaderWriter.readCommandLine()
            }
        } finally {
            patchFormReaderWriter(readerWriter)
        }
    }

    private fun patchFormReaderWriter(readerWriter: AbstractReaderWriter) {
        val form = ProductConsoleForm(readerWriter)
        commands["insert"] = InsertCommand(form)
        commands["update"] = UpdateCommand(form)
        commands["remove_lower"] = RemoveLowerCommand(form)
        commands["replace_if_greater"] = ReplaceIfGreaterCommand(form)
    }

    private fun processLine(line: String) {
        val tokens = line.trim().split(Regex("\\s+"))
        val name = tokens[0].lowercase()
        val args = tokens.toTypedArray()

        val command = commands[name]
        if (command == null) {
            readerWriter.write("The \"$name\" command was not found.")
            return
        }

        try {
            command.start(args)
        } catch (e: CommandInterruptedException) {
            throw e
        } catch (e: IllegalArgumentException) {
            readerWriter.write("Argument error: ${e.message}")
            return
        } catch (e: NumberFormatException) {
            readerWriter.write("Invalid number format: ${e.message}")
            return
        }

        if (!command.isShouldBeSent) return

        val payload = extractPayload(command)
        val request = Request(commandName = name, serializedCommand = payload)

        val response: Response = try {
            tcpClient.send(request)
        } catch (e: IllegalStateException) {
            readerWriter.write("Server unavailable: ${e.message}")
            return
        }

        if (response.errorMessage != null) {
            readerWriter.write("Server error: ${response.errorMessage}")
            return
        }

        applyResult(command, response.serializedResult)
        command.finish()
    }

    private fun extractPayload(command: Command): String = when (command) {
        is InsertCommand -> command.toPayload()
        is UpdateCommand -> command.toPayload()
        is RemoveKeyCommand -> command.toPayload()
        is RemoveLowerCommand -> command.toPayload()
        is ReplaceIfGreaterCommand -> command.toPayload()
        is RemoveGreaterKeyCommand -> command.toPayload()
        is CountGreaterThanPriceCommand -> command.toPayload()
        else -> "{}"
    }

    private fun applyResult(command: Command, resultJson: String) {
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
            else -> {}
        }
    }
}