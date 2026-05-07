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
 * Updates the product whose ID matches the given value.
 *
 * @property form the interactive form used to read new product fields from the user
 */
@Serializable
class UpdateCommand(@Transient private val form: ProductConsoleForm? = null) : Command() {

    @Transient
    override val description = "update id {element} : обновить значение элемента коллекции, id которого равен заданному"

    @Transient
    override val isShouldBeSent = true

    var id: Long = 0
    var product: Product? = null

    @Transient
    private var updated = false

    override fun start(args: Array<String>) {
        require(args.size == 2) { "Usage: update <id>" }
        id = args[1].toLong()
        product = form?.readProduct()
    }

    /**
     * Serializes [id] and [product] into the JSON payload expected by the server.
     *
     * @return the JSON string of this command's arguments
     */
    fun toPayload(): String = buildJsonObject {
        put("id", id)
        put("product", Json.encodeToString(product))
    }.toString()

    /**
     * Reads whether the server successfully updated the element.
     *
     * @param responseJson the serialised result from the server
     */
    fun applyResult(responseJson: String) {
        updated = Json.parseToJsonElement(responseJson).jsonObject["updated"]?.jsonPrimitive?.content == "true"
    }

    override fun finish() {
        if (updated) println("The collection has been successfully updated.")
        else println("The collection does not contain an element with this index.")
    }
}
