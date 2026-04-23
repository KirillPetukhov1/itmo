package objects

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
open class Product {
    var id: Long = 0

    var name: String = ""
        set(value: String) {
            if (value.isEmpty()) {
                throw IllegalArgumentException("Name can't be empty")
            }
            field = value.trim()
        }

    var coordinates: Coordinates = Coordinates()

    var creationDate: Instant = Instant.fromEpochMilliseconds(0L)

    var price: Long = 0
        set(value: Long) {
            if (value <= 0) {
                throw IllegalArgumentException("Price can't be less than zero")
            }
            field = value
        }

    var partNumber: String? = null

    var manufactureCost: Long? = null

    var unitOfMeasure: UnitOfMeasure? = null

    var owner: Person = Person()
}