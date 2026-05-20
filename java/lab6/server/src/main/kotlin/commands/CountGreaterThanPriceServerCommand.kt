package commands

import collection.CollectionManager
import connection.Response
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Counts products whose price exceeds the given threshold.
 *
 * @property collectionManager the collection to query
 * @property payload serialized JSON containing a "price" field
 */
class CountGreaterThanPriceServerCommand(
    private val collectionManager: CollectionManager,
    private val payload: String
) : ServerCommand {

    override fun execute(): Response {
        val price = requireLong(payload, "price")
        val count = collectionManager.countGreaterThanPrice(price)
        return Response("count_greater_than_price", buildJsonObject { put("count", count) }.toString())
    }
}
