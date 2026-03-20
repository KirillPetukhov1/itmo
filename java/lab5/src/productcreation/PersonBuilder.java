package src.productcreation;

import src.baseabstractions.Builder;
import src.baseobjects.Color;
import src.baseobjects.Country;
import src.baseobjects.Person;

/**
 * Builder class for constructing Person objects with a fluent interface.
 *
 * @param <T> the type of Person object to be built, must extend Person
 */
public class PersonBuilder<T extends Person> implements Builder<T> {
    private T person;

    /**
     * Constructs a PersonBuilder initialized with the specified person.
     *
     * @param person the person object to be used as the base for building
     */
    public PersonBuilder(T person) {
        this.person = person;
    }

    /**
     * Sets the name of the person.
     *
     * @param name the name to set
     * @return this builder instance for method chaining
     */
    public PersonBuilder<T> setName(String name) {
        person.setName(name);
        return this;
    }

    /**
     * Sets the height of the person.
     *
     * @param height the height to set
     * @return this builder instance for method chaining
     */
    public PersonBuilder<T> setHeight(long height) {
        person.setHeight(height);
        return this;
    }

    /**
     * Sets the hair color of the person.
     *
     * @param hairColor the hair color to set
     * @return this builder instance for method chaining
     */
    public PersonBuilder<T> setHairColor(Color hairColor) {
        person.setHairColor(hairColor);
        return this;
    }

    /**
     * Sets the nationality of the person.
     *
     * @param nationality the nationality to set
     * @return this builder instance for method chaining
     */
    public PersonBuilder<T> setNationality(Country nationality) {
        person.setNationality(nationality);
        return this;
    }

    /**
     * Resets the builder to its initial state.
     * Currently does nothing as the builder uses an existing person reference.
     */
    public void reset() {

    }

    /**
     * Returns the constructed person object.
     *
     * @return the built person of type T
     */
    public T create() {
        return person;
    }
}