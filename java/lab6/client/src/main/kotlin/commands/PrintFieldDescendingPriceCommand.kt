package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product

@Serializable
class PrintFieldDescendingPriceCommand<K : Comparable<K>, V : Product> : Command<K, V>() {
    @Transient
    override val description =
        "print_field_descending_price : вывести значения поля price всех элементов в порядке убывания"

    @Transient
    override val isShouldBeSent = true

    private var prices: Array<Long> = arrayOf()

    override fun start(args: Array<String>) {
        if (args.size != 1) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
    }

    override fun finish() {
        println("Price values in descending order: ")
        for (price in prices) {
            println(price)
        }
    }
}