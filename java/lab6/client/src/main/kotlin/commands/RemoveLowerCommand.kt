package commands

import abstractions.Command
import console.ProductConsoleForm
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import objects.Product

/**
 * Removes all products from the collection that are less than the given product (by price).
 *
 * @property form the interactive form used to read the reference product
 */
@Serializable
class RemoveLowerCommand(@Transient private val form: ProductConsoleForm? = null) : Command() {

    @Transient
    override val description = "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный"

    @Transient
    override val isShouldBeSent = true

    var product: Product? = null

    @Transient
    private var removedCount = 0

    override fun start(args: Array<String>) {
        require(args.size == 1) { "remove_lower takes no arguments" }
        product = form?.readProduct()
    }

    /**
     * Serializes [product] into the payload expected by the server.
     *
     * @return JSON string with the product field
     */
    fun toPayload(): String = buildJsonObject {
        put("product", Json.encodeToString(product))
    }.toString()

    /**
     * Reads the count of removed elements from the server response.
     *
     * @param responseJson the serialised result from the server
     */
    fun applyResult(responseJson: String) {
        removedCount = Json.parseToJsonElement(responseJson).jsonObject["removedCount"]
            ?.jsonPrimitive?.content?.toIntOrNull() ?: 0
    }

    override fun finish() {
        if (removedCount > 0) println("The collection has been successfully updated. Removed: $removedCount elements.")
        else println("The collection has not changed, no matching items have been found.")
    }
}
