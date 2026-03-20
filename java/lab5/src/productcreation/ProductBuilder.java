package src.productcreation;

import java.time.LocalDateTime;

import src.baseabstractions.Builder;
import src.baseobjects.Coordinates;
import src.baseobjects.Person;
import src.baseobjects.Product;
import src.baseobjects.UnitOfMeasure;

/**
 * Builder class for constructing Product objects with a fluent interface.
 *
 * @param <T> the type of Product object to be built, must extend Product
 */
public class ProductBuilder<T extends Product> implements Builder<T> {
    private T product;

    /**
     * Constructs a ProductBuilder initialized with the specified product.
     *
     * @param product the product object to be used as the base for building
     */
    public ProductBuilder(T product) {
        this.product = product;
    }

    /**
     * Sets the ID of the product.
     *
     * @param id the ID to set
     * @return this builder instance for method chaining
     */
    public ProductBuilder<T> setId(long id) {
        product.setId(id);
        return this;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the name to set
     * @return this builder instance for method chaining
     */
    public ProductBuilder<T> setName(String name) {
        product.setName(name);
        return this;
    }

    /**
     * Sets the coordinates of the product.
     *
     * @param coordinates the coordinates to set
     * @return this builder instance for method chaining
     */
    public ProductBuilder<T> setCoordinates(Coordinates coordinates) {
        product.setCoordinates(coordinates);
        return this;
    }

    /**
     * Sets the creation date of the product.
     *
     * @param creationDate the creation date to set
     * @return this builder instance for method chaining
     */
    public ProductBuilder<T> setCreationDate(LocalDateTime creationDate) {
        product.setCreationDate(creationDate);
        return this;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the price to set
     * @return this builder instance for method chaining
     */
    public ProductBuilder<T> setPrice(long price) {
        product.setPrice(price);
        return this;
    }

    /**
     * Sets the part number of the product.
     *
     * @param partNumber the part number to set
     * @return this builder instance for method chaining
     */
    public ProductBuilder<T> setPartNumber(String partNumber) {
        product.setPartNumber(partNumber);
        return this;
    }

    /**
     * Sets the manufacture cost of the product.
     *
     * @param manufactureCost the manufacture cost to set
     * @return this builder instance for method chaining
     */
    public ProductBuilder<T> setManufactureCost(Long manufactureCost) {
        product.setManufactureCost(manufactureCost);
        return this;
    }

    /**
     * Sets the unit of measure of the product.
     *
     * @param unitOfMeasure the unit of measure to set
     * @return this builder instance for method chaining
     */
    public ProductBuilder<T> setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        product.setUnitOfMeasure(unitOfMeasure);
        return this;
    }

    /**
     * Sets the owner of the product.
     *
     * @param owner the owner to set
     * @return this builder instance for method chaining
     */
    public ProductBuilder<T> setOwner(Person owner) {
        product.setOwner(owner);
        return this;
    }

    /**
     * Resets the builder to its initial state.
     * Currently does nothing as the builder uses an existing product reference.
     */
    public void reset() {

    }

    /**
     * Returns the constructed product object.
     *
     * @return the built product of type T
     */
    public T create() {
        return product;
    }

}