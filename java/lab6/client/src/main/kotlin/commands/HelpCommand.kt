package commands

import abstractions.AbstractCommandManager
import abstractions.Command
import objects.Product


class HelpCommand<K : Comparable<K>, V : Product>(
    private val commandManager: AbstractCommandManager<K, V>
) : Command<K, V>() {

    override val description = "help : вывести справку по доступным командам"

    override val isShouldBeSent = false

    override fun start(args: Array<String>) {
        if (args.size != 1) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
        commandManager.commands.forEach { (_, command) -> println(command.description) }
    }

    override fun finish() {}
}