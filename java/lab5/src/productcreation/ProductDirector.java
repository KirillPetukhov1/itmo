package src.productcreation;

import java.time.LocalDateTime;

import src.baseabstractions.Director;
import src.baseobjects.Coordinates;
import src.baseobjects.Person;
import src.baseobjects.Product;
import src.baseobjects.UnitOfMeasure;

public class ProductDirector<T extends ProductBuilder<? extends Product>> extends Director<T> {

    public ProductDirector(T builder) {
        super(builder);
    }

    public void makeFull(long id, String name, Coordinates coordinates, LocalDateTime creationDate, long price,
            String partNumber, Long manufactureCost, UnitOfMeasure unitOfMeasure, Person owner) {
        getBuilder().setId(id)
                .setName(name)
                .setCoordinates(coordinates)
                .setCreationDate(creationDate)
                .setPrice(price)
                .setPartNumber(partNumber)
                .setManufactureCost(manufactureCost)
                .setUnitOfMeasure(unitOfMeasure)
                .setOwner(owner);
    }
}
