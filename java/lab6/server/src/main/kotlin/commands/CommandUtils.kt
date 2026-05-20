package commands

import connection.Response
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import objects.Product

internal val sharedJson = Json { ignoreUnknownKeys = true }

/**
 * Parses the top-level JSON object from [payload].
 *
 * @return the parsed [kotlinx.serialization.json.JsonObject]
 */
internal fun parsePayload(payload: String) = Json.parseToJsonElement(payload).jsonObject

/**
 * Extracts a mandatory string field [key] from the payload object.
 *
 * @throws IllegalArgumentException if the field is absent
 */
internal fun requireString(payload: String, key: String): String =
    parsePayload(payload)[key]?.jsonPrimitive?.content
        ?: throw IllegalArgumentException("Field '$key' is missing from payload")

/**
 * Extracts a mandatory Long field [key] from the payload object.
 *
 * @throws IllegalArgumentException if the field is absent
 */
internal fun requireLong(payload: String, key: String): Long =
    parsePayload(payload)[key]?.jsonPrimitive?.long
        ?: throw IllegalArgumentException("Field '$key' is missing from payload")

/**
 * Deserializes a [Product] from the nested JSON object stored under [key] in the payload.
 *
 * @throws IllegalArgumentException if the product field is absent
 */
internal fun requireProduct(payload: String, key: String): Product {
    val productJson = parsePayload(payload)[key]?.toString()
        ?: throw IllegalArgumentException("Field '$key' is missing from payload")
    return sharedJson.decodeFromString<Product>(productJson)
}

/**
 * Builds a success [Response] carrying a simple boolean flag [fieldName] = true.
 */
internal fun successResponse(commandName: String, fieldName: String): Response =
    Response(commandName, buildJsonObject { put(fieldName, true) }.toString())

/**
 * Builds a [Response] whose payload is a JSON array of strings under [arrayKey].
 */
internal fun listResponse(commandName: String, arrayKey: String, items: List<String>): Response =
    Response(commandName, buildJsonObject {
        putJsonArray(arrayKey) { items.forEach { add(JsonPrimitive(it)) } }
    }.toString())

/**
 * Builds a [Response] whose payload is a JSON array of longs under [arrayKey].
 */
internal fun longListResponse(commandName: String, arrayKey: String, items: List<Long>): Response =
    Response(commandName, buildJsonObject {
        putJsonArray(arrayKey) { items.forEach { add(JsonPrimitive(it)) } }
    }.toString())
