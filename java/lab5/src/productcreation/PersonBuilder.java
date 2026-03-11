package src.productcreation;

import src.baseabstractions.Builder;
import src.baseobjects.Color;
import src.baseobjects.Country;
import src.baseobjects.Person;

public class PersonBuilder<T extends Person> implements Builder<T> {
    private T person;

    public PersonBuilder(T person) {
        this.person = person;
    }

    public PersonBuilder<T> setName(String name) {
        person.setName(name);
        return this;
    }

    public PersonBuilder<T> setHeight(long height) {
        person.setHeight(height);
        return this;
    }

    public PersonBuilder<T> setHairColor(Color hairColor) {
        person.setHairColor(hairColor);
        return this;
    }

    public PersonBuilder<T> setNationality(Country nationality) {
        person.setNationality(nationality);
        return this;
    }

    public void reset() {
        
    }

    public T create() {
        return person;
    }
}
