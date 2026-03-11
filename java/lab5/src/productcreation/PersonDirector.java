package src.productcreation;

import src.baseabstractions.AbstractDirector;
import src.baseobjects.Color;
import src.baseobjects.Country;
import src.baseobjects.Person;

public class PersonDirector<T extends PersonBuilder<? extends Person>> extends AbstractDirector<T> {

    public PersonDirector(T builder) {
        super(builder);
    }

    public void makeFull(String name, long height, Color hairColor, Country nationality) {
        getBuilder().setName(name)
                .setHeight(height)
                .setHairColor(hairColor)
                .setNationality(nationality);
    }
}
