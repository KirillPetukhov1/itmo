package src.workwithfiles;

import src.baseabstractions.ObjectReader;
import src.baseabstractions.ReaderWriter;
import src.baseobjects.Coordinates;
import src.baseobjects.Product;
import src.baseobjects.UnitOfMeasure;
import src.productcreation.ProductBuilder;

public class ProductFileReader<T extends Product> extends ObjectReader<T> {

    public ProductFileReader(ProductBuilder<T> builder, ReaderWriter readerWriter) {
        super(builder, readerWriter);
    }

    public void readName() {
        String name = getReaderWriter().readLine();
        ((ProductBuilder<T>) getBuilder()).setName(name);
    }

    public void readCoordinates() {
        Long x = null;
        Double y = null;

        x = getReaderWriter().readLong();

        y = getReaderWriter().readDouble();

        ((ProductBuilder<T>) getBuilder()).setCoordinates(new Coordinates(x, y));
    }

    public void readPrice() {
        long price = getReaderWriter().readLong();
        ((ProductBuilder<T>) getBuilder()).setPrice(price);
    }

    public void readPartNumber() {
        String partNumber = getReaderWriter().readLine();
        ((ProductBuilder<T>) getBuilder()).setPartNumber(partNumber);
    }

    public void readManufactureCost() {
        Long manufactureCost = getReaderWriter().readLong();
        ((ProductBuilder<T>) getBuilder()).setManufactureCost(manufactureCost);
    }

    public void readUnitOfMeasure() {
        String result = getReaderWriter().readLine();
        if (result == "") {
            ((ProductBuilder<T>) getBuilder()).setUnitOfMeasure(null);
        }
        UnitOfMeasure unitOfMeasure = UnitOfMeasure.valueOf(result.toUpperCase());
        ((ProductBuilder<T>) getBuilder()).setUnitOfMeasure(unitOfMeasure);
    }
}
