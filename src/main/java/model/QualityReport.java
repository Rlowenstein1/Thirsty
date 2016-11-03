package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDateTime;
import java.time.Month;
import javafx.beans.property.StringProperty;

/**
 * Represents a water quality report.
 */
public class QualityReport extends DisplayableReport implements Comparable<QualityReport> {
    /**
     * Primitve properties of the object
     */
    private LocalDateTime dateTime;
    private int reportNum;
    private User author;
    private WaterSafety safety;
    private double virusPPM;
    private double contaminantPPM;

    /**
     * JavaFX properties of the object - follows primitives
     */
    private final transient SimpleObjectProperty<LocalDateTime> dateTimeProperty = new SimpleObjectProperty<>();
    private final transient SimpleIntegerProperty reportNumProperty = new SimpleIntegerProperty();
    private final transient SimpleObjectProperty<User> authorProperty = new SimpleObjectProperty<>();
    private final transient SimpleObjectProperty<WaterSafety> safetyProperty = new SimpleObjectProperty<>();
    private final transient SimpleDoubleProperty virusPPMProperty = new SimpleDoubleProperty();
    private final transient SimpleDoubleProperty contaminantPPMProperty = new SimpleDoubleProperty();

    /**
     * Constructor for a new water source report.
     * The date and time are automatically generated.
     * @param reportNum the report number
     * @param author the author
     * @param safety overall safety of water
     * @param virusPPM the virus PPM
     * @param contaminantPPM the contaminant PPM
     * @param waterReport the availability report to add the quality report to
     */
    public QualityReport(int reportNum, User author, WaterSafety safety, double virusPPM, double contaminantPPM, WaterReport waterReport) {
        this(LocalDateTime.now(), reportNum, author, safety, virusPPM, contaminantPPM, waterReport);
    }

    /**
     * Constructor for a new water quality report with all fields
     * entered in manually.
     * @param dateTime the date and time of creation
     * @param reportNum the report number
     * @param author the author
     * @param safety overall safety of water
     * @param virusPPM the virus PPM
     * @param contaminantPPM the contaminant PPM
     * @param waterReport the availability report to add the quality report to
     */
    public QualityReport(LocalDateTime dateTime, int reportNum, User author, WaterSafety safety, double virusPPM, double contaminantPPM, WaterReport waterReport) {
        this.setDateTime(dateTime);
        this.setReportNum(reportNum);
        this.setAuthor(author);
        this.setWaterSafety(safety);
        this.setVirusPPM(virusPPM);
        this.setContaminantPPM(contaminantPPM);
    }

    /**
     * Gets this water report's number
     * @return the number
     */
    public int getReportNum() {
        return reportNum;
    }

    /**
     * Sets this water report's number
     * @param n the new number to be set
     */
    public void setReportNum(int n) {
        reportNum = n;
        reportNumProperty.set(n);
    }

    /**
     * Gets this water report's number property
     * @return the number property
     */
    @Override
    public SimpleIntegerProperty getReportNumProperty() {
        return reportNumProperty;
    }

    /**
     * Gets the second at which this report was created
     * @return the second
     */
    public int getSecond() {
        return dateTime.getSecond();
    }

    /**
     * Gets the minute at which this report was created
     * @return the minute
     */
    public int getMinute() {
        return dateTime.getMinute();
    }

    /**
     * Gets the hour at which this report was created
     * @return the hour
     */
    public int getHour() {
        return dateTime.getHour();
    }

    /** Gets the day at which this report was created
     * @return the day
     */
    public int getDay() {
        return dateTime.getDayOfMonth();
    }

    /**
     * Gets the month at which this report was created
     * @return the month
     */
    public Month getMonth() {
        return dateTime.getMonth();
    }

    /**
     * Gets the year at which this report was created
     * @return the year
     */
    public int getYear() {
        return dateTime.getYear();
    }

    /**
     * Get the report's creation time
     * @return the LocalDateTime of the report's creation
     */
    public LocalDateTime getDateTime() {
        return dateTime;
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
     * Sets this report's dateTime property
     * @param dt new date time
     */
    public void setDateTime(LocalDateTime dt) {
        dateTime = dt;
        dateTimeProperty.set(dt);
    }

    /**
     * Gets the water safety safety
     * @return the type
     */
    public WaterSafety getWaterSafety() {
        return safety;
    }

    /**
     * Sets the safety of water
     * @param t the new type of water
     */
    public void setWaterSafety(WaterSafety t) {
        safety = t;
        safetyProperty.set(t);
    }

    /**
     * Gets this water report's water safety
     * @return the water type property
     */
    @Override
    public ObjectProperty<WaterSafety> getWaterSafetyProperty() {
        return safetyProperty;
    }

    /**
     * Gets the water report's author
     * @return the author
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Sets the author
     * @param user the user of the report
     */
    public void setAuthor(User user) {
        author = user;
        authorProperty.set(user);
    }

    /**
     * Gets the water report's author property
     * @return the author property
     */
    public ObjectProperty<User> getAuthorProperty() {
        return authorProperty;
    }

    /**
     * Gets the water report's author property
     * @return the author property
     */
    @Override
    public StringProperty getAuthorUsernameProperty() {
        return author.getUsernameProperty();
    }

    /**
     * Gets the virus PPM for this water report
     * @return the virus PPM
     */
    public double getVirusPPM() {
        return virusPPM;
    }

    /**
     * Sets the virus PPM for this water source
     * @param virus the new virus PPM
     */
    public void setVirusPPM(double virus) {
        virusPPM = virus;
        virusPPMProperty.set(virus);
    }

    /**
     * Gets the virus PPM property for this water report
     * @return the virus PPM property
     */
    @Override
    public SimpleDoubleProperty getVppmProperty() {
        return virusPPMProperty;
    }

    /**
     * Gets the contaminant PPM for this water report
     * @return the contaminant PPM
     */
    public double getContaminantPPM() {
        return contaminantPPM;
    }

    /**
     * Sets the contaminant PPM for this water report
     * @param contaminant the new contaminant PPM
     */
    public void setContaminantPPM(double contaminant) {
        contaminantPPM = contaminant;
        contaminantPPMProperty.set(contaminant);
    }

    /**
     * Gets the contaminant PPM property for this water report
     * @return the contaminant PPM property
     */
    @Override
    public SimpleDoubleProperty getCppmProperty() {
        return contaminantPPMProperty;
    }

    /**
     * String representation for this water quality report
     * @return a string representation of this water quality report
     */
    @Override
    public String toString() {
        return "Quality report number: " + reportNum + "\n"
                + "Date and time: " + dateTime + "\n"
                + "Safety of water: " + safety + "\n"
                + "Virus PPM: " + virusPPM + "\n"
                + "Contaminant PPM " + contaminantPPM;
    }

    /**
     * Compare method for comparing water quality reports based
     * on the time created
     * @param report the water quality report to be compared
     * @return the integer value from comparison
     */
    @Override
    public int compareTo(QualityReport report) {
        return this.dateTime.compareTo(report.getDateTime());
    }
}
