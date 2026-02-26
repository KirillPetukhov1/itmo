package src.baseobjects;

/**
 * Represents a person with name, height, hair color, and nationality.
 * Provides methods to get and set these attributes with validation.
 *
 * @author Petukhov Kirill
 */
public class Person {
    /** The name of the person (must not be null or empty). */
    private String name;
    /** The height of the person in centimeters (must be positive). */
    private long height;
    /** The hair color of the person (must not be null). */
    private Color hairColor;
    /** The nationality of the person (can be null). */
    private Country nationality;

    /**
     * Returns the person's name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the person's name.
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
     * Returns the person's height.
     *
     * @return the height
     */
    public long getHeight() {
        return this.height;
    }

    /**
     * Sets the person's height.
     *
     * @param height the new height (must be > 0)
     * @throws IllegalArgumentException if height <= 0
     */
    public void setHeight(long height) {
        if (height <= 0)
            throw new IllegalArgumentException("Invalid height, height must be > 0");
        this.height = height;
    }

    /**
     * Returns the person's hair color.
     *
     * @return the hair color
     */
    public Color getHairColor() {
        return this.hairColor;
    }

    /**
     * Sets the person's hair color.
     *
     * @param hairColor the new hair color (must not be null)
     * @throws IllegalArgumentException if hairColor is null
     */
    public void setHairColor(Color hairColor) {
        if (hairColor == null)
            throw new IllegalArgumentException("Invalid hair color");
        this.hairColor = hairColor;
    }

    /**
     * Returns the person's nationality.
     *
     * @return the nationality
     */
    public Country getNationality() {
        return this.nationality;
    }

    /**
     * Sets the person's nationality.
     *
     * @param nationality the new nationality (can be null)
     */
    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    /**
     * Returns a string representation of the person.
     *
     * @return a string containing name, height, hairColor, and nationality
     */
    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", height='" + getHeight() + "'" +
            ", hairColor='" + getHairColor() + "'" +
            ", nationality='" + getNationality() + "'" +
            "}";
    }
}