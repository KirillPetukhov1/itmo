package objects

import kotlinx.serialization.Serializable

/**
 * Represents the hair color of a [Person].
 */
@Serializable
enum class Color {
    GREEN,
    BLACK,
    BLUE,
    YELLOW,
    BROWN
}
