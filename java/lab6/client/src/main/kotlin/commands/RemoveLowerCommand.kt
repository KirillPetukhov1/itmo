package commands

import abstractions.AbstractComplicatedObjectReader
import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product

@Serializable
class RemoveLowerCommand<K : Comparable<K>, V : Product>(
    @Transient private val productReader: AbstractComplicatedObjectReader<V>? = null
) : Command<K, V>() {

    @Transient
    override val description = "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный"

    @Transient
    override val isShouldBeSent = true

    private var product: V? = null

    private var isCollectionUpdated = false

    override fun start(args: Array<String>) {
        if (args.size != 1) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
        product = productReader?.readObject()
    }

    override fun finish() {
        if (isCollectionUpdated) {
            println("The collection has been successfully updated.")
        } else {
            println("The collection has not changed, no matching items have been found.")
        }
    }
}