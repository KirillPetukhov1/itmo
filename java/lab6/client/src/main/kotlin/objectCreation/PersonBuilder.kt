package objectCreation

import abstractions.Builder
import objects.Color
import objects.Country
import objects.Person

/**
 * Builds a [Person] instance field by field.
 */
class PersonBuilder : Builder<Person> {

    private var name: String = ""
    private var height: Long = 1
    private var hairColor: Color = Color.GREEN
    private var nationality: Country? = null

    override fun reset() {
        name = ""
        height = 1
        hairColor = Color.GREEN
        nationality = null
    }

    /**
     * Sets the person's [name].
     *
     * @param name non-blank name
     * @return this builder
     */
    fun setName(name: String): PersonBuilder {
        this.name = name
        return this
    }

    /**
     * Sets the person's [height] in centimetres.
     *
     * @param height must be greater than 0
     * @return this builder
     */
    fun setHeight(height: Long): PersonBuilder {
        this.height = height
        return this
    }

    /**
     * Sets the person's [hairColor].
     *
     * @param hairColor the hair color value
     * @return this builder
     */
    fun setHairColor(hairColor: Color): PersonBuilder {
        this.hairColor = hairColor
        return this
    }

    /**
     * Sets the person's [nationality], which may be null.
     *
     * @param nationality the country or null
     * @return this builder
     */
    fun setNationality(nationality: Country?): PersonBuilder {
        this.nationality = nationality
        return this
    }

    override fun build(): Person = Person(
        name = name,
        height = height,
        hairColor = hairColor,
        nationality = nationality
    )
}
