package system.exceptions;

import entities.*;

public class PersonIsUnavailableException extends Exception {
    
    public PersonIsUnavailableException(Person person) {
        super("Person " + person + " находится вне зоны доступа.");
    }
}
