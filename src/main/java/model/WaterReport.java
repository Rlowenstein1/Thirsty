package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import lib.Debug;

/**
 * Represents a water source report.
 */
public class WaterReport extends DisplayableReport implements Comparable<WaterReport> {
    private final SimpleIntegerProperty reportNum = new SimpleIntegerProperty();
    private final SimpleDoubleProperty latitude = new SimpleDoubleProperty();
    private final SimpleDoubleProperty longitude = new SimpleDoubleProperty();
    private final SimpleObjectProperty<LocalDateTime> dateTime = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<WaterType> type = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<WaterCondition> condition = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<User> author = new SimpleObjectProperty<>();
    private final SimpleListProperty<QualityReport> qualityReports = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * Constructor for a new water source report.
     * The date and time are automatically generated.
     * @param reportNum the number of the report
     * @param latitude Latitude coordinate
     * @param longitude Longitude coordinate
     * @param type the type of water
     * @param condition the condition of water
     * @param author the author of the report
     */
    public WaterReport(int reportNum, double latitude, double longitude, WaterType type,
                WaterCondition condition, User author) {
        this(reportNum, LocalDateTime.now(), latitude, longitude, type, condition, author);
    }

    /**
     * Constructor for a new water source with all fields
     * entered in manually.
     * @param reportNum the report number
     * @param dateTime the date and time of creation
     * @param latitude Latitude coordinate
     * @param longitude Longitude coordinate
     * @param type the type of water
     * @param condition the condition of water
     * @param author the author of the report
     */
    public WaterReport(int reportNum, LocalDateTime dateTime, double latitude, double longitude,
                       WaterType type, WaterCondition condition, User author) {
        this.dateTime.set(dateTime);
        this.reportNum.set(reportNum);
        this.latitude.set(latitude);
        this.longitude.set(longitude);
        this.type.set(type);
        this.condition.set(condition);
        this.author.set(author);
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
    @Override
    public SimpleIntegerProperty getReportNumProperty() {
        return reportNum;
    }

    /**
     * Gets this water report's longitude coordinate
     * @return the longitude coordinate
     */
    public double getLongitude() {
        return longitude.get();
    }

    /**
     * Sets this water report's longitude
     * @param l the new longitude to be set
     */
    public void setLongitude(double l) {
        longitude.set(l);
    }

    /**
     * Gets this water report's longitude property
     * @return the longitude coordinate property
     */
    @Override
    public SimpleDoubleProperty getLongitudeProperty() {
        return longitude;
    }

    /**
     * Gets this water report's latitude coordinate
     * @return the latitude coordinate
     */
    public double getLatitude() {
        return latitude.get();
    }

    /**
     * Sets this water report's latitude
     * @param l the new latitude to be set
     */
    public void setLatitude(double l) {
        latitude.set(l);
    }

    /**
     * Gets this water report's latitude property
     * @return the latitude coordinate property
     */
    @Override
    public DoubleProperty getLatitudeProperty() {
        return latitude;
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
     * Get the report's creation time
     * @return the LocalDateTime of the report's creation
     */
    public LocalDateTime getDateTime() {
        return (dateTime.get());
    }

    /**
     * Gets this water report's dateTime property
     * @return the dateTime property
     */
    @Override
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
    @Override
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
    public void setWaterCondition(WaterCondition c) {
        condition.set(c);
    }

    /**
     * Gets the water report's water condition property
     * @return the water condition property
     */
    @Override
    public ObjectProperty<WaterCondition> getWaterConditionProperty() {
        return condition;
    }
    
    /**
     * Gets the water report's author
     * @return the author
     */
    public User getAuthor() {
        return author.get();
    }

    /**
     * Sets the author
     * @param user the user of the report
     */
    public void setAuthor(User user) {
        author.set(user);
    }

    /**
     * Gets the water report's author property
     * @return the author property
     */
    @Override
    public StringProperty getAuthorUsernameProperty() {
        return author.get().getUsernameProperty();
    }

    /**
     * Gets the water report's author property
     * @return the author property
     */
    public ObjectProperty<User> getAuthorProperty() {
        return author;
    }

    /**
     * Adds a quality report to the list of quality reports for this
     * water source.
     * @param qualityReport the quality report to be added to the list
     */
    public void addQualityReport(QualityReport qualityReport) {
        try {
            qualityReports.add(qualityReport);
        } catch (Exception e) { //TODO: pretty sure that's going to throw up some red flags on code review day
            Debug.error("Error adding to list. Reason: %s", e.toString());
        }
    }

    /**
     * Removes a quality report from the list of quality reports for this
     * water source.
     * @param qualityReport the quality report to be removed from the list
     */
    public void removeQualityReport(QualityReport qualityReport) {
        try {
            qualityReports.remove(qualityReport);
        } catch (Exception e) { //TODO: pretty sure that's going to throw up some red flags on code review day
            Debug.error("Error adding to list. Reason: %s", e.toString());
        }
    }

    /**
     * Removes a quality report from the list of quality reports for this
     * water source.
     * @param reportNum the number of the report to remove
     */
    public void removeQualityReport(int reportNum) {
        try {
            QualityReport q = getQualityReportByNumber(reportNum);
            if (q != null) {
                qualityReports.remove(q);
            }
        } catch (Exception e) { //TODO: pretty sure that's going to throw up some red flags on code review day
            Debug.error("Error adding to list. Reason: %s", e.toString());
        }
    }

    /**
     * Returns the QualityReport with the report number matching reportNum
     * @param reportNum the number of the report to find
     * @return returns the quality report if found, otherwise null
     */
    public QualityReport getQualityReportByNumber(int reportNum) {
        for (QualityReport q : qualityReports) {
            if (q.getReportNum() == reportNum) {
                return (q);
            }
        }
        return (null);
    }

    /**
     * Gets the list of quality reports for this water source report.
     * @return the list of quality reports
     */
    public List<QualityReport> getQualityReportList() {
        return qualityReports;
    }


    /**
     * Gets the property list of quality reports for this water source report.
     * @return the property list of quality reports
     */
    public SimpleListProperty<QualityReport> getQualityReportPropertyList() {
        return qualityReports;
    }

    public QualityReport getMostRecentQualityReport() {
        if (qualityReports.getSize() < 1) {
            return (null);
        }
        return (qualityReports.get(qualityReports.getSize() - 1));
    }

    /**
     * Gets this water report's water safety
     * @return the water type property
     */
    @Override
    public ObjectProperty<WaterSafety> getWaterSafetyProperty() {
        QualityReport latestQR = getMostRecentQualityReport();
        if (latestQR == null) {
            return (super.getWaterSafetyProperty());
        }
        return (latestQR.getWaterSafetyProperty());
    }

    /**
     * Gets the virus PPM property for the most recent quality report
     * @return the virus PPM property
     */
    @Override
    public DoubleProperty getVppmProperty() {
        QualityReport latestQR = getMostRecentQualityReport();
        if (latestQR == null) {
            return (super.getVppmProperty());
        }
        return (latestQR.getVppmProperty());
    }

    /**
     * Gets the contaminant PPM property for the most recent quality report
     * @return the contaminant PPM property
     */
    @Override
    public DoubleProperty getCppmProperty() {
        QualityReport latestQR = getMostRecentQualityReport();
        if (latestQR == null) {
            return (super.getCppmProperty());
        }
        return (latestQR.getCppmProperty());
    }

    /**
     * String representation for this water report
     * @return a string representation of this water report object
     */
    @Override
    public String toString() {
        return "Water report number: " + reportNum.get() + "\n"
                + "Date and time: " + dateTime.get() + "\n"
                + "Location of water source: " + String.format("(%.5f,%.5f)", getLatitude(), getLongitude()) + "\n"
                + "Type of water: " + type.get() + "\n"
                + "Condition of water: " + condition.get();
    }

    /**
     * Compare method for comparing water source reports based
     * on the time created
     * @param report the water report to be compared
     * @return the integer value from comparison
     */
    @Override
    public int compareTo(WaterReport report) {
        return this.dateTime.get().compareTo(report.getDateTimeProperty().get());
    }
}
