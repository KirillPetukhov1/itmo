package commands

import abstractions.Command
import abstractions.KeyParse
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product

@Serializable
class RemoveGreaterKeyCommand<K : Comparable<K>, V : Product>(
    @Transient private val keyParser: KeyParse<K>? = null
) : Command<K, V>() {

    @Transient
    override val description =
        "remove_greater_key null : удалить из коллекции все элементы, ключ которых превышает заданный"

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
            println("The collection has not changed, no matching keys have been found.")
        }
    }
}