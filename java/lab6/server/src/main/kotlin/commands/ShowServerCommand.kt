package commands

import collection.CollectionManager
import connection.Response

/**
 * Returns all collection elements as strings sorted by default order.
 *
 * @property collectionManager the collection to query
 */
class ShowServerCommand(private val collectionManager: CollectionManager) : ServerCommand {

    override fun execute(): Response =
        listResponse("show", "lines", collectionManager.show())
}
