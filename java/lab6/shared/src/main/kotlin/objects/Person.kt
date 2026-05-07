package objects

import kotlinx.serialization.Serializable

/**
 * Represents the owner of a [Product].
 *
 * @property name the person's name, must not be blank
 * @property height the person's height in centimetres, must be greater than 0
 * @property hairColor the person's hair color, must not be null
 * @property nationality the person's nationality, may be null
 */
@Serializable
data class Person(
    val name: String,
    val height: Long,
    val hairColor: Color,
    val nationality: Country? = null
) {
    init {
        require(name.isNotBlank()) { "Person name must not be blank" }
        require(height > 0) { "Person height must be greater than 0" }
    }
}
