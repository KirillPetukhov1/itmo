package commands

import collection.CollectionManager
import connection.Response

/**
 * Clears all elements from the collection and resets the id counter.
 *
 * @property collectionManager the collection to clear
 */
class ClearServerCommand(private val collectionManager: CollectionManager) : ServerCommand {

    override fun execute(): Response {
        collectionManager.clear()
        return successResponse("clear", "cleared")
    }
}
