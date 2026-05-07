package objects

import kotlinx.serialization.Serializable

/**
 * Represents the geographic coordinates of a [Product].
 *
 * @property x the X coordinate, must not exceed 321
 * @property y the Y coordinate
 */
@Serializable
data class Coordinates(val x: Long, val y: Double) {
    init {
        require(x <= 321) { "X coordinate must not exceed 321" }
    }
}
