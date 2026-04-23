package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product
import kotlin.system.exitProcess

@Serializable
class ExitCommand<K : Comparable<K>, V : Product> : Command<K, V>() {
    @Transient
    override val description = "exit : завершить программу (без сохранения в файл)"

    @Transient
    override val isShouldBeSent = true

    override fun start(args: Array<String>) {
        if (args.size != 1) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
    }

    override fun finish() {
        println("The job is done, goodbye!")
        exitProcess(0)
    }
}