package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Arrays;

/**
 * Enum that represents the four different water conditions.
 */
public enum WaterSafety {
    SAFE("Safe"), TREATABLE("Treatable"), UNSAFE("Unsafe"), UNKNOWN("Unknown");

    private final String condition;

    /**
     * Default constructor
     * @param condition the condition of the water
     */
    WaterSafety(String condition) {
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
     * @return ObservableList of WaterCondition
     */
    public static ObservableList<WaterSafety> getAllObservableList() {
        return (FXCollections.observableList(Arrays.asList(values())));
    }
}
