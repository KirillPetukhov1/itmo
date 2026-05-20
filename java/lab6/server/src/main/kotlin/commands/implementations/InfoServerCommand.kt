package commands.implementations

import collection.CollectionManager
import commands.abstractions.ServerCommand
import connection.Response
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Returns metadata about the collection: type, initialisation date, and element count.
 *
 * @property collectionManager the collection to query
 */
class InfoServerCommand(private val collectionManager: CollectionManager) : ServerCommand {

    override fun execute(): Response =
        Response("info", buildJsonObject { put("info", collectionManager.info()) }.toString())
}
