package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.time.LocalDateTime;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author tybrown
 */
public abstract class DisplayableReport extends RecursiveTreeObject<DisplayableReport> {
    
    /**
     * Overrideable method to get the latitude property
     * @return  the latitude property
     */
    public DoubleProperty getLatitudeProperty() {
        return (new SimpleDoubleProperty(Double.NaN));
    }

    /**
     * Overrideable method to get the longitude property
     * @return the longitude property
     */
    public DoubleProperty getLongitudeProperty() {
        return (new SimpleDoubleProperty(Double.NaN));
    }

    /**
     * Overrideable method to get the report number property
     * @return the report number property
     */
    public abstract int getParentReportNum();
 
    /**
     * Overrideable method to get the report number property
     * @return the report number property
     */
    public IntegerProperty getReportNumProperty() {
        return (new SimpleIntegerProperty());
    }
    
    /**
     * Overrideable method to get the date time property
     * @return the date time property
     */
    public ObjectProperty<LocalDateTime> getDateTimeProperty() {
        return (new SimpleObjectProperty<>());
    }

    /**
     * Overrideable method to get the author's username property
     * @return the author's username property
     */
    public StringProperty getAuthorUsernameProperty() {
        return (new SimpleStringProperty());
    }

    /**
     * Overrideable method to get the water type property
     * @return the water type property
     */
    public ObjectProperty<WaterType> getWaterTypeProperty() {
        return (new SimpleObjectProperty<>());
    }

    /**
     * Overrideable method to get the water condition property
     * @return the water condition property
     */
    public ObjectProperty<WaterCondition> getWaterConditionProperty() {
        return (new SimpleObjectProperty<>());
    }

    /**
     * Overrideable method to get the water safety property
     * @return the water safety property
     */
    public ObjectProperty<WaterSafety> getWaterSafetyProperty() {
        return (new SimpleObjectProperty<>());
    }

    /**
     * Overrideable method to get the virus ppm property
     * @return the virus ppm property
     */
    public DoubleProperty getVppmProperty() {
        return (new SimpleDoubleProperty(Double.NaN));
    }

    /**
     * Overrideable method to get the contaminant ppm property
     * @return the contaminant ppm property
     */
    public DoubleProperty getCppmProperty() {
        return (new SimpleDoubleProperty(Double.NaN));
    }
}
