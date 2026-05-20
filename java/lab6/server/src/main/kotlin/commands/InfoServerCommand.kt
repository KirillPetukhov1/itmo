package commands

import collection.CollectionManager
import connection.Response
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Returns metadata about the collection: type, initialisation date, and element count.
 *
 * @property collectionManager the collection to query
 */
class InfoServerCommand(private val collectionManager: CollectionManager) : ServerCommand {

    override fun execute(): Response {
        val info = collectionManager.info()
        return Response("info", buildJsonObject { put("info", info) }.toString())
    }
}
