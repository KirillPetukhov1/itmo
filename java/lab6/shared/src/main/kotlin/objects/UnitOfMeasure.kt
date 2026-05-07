package objects

import kotlinx.serialization.Serializable

/**
 * Represents the unit of measure for a [Product].
 */
@Serializable
enum class UnitOfMeasure {
    SQUARE_METERS,
    MILLILITERS,
    GRAMS
}
