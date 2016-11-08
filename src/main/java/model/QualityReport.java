package model;

import com.google.gson.annotations.Expose;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDateTime;
import java.time.Month;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a water quality report.
 */
public class QualityReport extends DisplayableReport implements Comparable<QualityReport> {
    @Expose
    private final ObjectProperty<LocalDateTime> dateTimeProperty = new SimpleObjectProperty<>();
    @Expose
    private final IntegerProperty reportNumProperty = new SimpleIntegerProperty();
    @Expose
    private final ObjectProperty<User> authorProperty = new SimpleObjectProperty<>();
    @Expose
    private final ObjectProperty<WaterSafety> safetyProperty = new SimpleObjectProperty<>();
    @Expose
    private final DoubleProperty virusPPMProperty = new SimpleDoubleProperty();
    @Expose
    private final DoubleProperty contaminantPPMProperty = new SimpleDoubleProperty();
    @Expose
    private final ObjectProperty<WaterReport> parentReportProperty = new SimpleObjectProperty<>();
    private transient WaterReport parentReport;

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
        this.setParentReport(waterReport);
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
    public void setReportNum(int n) {
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
     * Gets this water report's longitude property
     * @return the latitude coordinate property
     */
    @Override
    public DoubleProperty getLongitudeProperty() {
        return getParentReport().getLongitudeProperty();
    }

    /**
     * Gets this water report's latitude property
     * @return the latitude coordinate property
     */
    @Override
    public DoubleProperty getLatitudeProperty() {
        return getParentReport().getLatitudeProperty();
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
    public int getHour() {
        return getDateTime().getHour();
    }

    /** Gets the day at which this report was created
     * @return the day
     */
    public int getDay() {
        return getDateTime().getDayOfMonth();
    }

    /**
     * Gets the month at which this report was created
     * @return the month
     */
    public Month getMonth() {
        return getDateTime().getMonth();
    }

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
     * Sets this report's dateTime property
     * @param dt new date time
     */
    public void setDateTime(LocalDateTime dt) {
        dateTimeProperty.set(dt);
    }

    /**
     * Gets the water safety safety
     * @return the type
     */
    public WaterSafety getWaterSafety() {
        return safetyProperty.get();
    }

    /**
     * Sets the safety of water
     * @param t the new type of water
     */
    public void setWaterSafety(WaterSafety t) {
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
        return authorProperty.get();
    }

    /**
     * Sets the author
     * @param user the user of the report
     */
    public void setAuthor(User user) {
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
        return getAuthor().getUsernameProperty();
    }

    /**
     * Gets the virus PPM for this water report
     * @return the virus PPM
     */
    public double getVirusPPM() {
        return virusPPMProperty.get();
    }

    /**
     * Sets the virus PPM for this water source
     * @param virus the new virus PPM
     */
    public void setVirusPPM(double virus) {
        virusPPMProperty.set(virus);
    }

    /**
     * Gets the virus PPM property for this water report
     * @return the virus PPM property
     */
    @Override
    public DoubleProperty getVppmProperty() {
        return virusPPMProperty;
    }

    /**
     * Gets the contaminant PPM for this water report
     * @return the contaminant PPM
     */
    public double getContaminantPPM() {
        return contaminantPPMProperty.get();
    }

    /**
     * Sets the contaminant PPM for this water report
     * @param contaminant the new contaminant PPM
     */
    public void setContaminantPPM(double contaminant) {
        contaminantPPMProperty.set(contaminant);
    }

    /**
     * Gets the contaminant PPM property for this water report
     * @return the contaminant PPM property
     */
    @Override
    public DoubleProperty getCppmProperty() {
        return contaminantPPMProperty;
    }

    /**
     * Gets the water source report attached to this quality report
     * @return the water source report
     */
    public WaterReport getParentReport() {
        return parentReport;
    }

    /**
     * Sets the water source report to a new one
     * @param wr the new water source report attached to this quality report
     */
    public void setParentReport(WaterReport wr) {
        parentReport = wr;
    }

    /**
     * Gets the water source report property
     * @return the water source report property
     */
    public ObjectProperty<WaterReport> getParentReportProperty() {
        return parentReportProperty;
    }

    public QualityReport clone() {
        return (new QualityReport(getDateTime(), getReportNum(), getAuthor(), getWaterSafety(), getVirusPPM(), getContaminantPPM(), getParentReport()));
    }

    /**
     * String representation for this water quality report
     * @return a string representation of this water quality report
     */
    @Override
    public String toString() {
        return (String.format(
              "Parent report #%s\n"
            + "Quality report number: %s\n"
            + "Creation date/time: %s\n"
            + "Safety: %s\n"
            + "Contaminant PPM: %s\n"
            + "Virus PPM: %s",
            parentReport == null ? "<no parent>" : parentReport.getReportNum(),
            getReportNum(),
            getDateTime(),
            getWaterSafety(),
            getContaminantPPM(),
            getVirusPPM()
        ));
    }

    /**
     * Compare method for comparing water quality reports based
     * on the time created
     * @param report the water quality report to be compared
     * @return the integer value from comparison
     */
    @Override
    public int compareTo(QualityReport report) {
        return getDateTime().compareTo(report.getDateTime());
    }
}
