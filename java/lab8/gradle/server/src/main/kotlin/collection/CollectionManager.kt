package collection

import objectCreation.IdManager
import objects.Product
import objects.UnitOfMeasure
import java.time.LocalDateTime
import java.util.Hashtable

/**
 * Manages the [Hashtable] collection of [Product] objects.
 * All mutation and query operations are implemented with the Stream API and lambda expressions.
 *
 * @property idManager the manager responsible for generating and tracking product IDs
 * @property initDateTime the moment the collection was created in memory
 */
class CollectionManager(val idManager: IdManager = IdManager()) {

    val products: Hashtable<String, Product> = Hashtable()
    val initDateTime: LocalDateTime = LocalDateTime.now()

    /**
     * Loads [initialProducts] into the collection and registers their IDs.
     *
     * @param initialProducts products read from persistent storage
     */
    fun load(initialProducts: Hashtable<String, Product>) {
        products.clear()
        initialProducts.forEach { (key, product) ->
            products[key] = product
            idManager.registerId(product.id)
        }
    }

    /**
     * Returns a string describing the collection type, initialisation date and element count.
     *
     * @return human-readable info string
     */
    fun info(): String =
        "Type: ${products.javaClass.simpleName}, " +
        "initialized: $initDateTime, " +
        "elements: ${products.size}"

    /**
     * Returns all elements sorted by default order ([Product.compareTo]) as a list of strings.
     *
     * @return list of "key: product" strings sorted by product price
     */
    fun show(): List<String> =
        products.entries
            .stream()
            .sorted { a, b -> a.value.compareTo(b.value) }
            .map { (k, v) -> "$k: $v" }
            .toList()

    /**
     * Inserts a new product under [key], stamping it with a generated ID and creation date.
     * If [key] already exists, the old product's ID is released before insertion.
     *
     * @param key the collection key
     * @param product the product to insert (id and creationDate will be overwritten)
     * @return the stored product with server-assigned fields
     */
    fun insert(key: String, product: Product): Product {
        products[key]?.let { idManager.releaseId(it.id) }
        val stamped = product.copy(
            id = idManager.nextId(),
            creationDate = LocalDateTime.now().toString()
        )
        products[key] = stamped
        return stamped
    }

    /**
     * Updates the product whose [id] matches [id], replacing it with [product].
     * The original ID and creation date are preserved.
     *
     * @param id the target product ID
     * @param product new product data
     * @return the updated product, or null if no product with [id] exists
     */
    fun update(id: Long, product: Product): Product? {
        val entry = products.entries.stream()
            .filter { it.value.id == id }
            .findFirst()
            .orElse(null) ?: return null

        val updated = product.copy(id = entry.value.id, creationDate = entry.value.creationDate)
        products[entry.key] = updated
        return updated
    }

    /**
     * Removes the product stored under [key].
     *
     * @param key the collection key to remove
     * @return true if an element was removed, false if the key was absent
     */
    fun removeKey(key: String): Boolean {
        val removed = products.remove(key) ?: return false
        idManager.releaseId(removed.id)
        return true
    }

    /**
     * Removes all elements from the collection and resets the ID manager.
     */
    fun clear() {
        products.clear()
        idManager.clear()
    }

    /**
     * Removes all products whose price is strictly less than [product]'s price.
     *
     * @param product the reference product for comparison
     * @return the number of removed elements
     */
    fun removeLower(product: Product): Int {
        val toRemove = products.entries.stream()
            .filter { it.value < product }
            .map { it.key }
            .toList()
        toRemove.forEach { key ->
            products.remove(key)?.let { idManager.releaseId(it.id) }
        }
        return toRemove.size
    }

    /**
     * Replaces the product under [key] with [product] only if [product] is greater.
     *
     * @param key the collection key
     * @param product the candidate replacement
     * @return the stored product if replaced, null if not replaced or key absent
     */
    fun replaceIfGreater(key: String, product: Product): Product? {
        val existing = products[key] ?: return null
        if (product <= existing) return null
        val stamped = product.copy(id = existing.id, creationDate = existing.creationDate)
        products[key] = stamped
        return stamped
    }

    /**
     * Removes all products whose key is lexicographically greater than [key].
     *
     * @param key the upper boundary key (exclusive)
     * @return the number of removed elements
     */
    fun removeGreaterKey(key: String): Int {
        val toRemove = products.keys.stream()
            .filter { it > key }
            .toList()
        toRemove.forEach { k ->
            products.remove(k)?.let { idManager.releaseId(it.id) }
        }
        return toRemove.size
    }

    /**
     * Counts products whose price exceeds [price].
     *
     * @param price the price threshold
     * @return the number of matching products
     */
    fun countGreaterThanPrice(price: Long): Long =
        products.values.stream()
            .filter { it.price > price }
            .count()

    /**
     * Collects all unique [UnitOfMeasure] values present in the collection.
     *
     * @return set of distinct unit-of-measure values (nulls excluded)
     */
    fun uniqueUnitOfMeasure(): Set<UnitOfMeasure> =
        products.values.stream()
            .map { it.unitOfMeasure }
            .filter { it != null }
            .map { it!! }
            .collect({ mutableSetOf() }, { s, v -> s.add(v) }, { a, b -> a.addAll(b) })

    /**
     * Returns product prices sorted in descending order.
     *
     * @return list of prices from highest to lowest
     */
    fun pricesDescending(): List<Long> =
        products.values.stream()
            .sorted(Comparator.reverseOrder())
            .map { it.price }
            .toList()
}
