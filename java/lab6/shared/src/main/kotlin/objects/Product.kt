package objects

import kotlinx.serialization.Serializable

/**
 * Represents a product stored in the collection.
 * Default ordering is by [price] ascending.
 *
 * @property id unique identifier, generated automatically on the server, must be greater than 0
 * @property name product name, must not be blank
 * @property coordinates product coordinates, must not be null
 * @property creationDate creation date string, generated automatically on the server
 * @property price product price, must be greater than 0
 * @property partNumber product part number, may be null
 * @property manufactureCost manufacture cost, may be null
 * @property unitOfMeasure unit of measure, may be null
 * @property owner product owner, must not be null
 */
@Serializable
data class Product(
    val id: Long = 0,
    val name: String,
    val coordinates: Coordinates,
    val creationDate: String = "",
    val price: Long,
    val partNumber: String? = null,
    val manufactureCost: Long? = null,
    val unitOfMeasure: UnitOfMeasure? = null,
    val owner: Person
) : Comparable<Product> {
    init {
        require(name.isNotBlank()) { "Product name must not be blank" }
        require(price > 0) { "Product price must be greater than 0" }
    }

    override fun compareTo(other: Product): Int = compareValuesBy(this, other, Product::price)
}
