package commands.implementations

import collection.CollectionManager
import commands.abstractions.ServerCommand
import commands.requireProduct
import commands.requireString
import commands.successResponse
import connection.Response

/**
 * Inserts a new product under the given key on behalf of [login].
 * The product's id is assigned by the database sequence; the creation date is set by the server.
 * Fails if the key is already occupied by another user's product.
 *
 * @property collectionManager the collection to modify
 * @property payload serialized JSON containing "key" and "product" fields
 * @property login the authenticated user performing the insert
 */
class InsertServerCommand(
    private val collectionManager: CollectionManager,
    private val payload: String,
    private val login: String
) : ServerCommand {

    override fun execute(): Response {
        val key = requireString(payload, "key")
        val product = requireProduct(payload, "product")
        collectionManager.insert(key, product, login)
        return successResponse("insert", "updated")
    }
}