package src.productcreation;

import java.time.LocalDateTime;

import src.baseabstractions.AbstractDirector;
import src.baseobjects.Coordinates;
import src.baseobjects.Person;
import src.baseobjects.Product;
import src.baseobjects.UnitOfMeasure;

/**
 * Director class for constructing Product objects with predefined
 * configurations.
 * Provides convenience methods for building fully initialized Product
 * instances.
 *
 * @param <T> the type of ProductBuilder this director works with
 */
public class ProductDirector<T extends ProductBuilder<? extends Product>> extends AbstractDirector<T> {

    /**
     * Constructs a ProductDirector with the specified builder.
     *
     * @param builder the ProductBuilder instance to be used by this director
     */
    public ProductDirector(T builder) {
        super(builder);
    }

    /**
     * Creates a fully initialized Product with all fields set.
     *
     * @param id              the product ID
     * @param name            the product name
     * @param coordinates     the product coordinates
     * @param creationDate    the product creation date
     * @param price           the product price
     * @param partNumber      the product part number
     * @param manufactureCost the product manufacture cost
     * @param unitOfMeasure   the product unit of measure
     * @param owner           the product owner
     */
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