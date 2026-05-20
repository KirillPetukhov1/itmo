package commands

import abstractions.Command

/**
 * Displays descriptions of all registered commands.
 * This command is handled entirely on the client side.
 *
 * @property commandRegistry the map of registered command names to command instances
 */
class HelpCommand(private val commandRegistry: Map<String, Command>) : Command() {

    override val description = "help : вывести справку по доступным командам"
    override val isShouldBeSent = false

    override fun start(args: Array<String>) {
        require(args.size == 1) { "help takes no arguments" }
        commandRegistry.values.forEach { println(it.description) }
    }

    override fun finish() {}
}
