package commands.implementations

import collection.CollectionManager
import commands.abstractions.ServerCommand
import commands.successResponse
import connection.Response

/**
 * Clears all elements from the collection and resets the id counter.
 *
 * @property collectionManager the collection to clear
 */
class ClearServerCommand(
    private val collectionManager: CollectionManager,
    private val login: String
) : ServerCommand {

    override fun execute(): Response {
        collectionManager.clear(login)
        return successResponse("clear", "cleared")
    }
}
