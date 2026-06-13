package commands

import abstractions.Command
import kotlin.system.exitProcess

/**
 * Terminates the client application without saving.
 */
class ExitCommand : Command() {

    override val description = "exit : завершить программу (без сохранения в файл)"
    override val isShouldBeSent = false

    override fun start(args: Array<String>) {
        require(args.size == 1) { "exit takes no arguments" }
        println("The job is done, goodbye!")
        exitProcess(0)
    }

    override fun finish() {}
}
