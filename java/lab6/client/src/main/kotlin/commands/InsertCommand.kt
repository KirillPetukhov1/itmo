package commands

import abstractions.Command
import abstractions.KeyParse
import abstractions.AbstractComplicatedObjectReader
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product

@Serializable
class InsertCommand<K : Comparable<K>, V : Product>(
    @Transient private val keyParser: KeyParse<K>? = null,
    @Transient private val productReader: AbstractComplicatedObjectReader<V>? = null
) : Command<K, V>() {

    @Transient
    override val description = "insert null {element} : добавить новый элемент с заданным ключом"

    @Transient
    override val isShouldBeSent = true

    private var key: K? = null

    private var product: V? = null

    private var outputText: String = "" // TODO

    override fun start(args: Array<String>) {
        if (args.size != 2) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
        key = keyParser?.getParsedObject(args[1])
        product = productReader?.readObject()
    }

    override fun finish() {
        println(outputText)
    }
}