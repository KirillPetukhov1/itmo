package src.baseobjects;

import java.time.LocalDateTime;

import src.productcreation.IdManager;

/**
 * Represents a product with various attributes such as id, name, coordinates,
 * creation date, price, part number, manufacture cost, unit of measure, and
 * owner.
 * Provides getters and setters with validation for certain fields.
 *
 * @author Petukhov Kirill
 */
public class Product {
    /** The unique identifier of the product (must be positive). */
    private long id;
    /** The name of the product (must not be null or empty). */
    private String name;
    /** The coordinates of the product (must not be null). */
    private Coordinates coordinates;
    /** The creation date of the product (must not be null). */
    private LocalDateTime creationDate;
    /** The price of the product (must be positive). */
    private long price;
    /** The part number of the product (can be null). */
    private String partNumber;
    /** The manufacture cost of the product (can be null). */
    private Long manufactureCost;
    /** The unit of measure of the product (can be null). */
    private UnitOfMeasure unitOfMeasure;
    /** The owner of the product (must not be null). */
    private Person owner;

    /**
     * Returns the product's id.
     *
     * @return the id
     */
    public long getId() {
        return this.id;
    }

    /**
     * Sets the product's id.
     *
     * @param id the new id (must be > 0 and uniq)
     * @throws IllegalArgumentException if id <= 0
     */
    public void setId(long id) {
        if (new IdManager().isIdExists(this))
            throw new IllegalArgumentException("Invalid id, id must be uniq");
        if (id <= 0)
            throw new IllegalArgumentException("Invalid id, id must be > 0");
        this.id = id;
    }

    /**
     * Returns the product's name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the product's name.
     *
     * @param name the new name (must not be null or empty)
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        if (name == null || name == "")
            throw new IllegalArgumentException("Invalid name");
        this.name = name;
    }

    /**
     * Returns the product's coordinates.
     *
     * @return the coordinates
     */
    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    /**
     * Sets the product's coordinates.
     *
     * @param coordinates the new coordinates (must not be null)
     * @throws IllegalArgumentException if coordinates is null
     */
    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null)
            throw new IllegalArgumentException("Invalid coordinates");
        this.coordinates = coordinates;
    }

    /**
     * Returns the product's creation date.
     *
     * @return the creation date
     */
    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    /**
     * Sets the product's creation date.
     *
     * @param creationDate the new creation date (must not be null)
     * @throws IllegalArgumentException if creationDate is null
     */
    public void setCreationDate(LocalDateTime creationDate) {
        if (creationDate == null)
            throw new IllegalArgumentException("Invalid creation date");
        this.creationDate = creationDate;
    }

    /**
     * Returns the product's price.
     *
     * @return the price
     */
    public long getPrice() {
        return this.price;
    }

    /**
     * Sets the product's price.
     *
     * @param price the new price (must be > 0)
     * @throws IllegalArgumentException if price <= 0
     */
    public void setPrice(long price) {
        if (price <= 0)
            throw new IllegalArgumentException("Invalid price, price must be > 0");
        this.price = price;
    }

    /**
     * Returns the product's part number.
     *
     * @return the part number (may be null)
     */
    public String getPartNumber() {
        return this.partNumber;
    }

    /**
     * Sets the product's part number.
     *
     * @param partNumber the new part number (can be null)
     */
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    /**
     * Returns the product's manufacture cost.
     *
     * @return the manufacture cost (may be null)
     */
    public Long getManufactureCost() {
        return this.manufactureCost;
    }

    /**
     * Sets the product's manufacture cost.
     *
     * @param manufactureCost the new manufacture cost (can be null)
     */
    public void setManufactureCost(Long manufactureCost) {
        this.manufactureCost = manufactureCost;
    }

    /**
     * Returns the product's unit of measure.
     *
     * @return the unit of measure (may be null)
     */
    public UnitOfMeasure getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    /**
     * Sets the product's unit of measure.
     *
     * @param unitOfMeasure the new unit of measure (can be null)
     */
    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    /**
     * Returns the product's owner.
     *
     * @return the owner
     */
    public Person getOwner() {
        return this.owner;
    }

    /**
     * Sets the product's owner.
     *
     * @param owner the new owner (must not be null)
     * @throws IllegalArgumentException if owner is null
     */
    public void setOwner(Person owner) {
        if (owner == null)
            throw new IllegalArgumentException("Invalid owner");
        this.owner = owner;
    }

    /**
     * Compares this Product with another for equality.
     *
     * @param p the other Product object
     * @return true if the id are equal, false otherwise
     */
    public boolean equals(Product p) {
        return p.getId() == this.id;
    }

    /**
     * Returns a string representation of the product.
     *
     * @return a string containing all product attributes
     */
    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", coordinates='" + getCoordinates() + "'" +
                ", creationDate='" + getCreationDate() + "'" +
                ", price='" + getPrice() + "'" +
                ", partNumber='" + getPartNumber() + "'" +
                ", manufactureCost='" + getManufactureCost() + "'" +
                ", unitOfMeasure='" + getUnitOfMeasure() + "'" +
                ", owner='" + getOwner() + "'" +
                "}";
    }
}