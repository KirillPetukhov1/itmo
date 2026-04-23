package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product
import objects.UnitOfMeasure

@Serializable
class PrintUniqueUnitOfMeasureCommand<K : Comparable<K>, V : Product> : Command<K, V>() {
    @Transient
    override val description =
        "print_unique_unit_of_measure : вывести уникальные значения поля unitOfMeasure всех элементов в коллекции"

    @Transient
    override val isShouldBeSent = true

    private var unitOfMeasureSet: MutableSet<UnitOfMeasure> = mutableSetOf()

    override fun start(args: Array<String>) {
        if (args.size != 1) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
    }

    override fun finish() {
        println("The collection contains the following unique UnitOfMeasure values: ")
        for (unitOfMeasure in unitOfMeasureSet) {
            println(unitOfMeasure)
        }
    }
}