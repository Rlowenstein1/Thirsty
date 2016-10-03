package model;

/**
 * Enum that represents the six different water types
 */
public enum WaterType {
    BOTTLED("Bottled"), WELL("Well"), STREAM("Stream"), LAKE("Lake"), SPRING("Spring"), OTHER("Other");

    private final String type;

    /**
     * Default constructor
     * @param type the type of water
     */
    WaterType(String type) {
        this.type = type;
    }

    /**
     * Gets the String value of the type of water
     * @return the type
     */
    public String getValue() {
        return type;
    }
}
