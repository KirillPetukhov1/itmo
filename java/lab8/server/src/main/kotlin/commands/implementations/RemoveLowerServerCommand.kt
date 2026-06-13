package commands.implementations

import collection.CollectionManager
import commands.abstractions.ServerCommand
import commands.requireProduct
import connection.Response
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Removes all products whose price is strictly less than the given reference product's price.
 *
 * @property collectionManager the collection to modify
 * @property payload serialized JSON containing a "product" field
 */
class RemoveLowerServerCommand(
    private val collectionManager: CollectionManager,
    private val payload: String,
    private val login: String
) : ServerCommand {

    override fun execute(): Response {
        val product = requireProduct(payload, "product")
        val count = collectionManager.removeLower(product, login)
        return Response("remove_lower", buildJsonObject { put("removedCount", count) }.toString())
    }
}
