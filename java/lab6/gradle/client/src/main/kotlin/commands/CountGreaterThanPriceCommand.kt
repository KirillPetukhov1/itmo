package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * Counts elements whose price exceeds the given threshold.
 */
@Serializable
class CountGreaterThanPriceCommand : Command() {

    @Transient
    override val description =
        "count_greater_than_price price : вывести количество элементов, значение поля price которых больше заданного"

    @Transient
    override val isShouldBeSent = true

    var price: Long = 0

    @Transient
    private var count: Long = 0

    override fun start(args: Array<String>) {
        require(args.size == 2) { "Usage: count_greater_than_price <price>" }
        price = args[1].toLong()
    }

    /**
     * Serializes [price] into the payload expected by the server.
     *
     * @return JSON string with the price field
     */
    fun toPayload(): String = buildJsonObject { put("price", price) }.toString()

    /**
     * Reads the count from the server response.
     *
     * @param responseJson the serialised result from the server
     */
    fun applyResult(responseJson: String) {
        count = Json.parseToJsonElement(responseJson).jsonObject["count"]
            ?.jsonPrimitive?.content?.toLongOrNull() ?: 0
    }

    override fun finish() =
        println("The collection contains $count items more expensive than the specified one.")
}
