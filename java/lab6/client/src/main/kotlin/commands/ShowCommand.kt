package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product

@Serializable
class ShowCommand<K : Comparable<K>, V : Product> : Command<K, V>() {
    @Transient
    override val description =
        "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении"

    @Transient
    override val isShouldBeSent = true

    private var productsCollection: HashMap<K, V> = hashMapOf()

    override fun start(args: Array<String>) {
        if (args.size != 1) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
    }

    override fun finish() {
        if (productsCollection.isEmpty()) {
            println("The collection does not contain any items.")
        } else {
            for (product in productsCollection) {
                println("${product.key}: ${product.value}")
            }
        }
    }
}