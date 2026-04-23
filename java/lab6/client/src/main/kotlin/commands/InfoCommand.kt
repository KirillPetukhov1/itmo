package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product

@Serializable
class InfoCommand<K : Comparable<K>, V : Product> : Command<K, V>() {
    @Transient
    override val description =
        "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)"

    @Transient
    override val isShouldBeSent = true

    private var info = ""

    override fun start(args: Array<String>) {
        if (args.size != 1) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
    }

    override fun finish() {
        println(info)
    }
}