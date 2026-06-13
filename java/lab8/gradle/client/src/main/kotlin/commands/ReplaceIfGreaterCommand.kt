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
 * Replaces the element at the given key only if the new product is greater (by price).
 *
 * @property form the interactive form used to read the candidate product
 */
@Serializable
class ReplaceIfGreaterCommand(@Transient private val form: ProductConsoleForm? = null) : Command() {

    @Transient
    override val description =
        "replace_if_greater null {element} : заменить значение по ключу, если новое значение больше старого"

    @Transient
    override val isShouldBeSent = true

    var key: String = ""
    var product: Product? = null

    @Transient
    private var updated = false

    override fun start(args: Array<String>) {
        require(args.size == 2) { "Usage: replace_if_greater <key>" }
        key = args[1]
        product = form?.readProduct()
    }

    /**
     * Serializes [key] and [product] into the payload expected by the server.
     *
     * @return JSON string with key and product fields
     */
    fun toPayload(): String = buildJsonObject {
        put("key", key)
        put("product", Json.encodeToString(product))
    }.toString()

    /**
     * Reads whether the replacement occurred from the server response.
     *
     * @param responseJson the serialised result from the server
     */
    fun applyResult(responseJson: String) {
        updated = Json.parseToJsonElement(responseJson).jsonObject["updated"]?.jsonPrimitive?.content == "true"
    }

    override fun finish() {
        if (updated) println("The collection has been successfully updated.")
        else println("The collection has not changed.")
    }
}
