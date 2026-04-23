package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product

@Serializable
class ClearCommand<K : Comparable<K>, V : Product> : Command<K, V>() {
    @Transient
    override val description = "clear : очистить коллекцию"

    @Transient
    override val isShouldBeSent = true

    override fun start(args: Array<String>) {
        if (args.size != 1) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
    }

    override fun finish() {
        println("The collection has been successfully cleared.")
    }
}