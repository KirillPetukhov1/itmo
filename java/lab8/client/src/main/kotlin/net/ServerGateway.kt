package net

import connection.ProductEntry
import connection.Request
import connection.Response
import connection.TcpClient
import kotlinx.serialization.json.*
import objects.Product
import session.AppSession

/**
 * Typed facade over [TcpClient] that translates method calls into [Request] objects,
 * sends them to the server over TCP, and returns parsed [GatewayResult] values.
 *
 * Every public method is synchronous and must not be called on the JavaFX Application Thread.
 * Callers are responsible for dispatching to a background thread via
 * [javafx.concurrent.Task] or a similar mechanism.
 *
 * The gateway replicates the JSON payload contracts defined by the original console-client
 * command classes so the server requires no further changes.
 *
 * @property tcpClient the underlying TCP transport
 * @property session the current user session; supplies credentials for every authenticated request
 */
class ServerGateway(
    private val tcpClient: TcpClient,
    private val session: AppSession
) {

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Sends a login request with [login] and [password] in the request envelope.
     * Credentials are validated server-side; on success the caller is expected to
     * store them in [session].
     *
     * @param login the user login to authenticate
     * @param password the plain-text password
     * @return [GatewayResult.Success] on successful authentication,
     *         [GatewayResult.Failure] with a descriptive message otherwise
     */
    fun login(login: String, password: String): GatewayResult<Unit> {
        val request = Request("login", "{}", login, password)
        val response = send(request) ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        val success = response.parseBody()["success"]?.jsonPrimitive?.booleanOrNull ?: false
        return if (success) GatewayResult.Success(Unit)
        else GatewayResult.Failure("Invalid credentials")
    }

    /**
     * Sends a registration request with [login] and [password] in the payload.
     * Registration is an auth-exempt command; no prior session is required.
     *
     * @param login the desired login for the new account
     * @param password the plain-text password for the new account
     * @return [GatewayResult.Success] when the account was created,
     *         [GatewayResult.Failure] with a descriptive message otherwise
     */
    fun register(login: String, password: String): GatewayResult<Unit> {
        val payload = buildJsonObject {
            put("login", login)
            put("password", password)
        }.toString()
        val request = Request("register", payload, "", "")
        val response = send(request) ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        val success = response.parseBody()["success"]?.jsonPrimitive?.booleanOrNull ?: false
        return if (success) GatewayResult.Success(Unit)
        else GatewayResult.Failure("Registration failed: login already taken")
    }

    /**
     * Requests all collection entries from the server as structured [ProductEntry] objects.
     * The entries arrive sorted by product price ascending, matching the server's default order.
     *
     * @return [GatewayResult.Success] carrying the sorted entry list,
     *         [GatewayResult.Failure] on a network or server error
     */
    fun show(): GatewayResult<List<ProductEntry>> {
        val response = sendAuth("show", "{}") ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        val itemsElement = response.parseBody()["items"]
            ?: return GatewayResult.Success(emptyList())
        return try {
            GatewayResult.Success(json.decodeFromJsonElement<List<ProductEntry>>(itemsElement))
        } catch (e: Exception) {
            GatewayResult.Failure("Failed to parse server response: ${e.message}")
        }
    }

    /**
     * Requests collection metadata from the server.
     *
     * @return [GatewayResult.Success] carrying the info string,
     *         [GatewayResult.Failure] on a network or server error
     */
    fun info(): GatewayResult<String> {
        val response = sendAuth("info", "{}") ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        val info = response.parseBody()["info"]?.jsonPrimitive?.content ?: ""
        return GatewayResult.Success(info)
    }

    /**
     * Inserts [product] under [key] into the collection.
     * Fails if [key] is already owned by another user.
     *
     * @param key the collection key
     * @param product the product to insert
     * @return [GatewayResult.Success] when the product was stored,
     *         [GatewayResult.Failure] with a descriptive message otherwise
     */
    fun insert(key: String, product: Product): GatewayResult<Unit> {
        val payload = buildJsonObject {
            put("key", key)
            put("product", json.encodeToJsonElement(product))
        }.toString()
        val response = sendAuth("insert", payload) ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        val updated = response.parseBody()["updated"]?.jsonPrimitive?.booleanOrNull ?: false
        return if (updated) GatewayResult.Success(Unit)
        else GatewayResult.Failure("Insert failed: key may already be occupied by another user")
    }

    /**
     * Replaces the product whose id equals [id] with [product].
     * Only the owner of the existing product may update it.
     *
     * @param id the id of the product to replace
     * @param product the new product data
     * @return [GatewayResult.Success] when the update was applied,
     *         [GatewayResult.Failure] when the id is absent or the user is not the owner
     */
    fun update(id: Long, product: Product): GatewayResult<Unit> {
        val payload = buildJsonObject {
            put("id", id)
            put("product", json.encodeToJsonElement(product))
        }.toString()
        val response = sendAuth("update", payload) ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        val updated = response.parseBody()["updated"]?.jsonPrimitive?.booleanOrNull ?: false
        return if (updated) GatewayResult.Success(Unit)
        else GatewayResult.Failure("No element with id $id found in the collection")
    }

    /**
     * Removes the element stored under [key].
     * Only the owner of the element may remove it.
     *
     * @param key the collection key to remove
     * @return [GatewayResult.Success] when the element was removed,
     *         [GatewayResult.Failure] when the key is absent or the user is not the owner
     */
    fun removeKey(key: String): GatewayResult<Unit> {
        val payload = buildJsonObject { put("key", key) }.toString()
        val response = sendAuth("remove_key", payload) ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        val removed = response.parseBody()["removed"]?.jsonPrimitive?.booleanOrNull ?: false
        return if (removed) GatewayResult.Success(Unit)
        else GatewayResult.Failure("No element with key '$key' found in the collection")
    }

    /**
     * Removes all products belonging to the current user from the collection.
     * Products owned by other users are not affected.
     *
     * @return [GatewayResult.Success] when the command was executed,
     *         [GatewayResult.Failure] on a network or server error
     */
    fun clear(): GatewayResult<Unit> {
        val response = sendAuth("clear", "{}") ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        return GatewayResult.Success(Unit)
    }

    /**
     * Removes all products belonging to the current user whose price is strictly
     * less than [product]'s price.
     *
     * @param product the reference product used for price comparison
     * @return [GatewayResult.Success] carrying the number of removed elements,
     *         [GatewayResult.Failure] on a network or server error
     */
    fun removeLower(product: Product): GatewayResult<Int> {
        val payload = buildJsonObject {
            put("product", json.encodeToJsonElement(product))
        }.toString()
        val response = sendAuth("remove_lower", payload) ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        val count = response.parseBody()["removedCount"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0
        return GatewayResult.Success(count)
    }

    /**
     * Replaces the product at [key] with [product] only when [product] is greater by price.
     * Only the owner of the existing product may invoke this.
     *
     * @param key the collection key
     * @param product the candidate replacement product
     * @return [GatewayResult.Success] with true when replaced, false when not replaced,
     *         [GatewayResult.Failure] on a network or server error
     */
    fun replaceIfGreater(key: String, product: Product): GatewayResult<Boolean> {
        val payload = buildJsonObject {
            put("key", key)
            put("product", json.encodeToJsonElement(product))
        }.toString()
        val response = sendAuth("replace_if_greater", payload)
            ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        val updated = response.parseBody()["updated"]?.jsonPrimitive?.booleanOrNull ?: false
        return GatewayResult.Success(updated)
    }

    /**
     * Removes all products belonging to the current user whose collection key is
     * lexicographically greater than [key].
     *
     * @param key the exclusive upper boundary key
     * @return [GatewayResult.Success] carrying the number of removed elements,
     *         [GatewayResult.Failure] on a network or server error
     */
    fun removeGreaterKey(key: String): GatewayResult<Int> {
        val payload = buildJsonObject { put("key", key) }.toString()
        val response = sendAuth("remove_greater_key", payload)
            ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        val count = response.parseBody()["removedCount"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0
        return GatewayResult.Success(count)
    }

    /**
     * Counts how many products in the collection have a price strictly greater than [price].
     *
     * @param price the price threshold
     * @return [GatewayResult.Success] carrying the count,
     *         [GatewayResult.Failure] on a network or server error
     */
    fun countGreaterThanPrice(price: Long): GatewayResult<Long> {
        val payload = buildJsonObject { put("price", price) }.toString()
        val response = sendAuth("count_greater_than_price", payload)
            ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        val count = response.parseBody()["count"]?.jsonPrimitive?.long ?: 0L
        return GatewayResult.Success(count)
    }

    /**
     * Returns all distinct unit-of-measure values present in the collection.
     *
     * @return [GatewayResult.Success] carrying a list of enum name strings,
     *         [GatewayResult.Failure] on a network or server error
     */
    fun uniqueUnitOfMeasure(): GatewayResult<List<String>> {
        val response = sendAuth("print_unique_unit_of_measure", "{}")
            ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        val values = response.parseBody()["values"]
            ?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
        return GatewayResult.Success(values)
    }

    /**
     * Returns all product price values sorted in descending order.
     *
     * @return [GatewayResult.Success] carrying the sorted price list,
     *         [GatewayResult.Failure] on a network or server error
     */
    fun pricesDescending(): GatewayResult<List<Long>> {
        val response = sendAuth("print_field_descending_price", "{}")
            ?: return GatewayResult.Failure("Server unavailable")
        if (response.errorMessage != null) return GatewayResult.Failure(response.errorMessage as String)
        val prices = response.parseBody()["prices"]
            ?.jsonArray?.mapNotNull { it.jsonPrimitive.content.toLongOrNull() } ?: emptyList()
        return GatewayResult.Success(prices)
    }

    private fun sendAuth(commandName: String, payload: String): Response? =
        send(Request(commandName, payload, session.currentLogin, session.currentPassword))

    private fun send(request: Request): Response? = try {
        tcpClient.send(request)
    } catch (_: IllegalStateException) {
        null
    }

    private fun Response.parseBody(): JsonObject =
        json.parseToJsonElement(serializedResult).jsonObject
}