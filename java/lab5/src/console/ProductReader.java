package src.console;

import java.util.Arrays;

import src.baseabstractions.ObjectReader;
import src.baseabstractions.ReaderWriter;
import src.baseobjects.Coordinates;
import src.baseobjects.Product;
import src.baseobjects.UnitOfMeasure;
import src.productcreation.ProductBuilder;

public class ProductReader<T extends Product> extends ObjectReader<T> {

    public ProductReader(ProductBuilder<T> builder, ReaderWriter readerWriter) {
        super(builder, readerWriter);
    }

    public void readName() {
        System.out.println("Введите название продукта: ");
        while (true) {
            try {
                String name = getReaderWriter().readLine();
                ((ProductBuilder<T>) getBuilder()).setName(name);
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Имя введёно неверно. Введите ещё раз: ");
            }
        }
    }

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
            System.out.println("Координата X больше 321. Введите ещё раз: ");
        }
    }

    public void readPrice() {
        System.out.println("Введите цену: ");
        while (true) {
            try {
                long price = getReaderWriter().readLong();
                ((ProductBuilder<T>) getBuilder()).setPrice(price);
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Цена введёна неверно. Введите ещё раз: ");
            }
        }
    }

    public void readPartNumber() {
        System.out.println("Введите номер партии: ");
        while (true) {
            try {
                String partNumber = getReaderWriter().readLine();
                ((ProductBuilder<T>) getBuilder()).setPartNumber(partNumber);
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Номер партии введён неверно. Введите ещё раз: ");
            }
        }
    }

    public void readManufactureCost() {
        System.out.println("Введите цену производства: ");
        while (true) {
            try {
                Long manufactureCost = getReaderWriter().readLong();
                ((ProductBuilder<T>) getBuilder()).setManufactureCost(manufactureCost);
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Цена производства введёна неверно. Введите ещё раз: ");
            }
        }
    }

    public void readUnitOfMeasure() {
        System.out.println("Введите единицу измерения: " + Arrays.toString(UnitOfMeasure.values()));
        while (true) {
            try {
                String result = getReaderWriter().readLine();
                if (result == "") {
                    ((ProductBuilder<T>) getBuilder()).setUnitOfMeasure(null);
                }
                UnitOfMeasure unitOfMeasure = UnitOfMeasure.valueOf(result.toUpperCase());
                ((ProductBuilder<T>) getBuilder()).setUnitOfMeasure(unitOfMeasure);
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Единица измерения введёна неверно. Введите ещё раз: ");
            }
        }
    }
}
