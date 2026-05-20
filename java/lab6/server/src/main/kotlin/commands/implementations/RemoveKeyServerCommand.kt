package commands.implementations

import collection.CollectionManager
import commands.abstractions.ServerCommand
import commands.requireString
import connection.Response
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Removes the element stored under the given key.
 *
 * @property collectionManager the collection to modify
 * @property payload serialized JSON containing a "key" field
 */
class RemoveKeyServerCommand(
    private val collectionManager: CollectionManager,
    private val payload: String
) : ServerCommand {

    override fun execute(): Response {
        val key = requireString(payload, "key")
        val removed = collectionManager.removeKey(key)
        return Response("remove_key", buildJsonObject { put("removed", removed) }.toString())
    }
}
