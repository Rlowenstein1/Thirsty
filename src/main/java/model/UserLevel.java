package main.java.model;

import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    @Override
    public String toString() {
        switch (level) {
        case 1:
            return ("user");
        case 2:
            return ("worker");
        case 3:
            return ("manager");
        case 4:
            return ("administrator");
        default:
            return ("hacker");
        }
    }

    /**
     * Getter for all observable lists
     * @return ObservableList of UserLevel
     */
    public static ObservableList<UserLevel> getAllObservableList() {
        return (FXCollections.observableList(Arrays.asList(values())));
    }
}