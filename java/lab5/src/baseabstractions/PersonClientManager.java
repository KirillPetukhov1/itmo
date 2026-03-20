package src.baseabstractions;

import src.baseobjects.Person;

/**
 * Interface for client managers that handle Person objects.
 *
 * @param <T> the type of Person object this manager handles, must extend Person
 */
public interface PersonClientManager<T extends Person> {
    /**
     * Retrieves a Person object.
     *
     * @return the Person object of type T
     */
    T getPerson();
}