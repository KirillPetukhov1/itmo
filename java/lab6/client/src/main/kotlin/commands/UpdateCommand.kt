package commands

import abstractions.AbstractComplicatedObjectReader
import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product

@Serializable
class UpdateCommand<K : Comparable<K>, V : Product>(
    @Transient private val productReader: AbstractComplicatedObjectReader<V>? = null
) : Command<K, V>() {

    @Transient
    override val description = "update id {element} : обновить значение элемента коллекции, id которого равен заданному"

    @Transient
    override val isShouldBeSent = true

    private var id = 0L

    private var product: V? = null

    private var isCollectionUpdated = false

    override fun start(args: Array<String>) {
        if (args.size != 2) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
        id = args[1].toLong()
        product = productReader?.readObject()
    }

    override fun finish() {
        if (isCollectionUpdated) {
            println("The collection has been successfully updated.")
        } else {
            println("The collection does not contain an element with this index.")
        }
    }
}