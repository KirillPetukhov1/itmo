package objects

import kotlinx.serialization.Serializable

/**
 * Represents the nationality of a [Person].
 */
@Serializable
enum class Country {
    SPAIN,
    VATICAN,
    JAPAN
}
