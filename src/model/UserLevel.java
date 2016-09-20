package model;

/**
 * An enum that represents the four different authority levels of Users
 */
public enum UserLevel {
    USER(1), WORKER(2), MANAGER(3), ADMINISTRATOR(4);

    private final int level;

    /**
     * The default constructor for the UserLevel enum
     *
     * @param level the integer value for the authority level
     */
    UserLevel(int level) {
        this.level = level;
    }

    /**
     * Gets the integer value of a UserLevel
     *
     * @return the integer (1-4) representing a User's authority level
     */
    public int getValue() {
        return level;
    }

    /**
     * Gets UserLevel enum from an integer value(1-4)
     *
     * @param i the integer value of the UserLevel
     * @return a UserLevel object
     */
    public static UserLevel fromInt(int i) {
        for (UserLevel l: UserLevel.values()) {
            if (l.getValue() == i) {
                return l;
            }
        }
        return null;
    }
}