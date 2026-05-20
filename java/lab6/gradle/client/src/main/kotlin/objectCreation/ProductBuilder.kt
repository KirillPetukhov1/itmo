package objectCreation

import abstractions.Builder
import objects.Coordinates
import objects.Person
import objects.Product
import objects.UnitOfMeasure

/**
 * Builds a [Product] instance field by field.
 * The [id] and [creationDate] fields are not set here — they are assigned by the server.
 */
class ProductBuilder : Builder<Product> {

    private var name: String = ""
    private var coordinates: Coordinates = Coordinates(0, 0.0)
    private var price: Long = 1
    private var partNumber: String? = null
    private var manufactureCost: Long? = null
    private var unitOfMeasure: UnitOfMeasure? = null
    private var owner: Person = Person("placeholder", 1, objects.Color.GREEN)

    override fun reset() {
        name = ""
        coordinates = Coordinates(0, 0.0)
        price = 1
        partNumber = null
        manufactureCost = null
        unitOfMeasure = null
        owner = Person("placeholder", 1, objects.Color.GREEN)
    }

    /**
     * Sets the product [name].
     *
     * @param name non-blank product name
     * @return this builder
     */
    fun setName(name: String): ProductBuilder {
        this.name = name
        return this
    }

    /**
     * Sets the product [coordinates].
     *
     * @param coordinates non-null coordinates
     * @return this builder
     */
    fun setCoordinates(coordinates: Coordinates): ProductBuilder {
        this.coordinates = coordinates
        return this
    }

    /**
     * Sets the product [price].
     *
     * @param price must be greater than 0
     * @return this builder
     */
    fun setPrice(price: Long): ProductBuilder {
        this.price = price
        return this
    }

    /**
     * Sets the product [partNumber], which may be null.
     *
     * @param partNumber the part number string or null
     * @return this builder
     */
    fun setPartNumber(partNumber: String?): ProductBuilder {
        this.partNumber = partNumber
        return this
    }

    /**
     * Sets the [manufactureCost], which may be null.
     *
     * @param manufactureCost the cost or null
     * @return this builder
     */
    fun setManufactureCost(manufactureCost: Long?): ProductBuilder {
        this.manufactureCost = manufactureCost
        return this
    }

    /**
     * Sets the [unitOfMeasure], which may be null.
     *
     * @param unitOfMeasure the unit or null
     * @return this builder
     */
    fun setUnitOfMeasure(unitOfMeasure: UnitOfMeasure?): ProductBuilder {
        this.unitOfMeasure = unitOfMeasure
        return this
    }

    /**
     * Sets the product [owner].
     *
     * @param owner non-null person
     * @return this builder
     */
    fun setOwner(owner: Person): ProductBuilder {
        this.owner = owner
        return this
    }

    override fun build(): Product = Product(
        name = name,
        coordinates = coordinates,
        price = price,
        partNumber = partNumber,
        manufactureCost = manufactureCost,
        unitOfMeasure = unitOfMeasure,
        owner = owner
    )
}
