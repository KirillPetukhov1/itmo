package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Displays all unique UnitOfMeasure values present in the collection.
 */
@Serializable
class PrintUniqueUnitOfMeasureCommand : Command() {

    @Transient
    override val description =
        "print_unique_unit_of_measure : вывести уникальные значения поля unitOfMeasure всех элементов в коллекции"

    @Transient
    override val isShouldBeSent = true

    private var values: List<String> = emptyList()

    override fun start(args: Array<String>) {
        require(args.size == 1) { "print_unique_unit_of_measure takes no arguments" }
    }

    /**
     * Reads the unique unit-of-measure values from the server response.
     *
     * @param responseJson the serialised result from the server
     */
    fun applyResult(responseJson: String) {
        values = Json.parseToJsonElement(responseJson).jsonObject["values"]
            ?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
    }

    override fun finish() {
        println("The collection contains the following unique UnitOfMeasure values:")
        values.forEach(::println)
    }
}
