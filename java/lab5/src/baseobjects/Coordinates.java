package src.baseobjects;

/**
 * The {@code Coordinates} class represents a pair of coordinates with an x
 * (Long) and y (double) value.
 * The x value must not be null and must be <= 321.
 * 
 * @author Petukhov Kirill
 */
public class Coordinates {
    /** The x-coordinate (must be non-null and <= 321). */
    private Long x;
    /** The y-coordinate. */
    private double y;

    /**
     * Constructs a new Coordinates object.
     *
     * @param x the x-coordinate (must not be null and <= 321)
     * @param y the y-coordinate
     * @throws IllegalArgumentException if x is null or greater than 321
     */
    public Coordinates(Long x, double y) {
        setXInternal(x);
        this.y = y;
    }

    /**
     * Returns the x-coordinate.
     *
     * @return the x-coordinate
     */
    public Long getX() {
        return this.x;
    }

    /**
     * Sets the x-coordinate.
     *
     * @param x the new x-coordinate (must not be null and <= 321)
     * @throws IllegalArgumentException if x is null or greater than 321
     */
    public void setX(Long x) {
        setXInternal(x);
    }

    /**
     * Returns the y-coordinate.
     *
     * @return the y-coordinate
     */
    public double getY() {
        return this.y;
    }

    /**
     * Sets the y-coordinate.
     *
     * @param y the new y-coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Internal setter for x that performs validation.
     *
     * @param x the x-coordinate to set
     * @throws IllegalArgumentException if x is null or greater than 321
     */
    private void setXInternal(Long x) {
        if (x == null || x > 321)
            throw new IllegalArgumentException("Invalid coordinates");
        this.x = x;
    }

    /**
     * Compares this Coordinates with another for equality.
     *
     * @param c the other Coordinates object
     * @return true if the coordinates are equal, false otherwise
     */
    public boolean equals(Coordinates c) {
        return c.getX() == this.x && c.getY() == this.y;
    }

    /**
     * Returns a string representation of the Coordinates.
     *
     * @return a string in the format "{ x='<x>', y='<y>' }"
     */
    @Override
    public String toString() {
        return "{" +
                " x='" + getX() + "'" +
                ", y='" + getY() + "'" +
                "}";
    }
}
