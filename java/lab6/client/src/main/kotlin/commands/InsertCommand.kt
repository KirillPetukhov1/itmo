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
 * Inserts a new product into the collection under the given key.
 *
 * @property form the interactive form used to read product fields from the user
 */
@Serializable
class InsertCommand(@Transient private val form: ProductConsoleForm? = null) : Command() {

    @Transient
    override val description = "insert null {element} : добавить новый элемент с заданным ключом"

    @Transient
    override val isShouldBeSent = true

    var key: String = ""
    var product: Product? = null

    override fun start(args: Array<String>) {
        require(args.size == 2) { "Usage: insert <key>" }
        key = args[1]
        product = form?.readProduct()
    }

    /**
     * Serializes [key] and [product] into the JSON payload expected by the server.
     *
     * @return the JSON string of this command's arguments
     */
    fun toPayload(): String = buildJsonObject {
        put("key", key)
        put("product", Json.encodeToString(product))
    }.toString()

    /**
     * Reads the server response and stores the result message.
     *
     * @param responseJson the serialised result from the server
     */
    fun applyResult(responseJson: String) {
        val updated = Json.parseToJsonElement(responseJson).jsonObject["updated"]?.jsonPrimitive?.content == "true"
        resultMessage = if (updated) "The collection has been successfully updated."
        else "Something went wrong during insert."
    }

    @Transient
    private var resultMessage = ""

    override fun finish() = println(resultMessage)
}
