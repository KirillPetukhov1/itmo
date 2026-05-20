package commands

import collection.CollectionManager
import connection.Response
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Updates the product whose id equals the given value, preserving its original id and creationDate.
 *
 * @property collectionManager the collection to modify
 * @property payload serialized JSON containing "id" and "product" fields
 */
class UpdateServerCommand(
    private val collectionManager: CollectionManager,
    private val payload: String
) : ServerCommand {

    override fun execute(): Response {
        val id = requireLong(payload, "id")
        val product = requireProduct(payload, "product")
        val updated = collectionManager.update(id, product) != null
        return Response("update", buildJsonObject { put("updated", updated) }.toString())
    }
}
