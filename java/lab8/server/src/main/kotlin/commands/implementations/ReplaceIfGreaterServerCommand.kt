package commands.implementations

import collection.CollectionManager
import commands.abstractions.ServerCommand
import commands.requireProduct
import commands.requireString
import connection.Response
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Replaces the element at the given key only when the new product's price exceeds the existing one.
 *
 * @property collectionManager the collection to modify
 * @property payload serialized JSON containing "key" and "product" fields
 */
class ReplaceIfGreaterServerCommand(
    private val collectionManager: CollectionManager,
    private val payload: String,
    private val login: String
) : ServerCommand {

    override fun execute(): Response {
        val key = requireString(payload, "key")
        val product = requireProduct(payload, "product")
        val replaced = collectionManager.replaceIfGreater(key, product, login) != null
        return Response("replace_if_greater", buildJsonObject { put("updated", replaced) }.toString())
    }
}
