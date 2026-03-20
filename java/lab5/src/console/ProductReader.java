package src.console;

import java.util.Arrays;

import src.baseabstractions.ObjectReader;
import src.baseabstractions.ReaderWriter;
import src.baseobjects.Coordinates;
import src.baseobjects.Product;
import src.baseobjects.UnitOfMeasure;
import src.productcreation.ProductBuilder;

/**
 * Reader class for interactive console input of Product objects.
 * Provides methods to read and validate all Product fields from user input.
 *
 * @param <T> the type of Product object to be read, must extend Product
 */
public class ProductReader<T extends Product> extends ObjectReader<T> {

    /**
     * Constructs a ProductReader with the specified builder and reader/writer.
     *
     * @param builder      the ProductBuilder to use for constructing Product
     *                     objects
     * @param readerWriter the reader/writer for console I/O operations
     */
    public ProductReader(ProductBuilder<T> builder, ReaderWriter readerWriter) {
        super(builder, readerWriter);
    }

    /**
     * Reads and validates the product name from console input.
     * Recursively prompts until a valid name is entered.
     */
    public void readName() {
        System.out.println("Введите название продукта: ");
        try {
            String name = getReaderWriter().readLine();
            ((ProductBuilder<T>) getBuilder()).setName(name);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("Имя введёно неверно. Введите ещё раз.");
            readName();
        }
    }

    /**
     * Reads and validates the product coordinates from console input.
     * Handles X and Y coordinates separately with validation for X <= 321.
     * Recursively prompts if validation fails.
     */
    public void readCoordinates() {
        Long x = null;
        Double y = null;

        try {
            System.out.println("Введите координату X: ");
            while (x == null) {
                try {
                    x = getReaderWriter().readLong();
                } catch (NumberFormatException | NullPointerException e) {
                    System.out.println("Число введено неверно. Введите ещё раз: ");
                }
            }

            System.out.println("Введите координату Y: ");
            while (y == null) {
                try {
                    y = getReaderWriter().readDouble();
                } catch (NumberFormatException | NullPointerException e) {
                    System.out.println("Число введено неверно. Введите ещё раз: ");
                }
            }

            ((ProductBuilder<T>) getBuilder()).setCoordinates(new Coordinates(x, y));
        } catch (IllegalArgumentException e) {
            System.out.println("Координата X больше 321. Введите ещё раз.");
            readCoordinates();
        }
    }

    /**
     * Reads and validates the product price from console input.
     * Recursively prompts until a valid price is entered.
     */
    public void readPrice() {
        System.out.println("Введите цену: ");
        try {
            long price = getReaderWriter().readLong();
            ((ProductBuilder<T>) getBuilder()).setPrice(price);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("Цена введёна неверно. Введите ещё раз.");
            readPrice();
        }

    }

    /**
     * Reads and validates the product part number from console input.
     * Recursively prompts until a valid part number is entered.
     */
    public void readPartNumber() {
        System.out.println("Введите номер партии: ");
        try {
            String partNumber = getReaderWriter().readLine();
            ((ProductBuilder<T>) getBuilder()).setPartNumber(partNumber);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("Номер партии введён неверно. Введите ещё раз.");
            readPartNumber();
        }
    }

    /**
     * Reads and validates the product manufacture cost from console input.
     * Recursively prompts until a valid cost value is entered.
     */
    public void readManufactureCost() {
        System.out.println("Введите цену производства: ");
        try {
            Long manufactureCost = getReaderWriter().readLong();
            ((ProductBuilder<T>) getBuilder()).setManufactureCost(manufactureCost);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("Цена производства введёна неверно. Введите ещё раз: ");
            readManufactureCost();
        }
    }

    /**
     * Reads and validates the product unit of measure from console input.
     * Displays available options and allows empty input for null value.
     * Recursively prompts until a valid unit is entered.
     */
    public void readUnitOfMeasure() {
        System.out.println("Введите единицу измерения: " + Arrays.toString(UnitOfMeasure.values()));
        try {
            String result = getReaderWriter().readLine();
            if (result == "") {
                ((ProductBuilder<T>) getBuilder()).setUnitOfMeasure(null);
            }
            UnitOfMeasure unitOfMeasure = UnitOfMeasure.valueOf(result.toUpperCase());
            ((ProductBuilder<T>) getBuilder()).setUnitOfMeasure(unitOfMeasure);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("Единица измерения введёна неверно. Введите ещё раз: ");
            readUnitOfMeasure();
        }
    }
}