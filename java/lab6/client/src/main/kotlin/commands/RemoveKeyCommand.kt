package commands

import abstractions.Command
import abstractions.KeyParse
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product

@Serializable
class RemoveKeyCommand<K : Comparable<K>, V : Product>(
    @Transient private val keyParser: KeyParse<K>? = null
) : Command<K, V>() {

    @Transient
    override val description = "remove_key null : удалить элемент из коллекции по его ключу"

    @Transient
    override val isShouldBeSent = true

    private var key: K? = null

    private var isCollectionUpdated = false

    override fun start(args: Array<String>) {
        if (args.size != 2) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
        key = keyParser?.getParsedObject(args[1])
    }

    override fun finish() {
        if (isCollectionUpdated) {
            println("The collection has been successfully updated.")
        } else {
            println("The collection does not contain an element with this key.")
        }
    }
}