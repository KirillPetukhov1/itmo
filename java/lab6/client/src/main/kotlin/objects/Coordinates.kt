package objects

import kotlinx.serialization.Serializable

@Serializable
open class Coordinates() {
    var x: Long = 0L
        set(value) {
            if (value > 321) {
                throw IllegalArgumentException("x must be greater than 321")
            }
            field = value
        }

    var y: Double = 0.0

    constructor(x: Long, y: Double) : this() {
        this.x = x
        this.y = y
    }
}