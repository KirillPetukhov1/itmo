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
 * Removes all elements whose key is lexicographically greater than the given key.
 */
@Serializable
class RemoveGreaterKeyCommand : Command() {

    @Transient
    override val description =
        "remove_greater_key null : удалить из коллекции все элементы, ключ которых превышает заданный"

    @Transient
    override val isShouldBeSent = true

    var key: String = ""

    @Transient
    private var removedCount = 0

    override fun start(args: Array<String>) {
        require(args.size == 2) { "Usage: remove_greater_key <key>" }
        key = args[1]
    }

    /**
     * Serializes [key] into the payload expected by the server.
     *
     * @return JSON string with the key field
     */
    fun toPayload(): String = buildJsonObject { put("key", key) }.toString()

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
        else println("The collection has not changed, no matching keys have been found.")
    }
}
