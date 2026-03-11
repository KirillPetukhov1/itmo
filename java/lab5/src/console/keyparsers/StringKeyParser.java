package src.console.keyparsers;

import src.baseabstractions.ParseKey;

public class StringKeyParser implements ParseKey<String> {
    public String getParsedObject(String value) {
        return value;
    }
}
