package main.java.model;

import com.google.gson.annotations.Expose;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a water source report.
 */
public final class WaterReport extends DisplayableReport implements Comparable<WaterReport> {

    @Expose
    private final IntegerProperty reportNumProperty = new SimpleIntegerProperty();
    @Expose
    private final DoubleProperty latitudeProperty = new SimpleDoubleProperty();
    @Expose
    private final DoubleProperty longitudeProperty = new SimpleDoubleProperty();
    @Expose
    private final ObjectProperty<LocalDateTime> dateTimeProperty = new SimpleObjectProperty<>();
    @Expose
    private final ObjectProperty<WaterType> typeProperty = new SimpleObjectProperty<>();
    @Expose
    private final ObjectProperty<WaterCondition> conditionProperty = new SimpleObjectProperty<>();
    @Expose
    private final ObjectProperty<User> authorProperty = new SimpleObjectProperty<>();
    @Expose
    private final List<QualityReport> qualityReports = new ArrayList<>();

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
        this.setDateTime(dateTime);
        this.setReportNum(reportNum);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setWaterType(type);
        this.setWaterCondition(condition);
        this.setAuthor(author);
        //qualityReports = new ArrayList<>();
    }

    /**
     * Gets this water report's number
     * @return the number
     */
    public int getReportNum() {
        return reportNumProperty.get();
    }

    /**
     * Sets this water report's number
     * @param n the new number to be set
     */
    private void setReportNum(int n) {
        reportNumProperty.set(n);
    }


    /**
     * Gets this water report's number property
     * @return the number property
     */
    @Override
    public IntegerProperty getReportNumProperty() {
        return reportNumProperty;
  }


    /**
     * Gets this water report's longitude coordinate
     * @return the longitude coordinate
     */
    public double getLongitude() {
        return longitudeProperty.get();
    }

    /**
     * Sets this water report's longitude
     * @param l the new longitude to be set
     */
    private void setLongitude(double l) {
        longitudeProperty.set(l);
    }

    /**
     * Gets this water report's longitude property
     * @return the longitude coordinate property
     */
    @Override
    public DoubleProperty getLongitudeProperty() {
        return longitudeProperty;
    }


    /**
     * Gets this water report's latitude coordinate
     * @return the latitude coordinate
     */
    public double getLatitude() {
        return latitudeProperty.get();
    }

    /**
     * Sets this water report's latitude
     * @param l the new latitude to be set
     */
    private void setLatitude(double l) {
        latitudeProperty.set(l);
    }

    /**
     * Gets this water report's latitude property
     * @return the latitude coordinate property
     */
    @Override
    public DoubleProperty getLatitudeProperty() {
        return latitudeProperty;
    }

    /**
     * Gets the second at which this report was created
     * @return the second
     */
    public int getSecond() {
        return getDateTime().getSecond();
    }

    /**
     * Gets the minute at which this report was created
     * @return the minute
     */
    public int getMinute() {
        return getDateTime().getMinute();
    }

    /**
     * Gets the hour at which this report was created
     * @return the hour
     */
    /*
    public int getHour() {
        return getDateTime().getHour();
    }*/

    /** Gets the day at which this report was created
     * @return the day
     */
    /*public int getDay() {
        return getDateTime().getDayOfMonth();
    }*/

    /**
     * Gets the month at which this report was created
     * @return the month
     */
//    public Month getMonth() {
//        return getDateTime().getMonth();
//    }

    /**
     * Gets the year at which this report was created
     * @return the year
     */
    public int getYear() {
        return getDateTime().getYear();
    }

    /**
     * Get the report's creation time
     * @return the LocalDateTime of the report's creation
     */
    public LocalDateTime getDateTime() {
        return dateTimeProperty.get();
    }


    /**
     * Gets this water report's dateTime property
     * @return the dateTime property
     */
    @Override
    public ObjectProperty<LocalDateTime> getDateTimeProperty() {
        return dateTimeProperty;
    }


    /**
     * Sets the report's creation time
     * @param dt report's new date time
     */
    private void setDateTime(LocalDateTime dt) {
        dateTimeProperty.set(dt);
    }

    /**
     * Gets the type of the water
     * @return the type
     */
    public WaterType getWaterType() {
        return typeProperty.get();
    }

    /**
     * Sets the type of water
     * @param t the new type of water
     */
    private void setWaterType(WaterType t) {
        typeProperty.set(t);
    }


    /**
     * Gets this water report's water source type
     * @return the water type property
     */
    @Override
    public ObjectProperty<WaterType> getWaterTypeProperty() {
        return typeProperty;
    }


    /**
     * Gets the water condition
     * @return the condition
     */
    public WaterCondition getWaterCondition() {
        return conditionProperty.get();
    }

    /**
     * Sets the water condition
     * @param c the condition
     */
    private void setWaterCondition(WaterCondition c) {
        conditionProperty.set(c);
    }

    /**
     * Gets the water report's water condition property
     * @return the water condition property
     */
    @Override
    public ObjectProperty<WaterCondition> getWaterConditionProperty() {
        return conditionProperty;
    }


    /**
     * Gets the water report's author
     * @return the author
     */
    public User getAuthor() {
        return authorProperty.get();
    }

    /**
     * Sets the author
     * @param user the user of the report
     */
    private void setAuthor(User user) {
        authorProperty.set(user);
    }


    /**
     * Gets the water report's author property
     * @return the author property
     */

    @Override
    public StringProperty getAuthorUsernameProperty() {
        return getAuthor().getUsernameProperty();
    }


    /**
     * Gets the water report's author property
     * @return the author property
     */
    public ObjectProperty<User> getAuthorProperty() {
        return authorProperty;
    }

    /**
     * Adds a quality report to the list of quality reports for this
     * water source.
     * @param qualityReport the quality report to be added to the list
     * @return True if this element did not already exist
     */
    public boolean addQualityReport(QualityReport qualityReport) {
        return qualityReports.add(qualityReport);
    }


    /**
     * Removes a quality report from the list of quality reports for this
     * water source.
     * @param qualityReport the quality report to be removed from the list
     * @return True if an element was removed
     */
    public boolean removeQualityReport(QualityReport qualityReport) {
        return qualityReports.remove(qualityReport);
    }


    /**
     * Removes a quality report from the list of quality reports for this
     * water source.
     * @param reportNum the number of the report to remove
     * @return True if an element was removed
     */
    public boolean removeQualityReport(int reportNum) {
        QualityReport q = getQualityReportByNumber(reportNum);
        return (q != null) && (qualityReports.remove(q));
    }

    /**
     * Returns the QualityReport with the report number matching reportNum
     * @param reportNum the number of the report to find
     * @return returns the quality report if found, otherwise null
     */
    private QualityReport getQualityReportByNumber(int reportNum) {
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
     * Gets the most recent (by report number) quality report in this water report
     * @return The most recent QualityReport
     */
    public QualityReport getMostRecentQualityReport() {
        if (qualityReports.size() < 1) {
            return (null);
        }
        return (qualityReports.get(qualityReports.size() - 1));
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
     * Creates a (deep -- quality reports are cloned too) cloneIt of this water report
     * @return Returns a WaterReport with all the same fields as this WaterReport
     */
    public WaterReport cloneIt() {
        WaterReport res = new WaterReport(getReportNum(), getDateTime(), getLatitude(),
                getLongitude(), getWaterType(), getWaterCondition(), getAuthor());
        for (QualityReport q : getQualityReportList()) {
            QualityReport newQ = q.cloneIt();
            newQ.setParentReport(res);
            res.addQualityReport(newQ);
        }
        return (res);
    }

    /**
     * String representation for this water report
     * @return a string representation of this water report object
     */
    @Override
    public String toString() {
        return (String.format(
              "Water report number: %s\n"
            + "Creation date/time: %s\n"
            + "Location: (%.5f, %.5f)\n"
            + "Type: %s\n"
            + "Condition: %s\n",
            getReportNum(),
            getDateTime(),
            getLatitude(),
            getLongitude(),
            getWaterType(),
            getWaterCondition()
        ));
    }

    /**
     * Compare method for comparing water source reports based
     * on the time created
     * @param report the water report to be compared
     * @return the integer value from comparison
     */
    @Override
    public int compareTo(WaterReport report) {
        return getDateTime().compareTo(report.getDateTime());
    }
}
