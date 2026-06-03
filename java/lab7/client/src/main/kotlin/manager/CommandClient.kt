package manager

import abstractions.AbstractReaderWriter
import abstractions.EofException
import commands.ExecuteScriptCommand
import commands.LoginCommand
import connection.Request
import connection.Response
import connection.TcpClient
import console.CommandInterruptedException
import console.FileReaderWriter
import kotlin.system.exitProcess

class CommandClient(
    private val session: Session,
    private val tcpClient: TcpClient,
    private val readerWriter: AbstractReaderWriter,
    private val commandManager: CommandManager
) {

    /**
     * Runs the interactive command loop, reading from [readerWriter] until EOF.
     * Prints a prompt on startup asking the user to authenticate.
     */
    fun run() {
        readerWriter.write("Use 'register' to create an account or 'login' to authenticate.")
        while (true) {
            readerWriter.write("Enter the command:")
            val line = readerWriter.readLine() ?: run {
                println("Goodbye!")
                exitProcess(0)
            }
            if (line.isBlank()) continue
            try {
                processLine(line)
            } catch (e: EofException) {
                println("Goodbye!")
                exitProcess(0)
            }
        }
    }

    /**
     * Runs commands from [fileReaderWriter], used by [ExecuteScriptCommand].
     * Form readers and auth-command instances are patched to use [fileReaderWriter] for the
     * duration of the script, then restored.
     *
     * @param fileReaderWriter the file-backed I/O channel to read commands from
     */
    fun runFromReaderWriter(fileReaderWriter: FileReaderWriter) {
        commandManager.patchFormReaderWriter(fileReaderWriter)
        try {
            var nextLine: String? = fileReaderWriter.readCommandLine()
            while (nextLine != null) {
                val line = nextLine
                nextLine = null
                try {
                    processLine(line)
                } catch (e: CommandInterruptedException) {
                    val commandName = line.trim().split(Regex("\\s+")).firstOrNull() ?: line
                    fileReaderWriter.writeError(
                        "Command \"$commandName\" was not executed: object input was incomplete."
                    )
                    nextLine = e.commandLine
                    continue
                }
                nextLine = fileReaderWriter.readCommandLine()
            }
        } finally {
            commandManager.patchFormReaderWriter(readerWriter)
        }
    }

    private fun processLine(line: String) {
        val tokens = line.trim().split(Regex("\\s+"))
        val name = tokens[0].lowercase()
        val args = tokens.toTypedArray()

        val command = commandManager.commands[name]
        if (command == null) {
            readerWriter.writeError("The \"$name\" command was not found.")
            return
        }

        val requiresAuth = command.isShouldBeSent && name !in setOf("register", "login")
        if (requiresAuth && !session.isAuthenticated) {
            readerWriter.writeError("Not authenticated. Use 'login' or 'register' first.")
            return
        }

        try {
            command.start(args)
        } catch (e: CommandInterruptedException) {
            throw e
        } catch (e: EofException) {
            throw e
        } catch (e: IllegalArgumentException) {
            readerWriter.writeError("Argument error: ${e.message}")
            return
        } catch (e: NumberFormatException) {
            readerWriter.writeError("Invalid number format: ${e.message}")
            return
        }

        if (!command.isShouldBeSent) return

        val payload = commandManager.extractPayload(command)
        val (reqLogin, reqPassword) = when (command) {
            is LoginCommand -> command.login to command.password
            else -> session.currentLogin to session.currentPassword
        }

        val request = Request(
            commandName = name,
            serializedCommand = payload,
            login = reqLogin,
            password = reqPassword
        )

        val response: Response = try {
            tcpClient.send(request)
        } catch (e: IllegalStateException) {
            readerWriter.writeError("Server unavailable: ${e.message}")
            return
        }

        if (response.errorMessage != null) {
            readerWriter.writeError("Server error: ${response.errorMessage}")
            return
        }

        commandManager.applyResult(command, response.serializedResult)
        command.finish()
    }
}