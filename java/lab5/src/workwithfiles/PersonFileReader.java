package src.workwithfiles;

import src.baseabstractions.ObjectReader;
import src.baseabstractions.ReaderWriter;
import src.baseobjects.Color;
import src.baseobjects.Country;
import src.baseobjects.Person;
import src.productcreation.PersonBuilder;

public class PersonFileReader<T extends Person> extends ObjectReader<T> {

    public PersonFileReader(PersonBuilder<T> builder, ReaderWriter readerWriter) {
        super(builder, readerWriter);
    }

    public void readName() {
        String name = getReaderWriter().readLine();
        ((PersonBuilder<T>) getBuilder()).setName(name);
    }

    public void readHeight() {
        long height = getReaderWriter().readLong();
        ((PersonBuilder<T>) getBuilder()).setHeight(height);
    }

    public void readHairColor() {
        String result = getReaderWriter().readLine();
        Color hairColor = Color.valueOf(result.toUpperCase());
        ((PersonBuilder<T>) getBuilder()).setHairColor(hairColor);
    }

    public void readNationality() {
        String result = getReaderWriter().readLine();
        if (result == "") {
            ((PersonBuilder<T>) getBuilder()).setNationality(null);
        }
        Country nationality = Country.valueOf(result.toUpperCase());
        ((PersonBuilder<T>) getBuilder()).setNationality(nationality);
    }
}
