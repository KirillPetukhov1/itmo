package src.baseabstractions;

import src.baseobjects.Person;

public interface PersonClientManager<T extends Person> {
    T getPerson();
}
