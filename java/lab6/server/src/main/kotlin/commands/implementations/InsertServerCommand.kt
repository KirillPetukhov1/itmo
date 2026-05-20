package commands.implementations

import collection.CollectionManager
import commands.abstractions.ServerCommand
import commands.requireProduct
import commands.requireString
import commands.successResponse
import connection.Response

/**
 * Inserts a new product under the given key.
 * The product's id and creationDate are assigned by [CollectionManager].
 *
 * @property collectionManager the collection to modify
 * @property payload serialized JSON containing "key" and "product" fields
 */
class InsertServerCommand(
    private val collectionManager: CollectionManager,
    private val payload: String
) : ServerCommand {

    override fun execute(): Response {
        val key = requireString(payload, "key")
        val product = requireProduct(payload, "product")
        collectionManager.insert(key, product)
        return successResponse("insert", "updated")
    }
}
