package src.console;

import java.util.Arrays;

import src.baseabstractions.ObjectReader;
import src.baseabstractions.ReaderWriter;
import src.baseobjects.Color;
import src.baseobjects.Country;
import src.baseobjects.Person;
import src.productcreation.PersonBuilder;

public class PersonReader<T extends Person> extends ObjectReader<T> {

    public PersonReader(PersonBuilder<T> builder, ReaderWriter readerWriter) {
        super(builder, readerWriter);
    }
    
    public void readName() {
        System.out.println("Введите имя обладателя: ");
        while (true) {
            try {
                String name = getReaderWriter().readLine();
                ((PersonBuilder<T>) getBuilder()).setName(name);
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Имя введёно неверно. Введите ещё раз: ");
            }
        }
    }

    public void readHeight() {
        System.out.println("Введите рост обладателя: ");
        while (true) {
            try {
                long height = getReaderWriter().readLong();
                ((PersonBuilder<T>) getBuilder()).setHeight(height);
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Рост введён неверно. Введите ещё раз: ");
            }
        }
    }

        public void readHairColor() {
        System.out.println("Введите цвет волос обладателя: " + Arrays.toString(Color.values()));
        while (true) {
            try {
                String result = getReaderWriter().readLine();
                Color hairColor = Color.valueOf(result.toUpperCase());
                ((PersonBuilder<T>) getBuilder()).setHairColor(hairColor);
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Цвет волос введён неверно. Введите ещё раз: ");
            }
        }
    }

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
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Национальность введёна неверно. Введите ещё раз: ");
            }
        }
    }
}
