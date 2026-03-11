package src.productcreation;

import java.time.LocalDateTime;

import src.baseabstractions.Builder;
import src.baseobjects.Coordinates;
import src.baseobjects.Person;
import src.baseobjects.Product;
import src.baseobjects.UnitOfMeasure;

public class ProductBuilder<T extends Product> implements Builder<T> {
    private T product;

    public ProductBuilder(T product) {
        this.product = product;
    }

    public ProductBuilder<T> setId(long id) {
        product.setId(id);
        return this;
    }

    public ProductBuilder<T> setName(String name) {
        product.setName(name);
        return this;
    }

    public ProductBuilder<T> setCoordinates(Coordinates coordinates) {
        product.setCoordinates(coordinates);
        return this;
    }

    public ProductBuilder<T> setCreationDate(LocalDateTime creationDate) {
        product.setCreationDate(creationDate);
        return this;
    }

    public ProductBuilder<T> setPrice(long price) {
        product.setPrice(price);
        return this;
    }

    public ProductBuilder<T> setPartNumber(String partNumber) {
        product.setPartNumber(partNumber);
        return this;
    }

    public ProductBuilder<T> setManufactureCost(Long manufactureCost) {
        product.setManufactureCost(manufactureCost);
        return this;
    }

    public ProductBuilder<T> setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        product.setUnitOfMeasure(unitOfMeasure);
        return this;
    }

    public ProductBuilder<T> setOwner(Person owner) {
        product.setOwner(owner);
        return this;
    }

    public void reset() {

    }

    public T create() {
        return product;
    }

}
