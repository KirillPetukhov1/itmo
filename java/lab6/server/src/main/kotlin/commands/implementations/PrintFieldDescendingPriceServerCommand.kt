package commands.implementations

import collection.CollectionManager
import commands.abstractions.ServerCommand
import commands.longListResponse
import connection.Response

/**
 * Returns all product price values sorted in descending order.
 *
 * @property collectionManager the collection to query
 */
class PrintFieldDescendingPriceServerCommand(
    private val collectionManager: CollectionManager
) : ServerCommand {

    override fun execute(): Response =
        longListResponse(
            "print_field_descending_price",
            "prices",
            collectionManager.pricesDescending()
        )
}
