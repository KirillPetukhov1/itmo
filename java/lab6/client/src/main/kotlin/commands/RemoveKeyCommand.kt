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
 * Removes the element stored under the given key.
 */
@Serializable
class RemoveKeyCommand : Command() {

    @Transient
    override val description = "remove_key null : удалить элемент из коллекции по его ключу"

    @Transient
    override val isShouldBeSent = true

    var key: String = ""

    @Transient
    private var removed = false

    override fun start(args: Array<String>) {
        require(args.size == 2) { "Usage: remove_key <key>" }
        key = args[1]
    }

    /**
     * Serializes [key] into the payload expected by the server.
     *
     * @return JSON string with the key field
     */
    fun toPayload(): String = buildJsonObject { put("key", key) }.toString()

    /**
     * Reads the removal result from the server response.
     *
     * @param responseJson the serialised result from the server
     */
    fun applyResult(responseJson: String) {
        removed = Json.parseToJsonElement(responseJson).jsonObject["removed"]?.jsonPrimitive?.content == "true"
    }

    override fun finish() {
        if (removed) println("The collection has been successfully updated.")
        else println("The collection does not contain an element with this key.")
    }
}
