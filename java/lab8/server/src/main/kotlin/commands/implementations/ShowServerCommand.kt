package commands.implementations

import collection.CollectionManager
import commands.abstractions.ServerCommand
import commands.sharedJson
import connection.Response
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray

/**
 * Returns all collection elements sorted by default order.
 *
 * The response payload carries two parallel fields. The "lines" field holds the legacy
 * string representation of every entry and preserves backward compatibility with the
 * console client. The "items" field holds the structured entries consumed by the
 * graphical client to populate the table and the visualization area.
 *
 * @property collectionManager the collection to query
 */
class ShowServerCommand(private val collectionManager: CollectionManager) : ServerCommand {

    override fun execute(): Response {
        val entries = collectionManager.showStructured()
        val payload = buildJsonObject {
            putJsonArray("lines") {
                entries.forEach { add("${it.key}: ${it.product}") }
            }
            put("items", sharedJson.encodeToJsonElement(entries))
        }
        return Response("show", payload.toString())
    }
}