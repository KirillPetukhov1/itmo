package commands

import abstractions.AbstractComplicatedObjectReader
import abstractions.Command
import abstractions.KeyParse
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product

@Serializable
class ReplaceIfGreaterCommand<K : Comparable<K>, V : Product>(
    @Transient private val keyParser: KeyParse<K>? = null,
    @Transient private val productReader: AbstractComplicatedObjectReader<V>? = null
) : Command<K, V>() {

    @Transient
    override val description =
        "replace_if_greater null {element} : заменить значение по ключу, если новое значение больше старого"

    @Transient
    override val isShouldBeSent = true

    private var key: K? = null

    private var product: V? = null

    private var isCollectionUpdated = false

    override fun start(args: Array<String>) {
        if (args.size != 2) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
        key = keyParser?.getParsedObject(args[1])
        product = productReader?.readObject()
    }

    override fun finish() {
        if (isCollectionUpdated) {
            println("The collection has been successfully updated.")
        } else {
            println("The collection has not changed.")
        }
    }
}