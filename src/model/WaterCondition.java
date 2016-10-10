package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

/**
 * Enum that represents the four different water conditions.
 */
public enum WaterCondition {
    WASTE("Waste"), CLEAR("Clear"), MUDDY("Muddy"), POTABLE("Potable");

    private final String condition;

    /**
     * Default constructor
     * @param condition the condition of the water
     */
    WaterCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Gets the String value of the water condition
     * @return the condition
     */
    public String getValue() {
        return condition;
    }

    /**
     * Getter for all observable lists
     * @return ObservableList of UserLevel
     */
    public static ObservableList<WaterCondition> getAllObservableList() {
        return (FXCollections.observableList(Arrays.asList(values())));
    }
}
