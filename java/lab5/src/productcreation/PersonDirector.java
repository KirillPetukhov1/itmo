package src.productcreation;

import src.baseabstractions.AbstractDirector;
import src.baseobjects.Color;
import src.baseobjects.Country;
import src.baseobjects.Person;

/**
 * Director class for constructing Person objects with predefined
 * configurations.
 * Provides convenience methods for building fully initialized Person instances.
 *
 * @param <T> the type of PersonBuilder this director works with
 */
public class PersonDirector<T extends PersonBuilder<? extends Person>> extends AbstractDirector<T> {

    /**
     * Constructs a PersonDirector with the specified builder.
     *
     * @param builder the PersonBuilder instance to be used by this director
     */
    public PersonDirector(T builder) {
        super(builder);
    }

    /**
     * Creates a fully initialized Person with all fields set.
     *
     * @param name        the name of the person
     * @param height      the height of the person
     * @param hairColor   the hair color of the person
     * @param nationality the nationality of the person
     */
    public void makeFull(String name, long height, Color hairColor, Country nationality) {
        getBuilder().setName(name)
                .setHeight(height)
                .setHairColor(hairColor)
                .setNationality(nationality);
    }
}