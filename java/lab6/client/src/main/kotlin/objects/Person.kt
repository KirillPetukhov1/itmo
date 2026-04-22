package objects

import kotlinx.serialization.Serializable

@Serializable
open class Person() {
    var name: String = ""
        set(value: String) {
            if (value.isEmpty()) throw IllegalArgumentException("Name can't be empty")
            field = value.trim()
        }

    var height: Long = 0L
        set(value: Long) {
            if (value <= 0) throw IllegalArgumentException("Height can't be less than zero")
            field = value
        }

    var hairColor: Color = Color.GREEN

    var nationality: Country? = null
}