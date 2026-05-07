package commands

import abstractions.Command
import console.CommandManager
import console.FileReaderWriter
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * Reads a script file and executes each line through the same command pipeline
 * as interactive console input. Protects against infinite recursion by tracking
 * the stack of active script files.
 *
 * @property commandManager the command manager used to dispatch each script line
 */
class ExecuteScriptCommand(private val commandManager: CommandManager) : Command() {

    override val description =
        "execute_script file_name : считать и исполнить скрипт из указанного файла. " +
        "В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме."

    override val isShouldBeSent = false

    override fun start(args: Array<String>) {
        require(args.size == 2) { "Usage: execute_script <file_name>" }
        val fileName = args[1]

        if (activeFiles.contains(fileName)) {
            println("Recursive script execution detected for '$fileName'. Skipping.")
            return
        }

        val file = File(fileName)
        if (!file.exists()) {
            println("Script file not found: $fileName")
            return
        }
        if (!file.canRead()) {
            println("No read permission for script file: $fileName")
            return
        }

        activeFiles.add(fileName)
        try {
            val fileReaderWriter = FileReaderWriter(BufferedReader(FileReader(file)))
            commandManager.runFromFileReaderWriter(fileReaderWriter)
        } finally {
            activeFiles.remove(fileName)
        }
    }

    override fun finish() {}

    companion object {
        private val activeFiles: MutableSet<String> = mutableSetOf()
    }
}
