package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Displays all product price values in descending order.
 */
@Serializable
class PrintFieldDescendingPriceCommand : Command() {

    @Transient
    override val description =
        "print_field_descending_price : вывести значения поля price всех элементов в порядке убывания"

    @Transient
    override val isShouldBeSent = true

    private var prices: List<Long> = emptyList()

    override fun start(args: Array<String>) {
        require(args.size == 1) { "print_field_descending_price takes no arguments" }
    }

    /**
     * Reads the price list from the server response.
     *
     * @param responseJson the serialised result from the server
     */
    fun applyResult(responseJson: String) {
        prices = Json.parseToJsonElement(responseJson).jsonObject["prices"]
            ?.jsonArray?.mapNotNull { it.jsonPrimitive.content.toLongOrNull() } ?: emptyList()
    }

    override fun finish() {
        println("Price values in descending order:")
        prices.forEach(::println)
    }
}
