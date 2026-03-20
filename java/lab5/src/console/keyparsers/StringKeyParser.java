package src.console.keyparsers;

import src.baseabstractions.ParseKey;

/**
 * Parser for string keys.
 * Implements ParseKey interface to provide string key parsing functionality.
 */
public class StringKeyParser implements ParseKey<String> {
    /**
     * Returns the input string as the parsed object.
     *
     * @param value the string value to be parsed
     * @return the same string value
     */
    public String getParsedObject(String value) {
        return value;
    }
}