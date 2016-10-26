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
import javafx.beans.value.ObservableValue;

/**
 *
 * @author tybrown
 */
public class DisplayableReport extends RecursiveTreeObject<DisplayableReport> {
    
    public DoubleProperty getLatitudeProperty() {
        return (new SimpleDoubleProperty(Double.NaN));
    }

    public DoubleProperty getLongitudeProperty() {
        return (new SimpleDoubleProperty(Double.NaN));
    }

    public IntegerProperty getReportNumProperty() {
        return (new SimpleIntegerProperty());
    }
    
    public ObjectProperty<LocalDateTime> getDateTimeProperty() {
        return (new SimpleObjectProperty<LocalDateTime>());
    }

    public StringProperty getAuthorUsernameProperty() {
        return (new SimpleStringProperty());
    }

    public ObjectProperty<WaterType> getWaterTypeProperty() {
        return (new SimpleObjectProperty<WaterType>());
    }

    public ObjectProperty<WaterCondition> getWaterConditionProperty() {
        return (new SimpleObjectProperty<WaterCondition>());
    }

    public ObjectProperty<WaterSafety> getWaterSafetyProperty() {
        return (new SimpleObjectProperty<WaterSafety>());
    }

    public DoubleProperty getVppmProperty() {
        return (new SimpleDoubleProperty(Double.NaN));
    }

    public DoubleProperty getCppmProperty() {
        return (new SimpleDoubleProperty(Double.NaN));
    }
}
