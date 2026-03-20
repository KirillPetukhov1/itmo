package src.workwithfiles;

import src.baseabstractions.ObjectReader;
import src.baseabstractions.ReaderWriter;
import src.baseobjects.Coordinates;
import src.baseobjects.Product;
import src.baseobjects.UnitOfMeasure;
import src.productcreation.ProductBuilder;

/**
 * File-based reader class for reading Product objects from script files.
 * Provides methods to read Product fields directly from file input without
 * validation prompts.
 *
 * @param <T> the type of Product object to be read, must extend Product
 */
public class ProductFileReader<T extends Product> extends ObjectReader<T> {

    /**
     * Constructs a ProductFileReader with the specified builder and reader/writer.
     *
     * @param builder      the ProductBuilder to use for constructing Product
     *                     objects
     * @param readerWriter the reader/writer for file I/O operations
     */
    public ProductFileReader(ProductBuilder<T> builder, ReaderWriter readerWriter) {
        super(builder, readerWriter);
    }

    /**
     * Reads the product name from file and sets it in the builder.
     */
    public void readName() {
        String name = getReaderWriter().readLine();
        ((ProductBuilder<T>) getBuilder()).setName(name);
    }

    /**
     * Reads the product coordinates from file and sets them in the builder.
     * Reads X coordinate as Long and Y coordinate as Double from file.
     */
    public void readCoordinates() {
        Long x = null;
        Double y = null;

        x = getReaderWriter().readLong();

        y = getReaderWriter().readDouble();

        ((ProductBuilder<T>) getBuilder()).setCoordinates(new Coordinates(x, y));
    }

    /**
     * Reads the product price from file and sets it in the builder.
     */
    public void readPrice() {
        long price = getReaderWriter().readLong();
        ((ProductBuilder<T>) getBuilder()).setPrice(price);
    }

    /**
     * Reads the product part number from file and sets it in the builder.
     */
    public void readPartNumber() {
        String partNumber = getReaderWriter().readLine();
        ((ProductBuilder<T>) getBuilder()).setPartNumber(partNumber);
    }

    /**
     * Reads the product manufacture cost from file and sets it in the builder.
     */
    public void readManufactureCost() {
        Long manufactureCost = getReaderWriter().readLong();
        ((ProductBuilder<T>) getBuilder()).setManufactureCost(manufactureCost);
    }

    /**
     * Reads the product unit of measure from file and sets it in the builder.
     * Allows empty input to set unit of measure to null.
     * Converts non-empty input to the corresponding UnitOfMeasure enum value.
     */
    public void readUnitOfMeasure() {
        String result = getReaderWriter().readLine();
        if (result == "") {
            ((ProductBuilder<T>) getBuilder()).setUnitOfMeasure(null);
        }
        UnitOfMeasure unitOfMeasure = UnitOfMeasure.valueOf(result.toUpperCase());
        ((ProductBuilder<T>) getBuilder()).setUnitOfMeasure(unitOfMeasure);
    }
}