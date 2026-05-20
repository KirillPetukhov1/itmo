package commands.implementations

import collection.CollectionManager
import commands.abstractions.ServerCommand
import commands.requireString
import connection.Response
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Removes all elements whose key is lexicographically greater than the given key.
 *
 * @property collectionManager the collection to modify
 * @property payload serialized JSON containing a "key" field
 */
class RemoveGreaterKeyServerCommand(
    private val collectionManager: CollectionManager,
    private val payload: String
) : ServerCommand {

    override fun execute(): Response {
        val key = requireString(payload, "key")
        val count = collectionManager.removeGreaterKey(key)
        return Response("remove_greater_key", buildJsonObject { put("removedCount", count) }.toString())
    }
}
