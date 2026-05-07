package commands

import collection.CollectionManager
import connection.Request
import connection.Response
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.long
import objects.Product

/**
 * Executes incoming [Request] objects against the [CollectionManager] and produces [Response] objects.
 * Each public method corresponds to one supported command name.
 * All collection operations are delegated to [CollectionManager].
 *
 * @property collectionManager the collection to operate on
 */
class CommandExecutor(private val collectionManager: CollectionManager) {

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Dispatches a [request] to the appropriate handler method.
     *
     * @param request the request received from a client
     * @return a [Response] carrying the result or an error message
     */
    fun execute(request: Request): Response {
        return try {
            when (request.commandName) {
                "info" -> info()
                "show" -> show()
                "insert" -> insert(request.serializedCommand)
                "update" -> update(request.serializedCommand)
                "remove_key" -> removeKey(request.serializedCommand)
                "clear" -> clear()
                "exit" -> exit()
                "remove_lower" -> removeLower(request.serializedCommand)
                "replace_if_greater" -> replaceIfGreater(request.serializedCommand)
                "remove_greater_key" -> removeGreaterKey(request.serializedCommand)
                "count_greater_than_price" -> countGreaterThanPrice(request.serializedCommand)
                "print_unique_unit_of_measure" -> printUniqueUnitOfMeasure()
                "print_field_descending_price" -> printFieldDescendingPrice()
                else -> error("Unknown command: ${request.commandName}")
            }
        } catch (e: Exception) {
            Response(
                commandName = request.commandName,
                serializedResult = "{}",
                errorMessage = e.message ?: "Unknown error"
            )
        }
    }

    private fun info(): Response {
        val info = collectionManager.info()
        val result = buildJsonObject { put("info", info) }
        return Response("info", result.toString())
    }

    private fun show(): Response {
        val lines = collectionManager.show()
        val result = buildJsonObject {
            putJsonArray("lines") { lines.forEach { add(kotlinx.serialization.json.JsonPrimitive(it)) } }
        }
        return Response("show", result.toString())
    }

    private fun insert(serializedCommand: String): Response {
        val obj = Json.parseToJsonElement(serializedCommand).jsonObject
        val key = obj["key"]?.jsonPrimitive?.content
            ?: return errorResponse("insert", "Key is missing")
        val productJson = obj["product"]?.toString()
            ?: return errorResponse("insert", "Product is missing")
        val product = json.decodeFromString<Product>(productJson)
        collectionManager.insert(key, product)
        return Response("insert", buildJsonObject { put("updated", true) }.toString())
    }

    private fun update(serializedCommand: String): Response {
        val obj = Json.parseToJsonElement(serializedCommand).jsonObject
        val id = obj["id"]?.jsonPrimitive?.long
            ?: return errorResponse("update", "Id is missing")
        val productJson = obj["product"]?.toString()
            ?: return errorResponse("update", "Product is missing")
        val product = json.decodeFromString<Product>(productJson)
        val result = collectionManager.update(id, product)
        return if (result != null) {
            Response("update", buildJsonObject { put("updated", true) }.toString())
        } else {
            Response("update", buildJsonObject { put("updated", false) }.toString())
        }
    }

    private fun removeKey(serializedCommand: String): Response {
        val obj = Json.parseToJsonElement(serializedCommand).jsonObject
        val key = obj["key"]?.jsonPrimitive?.content
            ?: return errorResponse("remove_key", "Key is missing")
        val removed = collectionManager.removeKey(key)
        return Response("remove_key", buildJsonObject { put("removed", removed) }.toString())
    }

    private fun clear(): Response {
        collectionManager.clear()
        return Response("clear", buildJsonObject { put("cleared", true) }.toString())
    }

    private fun exit(): Response =
        Response("exit", buildJsonObject { put("exit", true) }.toString())

    private fun removeLower(serializedCommand: String): Response {
        val obj = Json.parseToJsonElement(serializedCommand).jsonObject
        val productJson = obj["product"]?.toString()
            ?: return errorResponse("remove_lower", "Product is missing")
        val product = json.decodeFromString<Product>(productJson)
        val count = collectionManager.removeLower(product)
        return Response("remove_lower", buildJsonObject { put("removedCount", count) }.toString())
    }

    private fun replaceIfGreater(serializedCommand: String): Response {
        val obj = Json.parseToJsonElement(serializedCommand).jsonObject
        val key = obj["key"]?.jsonPrimitive?.content
            ?: return errorResponse("replace_if_greater", "Key is missing")
        val productJson = obj["product"]?.toString()
            ?: return errorResponse("replace_if_greater", "Product is missing")
        val product = json.decodeFromString<Product>(productJson)
        val replaced = collectionManager.replaceIfGreater(key, product) != null
        return Response("replace_if_greater", buildJsonObject { put("updated", replaced) }.toString())
    }

    private fun removeGreaterKey(serializedCommand: String): Response {
        val obj = Json.parseToJsonElement(serializedCommand).jsonObject
        val key = obj["key"]?.jsonPrimitive?.content
            ?: return errorResponse("remove_greater_key", "Key is missing")
        val count = collectionManager.removeGreaterKey(key)
        return Response("remove_greater_key", buildJsonObject { put("removedCount", count) }.toString())
    }

    private fun countGreaterThanPrice(serializedCommand: String): Response {
        val obj = Json.parseToJsonElement(serializedCommand).jsonObject
        val price = obj["price"]?.jsonPrimitive?.long
            ?: return errorResponse("count_greater_than_price", "Price is missing")
        val count = collectionManager.countGreaterThanPrice(price)
        return Response("count_greater_than_price", buildJsonObject { put("count", count) }.toString())
    }

    private fun printUniqueUnitOfMeasure(): Response {
        val values = collectionManager.uniqueUnitOfMeasure().map { it.name }
        val result = buildJsonObject {
            putJsonArray("values") { values.forEach { add(kotlinx.serialization.json.JsonPrimitive(it)) } }
        }
        return Response("print_unique_unit_of_measure", result.toString())
    }

    private fun printFieldDescendingPrice(): Response {
        val prices = collectionManager.pricesDescending()
        val result = buildJsonObject {
            putJsonArray("prices") { prices.forEach { add(kotlinx.serialization.json.JsonPrimitive(it)) } }
        }
        return Response("print_field_descending_price", result.toString())
    }

    private fun errorResponse(commandName: String, message: String): Response =
        Response(commandName = commandName, serializedResult = "{}", errorMessage = message)
}
