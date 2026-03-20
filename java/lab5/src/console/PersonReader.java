package src.console;

import java.util.Arrays;

import src.baseabstractions.ObjectReader;
import src.baseabstractions.ReaderWriter;
import src.baseobjects.Color;
import src.baseobjects.Country;
import src.baseobjects.Person;
import src.productcreation.PersonBuilder;

/**
 * Reader class for interactive console input of Person objects.
 * Provides methods to read and validate all Person fields from user input.
 *
 * @param <T> the type of Person object to be read, must extend Person
 */
public class PersonReader<T extends Person> extends ObjectReader<T> {

    /**
     * Constructs a PersonReader with the specified builder and reader/writer.
     *
     * @param builder      the PersonBuilder to use for constructing Person objects
     * @param readerWriter the reader/writer for console I/O operations
     */
    public PersonReader(PersonBuilder<T> builder, ReaderWriter readerWriter) {
        super(builder, readerWriter);
    }

    /**
     * Reads and validates the person's name from console input.
     * Continues prompting until a valid name is entered.
     */
    public void readName() {
        System.out.println("Введите имя обладателя: ");
        while (true) {
            try {
                String name = getReaderWriter().readLine();
                ((PersonBuilder<T>) getBuilder()).setName(name);
                return;
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Имя введёно неверно. Введите ещё раз: ");
            }
        }
    }

    /**
     * Reads and validates the person's height from console input.
     * Continues prompting until a valid height value is entered.
     */
    public void readHeight() {
        System.out.println("Введите рост обладателя: ");
        while (true) {
            try {
                long height = getReaderWriter().readLong();
                ((PersonBuilder<T>) getBuilder()).setHeight(height);
                return;
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Рост введён неверно. Введите ещё раз: ");
            }
        }
    }

    /**
     * Reads and validates the person's hair color from console input.
     * Displays available color options and continues prompting until a valid color
     * is entered.
     */
    public void readHairColor() {
        System.out.println("Введите цвет волос обладателя: " + Arrays.toString(Color.values()));
        while (true) {
            try {
                String result = getReaderWriter().readLine();
                Color hairColor = Color.valueOf(result.toUpperCase());
                ((PersonBuilder<T>) getBuilder()).setHairColor(hairColor);
                return;
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Цвет волос введён неверно. Введите ещё раз: ");
            }
        }
    }

    /**
     * Reads and validates the person's nationality from console input.
     * Displays available country options and allows empty input for null
     * nationality.
     * Continues prompting until a valid nationality is entered.
     */
    public void readNationality() {
        System.out.println("Введите национальность: " + Arrays.toString(Country.values()));
        while (true) {
            try {
                String result = getReaderWriter().readLine();
                if (result == "") {
                    ((PersonBuilder<T>) getBuilder()).setNationality(null);
                }
                Country nationality = Country.valueOf(result.toUpperCase());
                ((PersonBuilder<T>) getBuilder()).setNationality(nationality);
                return;
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Национальность введёна неверно. Введите ещё раз: ");
            }
        }
    }
}