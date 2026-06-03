package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Clears all elements from the collection.
 */
@Serializable
class ClearCommand : Command() {

    @Transient
    override val description = "clear : очистить коллекцию"

    @Transient
    override val isShouldBeSent = true

    override fun start(args: Array<String>) {
        require(args.size == 1) { "clear takes no arguments" }
    }

    override fun finish() = println("The collection has been successfully cleared.")
}
