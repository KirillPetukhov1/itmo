package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product

@Serializable
class CountGreaterThanPriceCommand<K : Comparable<K>, V : Product> : Command<K, V>() {
    @Transient
    override val description =
        "count_greater_than_price price : вывести количество элементов, значение поля price которых больше заданного"

    @Transient
    override val isShouldBeSent = true

    private var price = 0L

    private var count = 0

    override fun start(args: Array<String>) {
        if (args.size != 2) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
        price = args[1].toLong()
    }

    override fun finish() {
        println("The collection contains $count items more expensive than the specified one.")
    }
}