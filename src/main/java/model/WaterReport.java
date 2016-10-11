package model;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.awt.geom.Point2D;
import java.time.LocalDateTime;
import java.time.Month;

/**
 * Represents a water source report.
 */
public class WaterReport {
    private final SimpleIntegerProperty reportNum = new SimpleIntegerProperty();
    private final SimpleObjectProperty<LocalDateTime> dateTime = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Point2D.Double> location = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<WaterType> type = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<WaterCondition> condition = new SimpleObjectProperty<>();
    private static int reportNumCounter = 1;

    /**
     * Constructor for a new water source report.
     * The report number, date, and time are automatically generated.
     * @param location the location (long, lat) of the water source
     * @param type the type of water
     * @param condition the condition of water
     */
    public WaterReport(Point2D.Double location, WaterType type, WaterCondition condition) {
        this.reportNum.set(reportNumCounter);
        this.dateTime.set(LocalDateTime.now());
        this.location.set(location);
        this.type.set(type);
        this.condition.set(condition);
        reportNumCounter++;
    }

    /**
     * Constructor for a new water source with all fields
     * entered in manually.
     * @param reportNum the report number
     * @param dateTime the date and time of creation
     * @param location the location of the water source
     * @param type the type of water
     * @param condition the condition of water
     */
    public WaterReport(int reportNum, LocalDateTime dateTime, Point2D.Double location,
                       WaterType type, WaterCondition condition) {
        this(location, type, condition);
        this.reportNum.set(reportNum);
        this.dateTime.set(dateTime);
        reportNumCounter++;
    }

    /**
     * Returns total number of water reports
     * @return the number
     */
    public static int getCount() {
        return reportNumCounter;
    }

    /**
     * Gets this water report's number
     * @return the number
     */
    public int getReportNum() {
        return reportNum.get();
    }

    /**
     * Sets this water report's number
     * @param n the new number to be set
     */
    public void setReportNum(int n) {
        reportNum.set(n);
    }

    /**
     * Gets this water report's number property
     * @return the number property
     */
    public SimpleIntegerProperty getReportNumProperty() {
        return reportNum;
    }

    /**
     * Gets the point containing the longitude and latitude
     * of the water source.
     * @return
     */
    public Point2D.Double getLocation() {
        return location.get();
    }


    /**
     * Sets the point to the location of the new water source
     * @param p the point containing the location of new water source
     */
    public void setLocation(Point2D.Double p) {
        location.set(p);
    }

    /**
     * Gets this water report's location property
     * @return the location property
     */
    public ObjectProperty<Point2D.Double> getLocationProperty() {
        return location;
    }

    /**
     * Gets the second at which this report was created
     * @return the second
     */
    public int getSecond() {
        return dateTime.get().getSecond();
    }

    /**
     * Gets the minute at which this report was created
     * @return the minute
     */
    public int getMinute() {
        return dateTime.get().getMinute();
    }

    /**
     * Gets the hour at which this report was created
     * @return the hour
     */
    public int getHour() {
        return dateTime.get().getHour();
    }

    /** Gets the day at which this report was created
     * @return the day
     */
    public int getDay() {
        return dateTime.get().getDayOfMonth();
    }

    /**
     * Gets the month at which this report was created
     * @return the month
     */
    public Month getMonth() {
        return dateTime.get().getMonth();
    }

    /**
     * Gets the year at which this report was created
     * @return the year
     */
    public int getYear() {
        return dateTime.get().getYear();
    }

    /**
     * Gets this water report's dateTime property
     * @return the dateTime property
     */
    public ObjectProperty<LocalDateTime> getDateTimeProperty() {
        return dateTime;
    }

    /**
     * Gets the type of the water
     * @return the type
     */
    public WaterType getWaterType() {
        return type.get();
    }

    /**
     * Sets the type of water
     * @param t the new type of water
     */
    public void setWaterType(WaterType t) {
        type.set(t);
    }

    /**
     * Gets this water report's water source type
     * @return the water type property
     */
    public ObjectProperty<WaterType> getWaterTypeProperty() {
        return type;
    }

    /**
     * Gets the water condition
     * @return the condition
     */
    public WaterCondition getWaterCondition() {
        return condition.get();
    }

    /**
     * Sets the water condition
     * @param c the condition
     */
    public void setWaterCondtion(WaterCondition c) {
        condition.set(c);
    }

    /**
     * Gets thus water report's water condition property
     * @return the water condition property
     */
    public ObjectProperty<WaterCondition> getWaterConditionProperty() {
        return condition;
    }

    @Override
    public String toString() {
        return "Report number: " + reportNum.get() + "\n"
                + "Date and time: " + dateTime.get() + "\n"
                + "Location of water source: " + location.get() + "\n"
                + "Type of water: " + type.get() + "\n"
                + "Condition of water: " + condition.get();
    }
}
