package src.workwithfiles;

import src.baseabstractions.ObjectReader;
import src.baseabstractions.ReaderWriter;
import src.baseobjects.Color;
import src.baseobjects.Country;
import src.baseobjects.Person;
import src.productcreation.PersonBuilder;

/**
 * File-based reader class for reading Person objects from script files.
 * Provides methods to read Person fields directly from file input without
 * validation prompts.
 *
 * @param <T> the type of Person object to be read, must extend Person
 */
public class PersonFileReader<T extends Person> extends ObjectReader<T> {

    /**
     * Constructs a PersonFileReader with the specified builder and reader/writer.
     *
     * @param builder      the PersonBuilder to use for constructing Person objects
     * @param readerWriter the reader/writer for file I/O operations
     */
    public PersonFileReader(PersonBuilder<T> builder, ReaderWriter readerWriter) {
        super(builder, readerWriter);
    }

    /**
     * Reads the person's name from file and sets it in the builder.
     */
    public void readName() {
        String name = getReaderWriter().readLine();
        ((PersonBuilder<T>) getBuilder()).setName(name);
    }

    /**
     * Reads the person's height from file and sets it in the builder.
     */
    public void readHeight() {
        long height = getReaderWriter().readLong();
        ((PersonBuilder<T>) getBuilder()).setHeight(height);
    }

    /**
     * Reads the person's hair color from file and sets it in the builder.
     * Converts the input string to the corresponding Color enum value.
     */
    public void readHairColor() {
        String result = getReaderWriter().readLine();
        Color hairColor = Color.valueOf(result.toUpperCase());
        ((PersonBuilder<T>) getBuilder()).setHairColor(hairColor);
    }

    /**
     * Reads the person's nationality from file and sets it in the builder.
     * Allows empty input to set nationality to null.
     * Converts non-empty input to the corresponding Country enum value.
     */
    public void readNationality() {
        String result = getReaderWriter().readLine();
        if (result == "") {
            ((PersonBuilder<T>) getBuilder()).setNationality(null);
        }
        Country nationality = Country.valueOf(result.toUpperCase());
        ((PersonBuilder<T>) getBuilder()).setNationality(nationality);
    }
}