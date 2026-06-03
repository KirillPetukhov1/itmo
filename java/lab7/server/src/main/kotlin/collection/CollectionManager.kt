package collection

import database.DatabaseManager
import objects.Product
import objects.UnitOfMeasure
import java.time.LocalDateTime
import java.util.Collections

/**
 * Manages the in-memory synchronized collection of [Product] objects.
 * All mutation operations persist to the database first; the in-memory state is updated
 * only after a successful database write.
 * All read operations work exclusively against the in-memory collection.
 * The collection and the creator-login index both use [Collections.synchronizedMap].
 *
 * @property databaseManager the database manager used for persistence
 * @property initDateTime the moment this instance was created
 */
class CollectionManager(private val databaseManager: DatabaseManager) {

    /** The synchronized in-memory map of products keyed by their collection key. */
    val products: MutableMap<String, Product> = Collections.synchronizedMap(HashMap())

    private val creatorLogins: MutableMap<Long, String> = Collections.synchronizedMap(HashMap())

    /** The date and time when this collection manager was instantiated. */
    val initDateTime: LocalDateTime = LocalDateTime.now()

    /**
     * Replaces the in-memory state with [loadedProducts] and [loadedCreators].
     *
     * @param loadedProducts products to load, keyed by collection key
     * @param loadedCreators map from product id to the login of the user who created it
     */
    fun load(loadedProducts: Map<String, Product>, loadedCreators: Map<Long, String>) {
        synchronized(products) { products.run { clear(); putAll(loadedProducts) } }
        synchronized(creatorLogins) { creatorLogins.run { clear(); putAll(loadedCreators) } }
    }

    /**
     * Returns a human-readable description of the collection.
     *
     * @return type, initialization date, and element count as a single string
     */
    fun info(): String =
        "Type: ${products.javaClass.simpleName}, initialized: $initDateTime, elements: ${products.size}"

    /**
     * Returns all elements sorted by default order as a list of key-value strings.
     *
     * @return list of "key: product" strings sorted by product price ascending
     */
    fun show(): List<String> {
        val snapshot = synchronized(products) { products.entries.toList() }
        return snapshot.stream()
            .sorted { a, b -> a.value.compareTo(b.value) }
            .map { (k, v) -> "$k: $v" }
            .toList()
    }

    /**
     * Inserts [product] under [key] on behalf of [login].
     * If [key] already exists and belongs to the same user, the old product is replaced.
     * The product id is assigned by the database sequence, and the creation date is set
     * to the current server time.
     *
     * @param key the collection key
     * @param product the product to insert (id and creationDate are overwritten)
     * @param login the authenticated user performing the insert
     * @return the stored product with server-assigned fields
     * @throws IllegalArgumentException if the key is already occupied by another user's product
     */
    fun insert(key: String, product: Product, login: String): Product {
        val existing = products[key]
        if (existing != null && creatorLogins[existing.id] != login) {
            throw IllegalArgumentException("Key '$key' is already owned by another user")
        }
        if (existing != null) {
            databaseManager.deleteByKey(key)
            creatorLogins.remove(existing.id)
        }
        val creationDate = LocalDateTime.now().toString()
        val id = databaseManager.insertProduct(key, product.copy(creationDate = creationDate), login)
        val stamped = product.copy(id = id, creationDate = creationDate)
        products[key] = stamped
        creatorLogins[id] = login
        return stamped
    }

    /**
     * Updates the product whose id matches [id], preserving its original id and creation date.
     * Only the product's creator may update it.
     *
     * @param id the target product id
     * @param product new product data
     * @param login the authenticated user performing the update
     * @return the updated product, or null if no product with [id] exists
     * @throws IllegalArgumentException if the product exists but belongs to another user
     */
    fun update(id: Long, product: Product, login: String): Product? {
        val entry = synchronized(products) { products.entries.find { it.value.id == id } }
            ?: return null
        if (creatorLogins[id] != login) {
            throw IllegalArgumentException("You do not own product with id $id")
        }
        val updated = product.copy(id = entry.value.id, creationDate = entry.value.creationDate)
        if (!databaseManager.updateProduct(id, updated, login)) return null
        products[entry.key] = updated
        return updated
    }

    /**
     * Removes the product stored under [key].
     * Only the product's creator may remove it.
     *
     * @param key the collection key to remove
     * @param login the authenticated user performing the removal
     * @return true if an element was removed, false if the key was absent
     * @throws IllegalArgumentException if the product at [key] belongs to another user
     */
    fun removeKey(key: String, login: String): Boolean {
        val product = products[key] ?: return false
        if (creatorLogins[product.id] != login) {
            throw IllegalArgumentException("You do not own the product at key '$key'")
        }
        if (!databaseManager.deleteProduct(product.id, login)) return false
        products.remove(key)
        creatorLogins.remove(product.id)
        return true
    }

    /**
     * Removes all products created by [login].
     * Products belonging to other users are not affected.
     *
     * @param login the user whose products should be cleared
     * @return the number of products removed
     */
    fun clear(login: String): Int {
        val toRemove = synchronized(products) {
            products.entries
                .filter { creatorLogins[it.value.id] == login }
                .map { it.key to it.value.id }
        }
        val count = databaseManager.deleteAllByCreator(login)
        toRemove.forEach { (key, id) ->
            products.remove(key)
            creatorLogins.remove(id)
        }
        return count
    }

    /**
     * Removes all products owned by [login] whose price is strictly less than [product]'s price.
     * Products belonging to other users are not affected.
     *
     * @param product the reference product for price comparison
     * @param login the authenticated user
     * @return the number of products removed
     */
    fun removeLower(product: Product, login: String): Int {
        val snapshot = synchronized(products) { products.entries.toList() }
        val toRemove = snapshot
            .filter { it.value < product && creatorLogins[it.value.id] == login }
            .map { it.key to it.value.id }
        val count = databaseManager.deleteLowerByCreator(product.price, login)
        toRemove.forEach { (key, id) ->
            products.remove(key)
            creatorLogins.remove(id)
        }
        return count
    }

    /**
     * Replaces the product at [key] with [product] only if [product] is greater.
     * Only the product's creator may trigger a replacement.
     *
     * @param key the collection key
     * @param product the candidate replacement
     * @param login the authenticated user
     * @return the stored product if replaced, null if not replaced or key absent
     * @throws IllegalArgumentException if the product at [key] belongs to another user
     */
    fun replaceIfGreater(key: String, product: Product, login: String): Product? {
        val existing = products[key] ?: return null
        if (creatorLogins[existing.id] != login) {
            throw IllegalArgumentException("You do not own the product at key '$key'")
        }
        if (product <= existing) return null
        val stamped = product.copy(id = existing.id, creationDate = existing.creationDate)
        if (!databaseManager.updateProduct(existing.id, stamped, login)) return null
        products[key] = stamped
        return stamped
    }

    /**
     * Removes all products owned by [login] whose key is lexicographically greater than [key].
     * Products belonging to other users are not affected.
     *
     * @param key the exclusive upper boundary key
     * @param login the authenticated user
     * @return the number of products removed
     */
    fun removeGreaterKey(key: String, login: String): Int {
        val snapshot = synchronized(products) { products.entries.toList() }
        val toRemove = snapshot
            .filter { it.key > key && creatorLogins[it.value.id] == login }
            .map { it.key to it.value.id }
        val count = databaseManager.deleteGreaterKeyByCreator(key, login)
        toRemove.forEach { (k, id) ->
            products.remove(k)
            creatorLogins.remove(id)
        }
        return count
    }

    /**
     * Counts products whose price exceeds [price].
     *
     * @param price the price threshold
     * @return the number of matching products
     */
    fun countGreaterThanPrice(price: Long): Long {
        val snapshot = synchronized(products) { products.values.toList() }
        return snapshot.stream().filter { it.price > price }.count()
    }

    /**
     * Collects all distinct [UnitOfMeasure] values present in the collection.
     *
     * @return set of distinct unit-of-measure values, nulls excluded
     */
    fun uniqueUnitOfMeasure(): Set<UnitOfMeasure> {
        val snapshot = synchronized(products) { products.values.toList() }
        return snapshot.stream()
            .map { it.unitOfMeasure }
            .filter { it != null }
            .map { it!! }
            .collect({ mutableSetOf() }, { s, v -> s.add(v) }, { a, b -> a.addAll(b) })
    }

    /**
     * Returns product prices sorted in descending order.
     *
     * @return list of prices from highest to lowest
     */
    fun pricesDescending(): List<Long> {
        val snapshot = synchronized(products) { products.values.toList() }
        return snapshot.stream()
            .sorted(Comparator.reverseOrder())
            .map { it.price }
            .toList()
    }
}