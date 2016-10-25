package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDateTime;
import java.time.Month;

/**
 * Represents a water quality report.
 */
public class QualityReport extends RecursiveTreeObject<QualityReport> implements Comparable<QualityReport> {
    private final SimpleObjectProperty<LocalDateTime> dateTime = new SimpleObjectProperty<>();
    private final SimpleIntegerProperty reportNum = new SimpleIntegerProperty();
    private final SimpleObjectProperty<User> author = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<WaterSafety> condition = new SimpleObjectProperty<>();
    private final SimpleDoubleProperty virusPPM = new SimpleDoubleProperty();
    private final SimpleDoubleProperty contaminantPPM = new SimpleDoubleProperty();
    private final SimpleObjectProperty<WaterReport> waterReport = new SimpleObjectProperty<>();

    /**
     * Constructor for a new water source report.
     * The date and time are automatically generated.
     * @param reportNum the report number
     * @param author the author
     * @param condition overall condition of water
     * @param virusPPM the virus PPM
     * @param contaminantPPM the contaminant PPM
     */
    public QualityReport(int reportNum, User author, WaterSafety condition, double virusPPM, double contaminantPPM, WaterReport waterReport) {
        this(LocalDateTime.now(), reportNum, author, condition, virusPPM, contaminantPPM, waterReport);
    }

    /**
     * Constructor for a new water quality report with all fields
     * entered in manually.
     * @param dateTime the date and time of creation
     * @param reportNum the report number
     * @param author the author
     * @param condition overall condition of water
     * @param virusPPM the virus PPM
     * @param contaminantPPM the contaminant PPM
     */
    public QualityReport(LocalDateTime dateTime, int reportNum, User author, WaterSafety condition, double virusPPM, double contaminantPPM, WaterReport waterReport) {
        this.dateTime.set(dateTime);
        this.reportNum.set(reportNum);
        this.author.set(author);
        this.condition.set(condition);
        this.virusPPM.set(virusPPM);
        this.contaminantPPM.set(contaminantPPM);
        this.waterReport.set(waterReport);
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
    public ObjectProperty<LocalDateTime> getDateTimeProperty() {
        return dateTime;
    }

    /**
     * Gets the water safety condition
     * @return the type
     */
    public WaterSafety getWaterCondition() {
        return condition.get();
    }

    /**
     * Sets the condition of water
     * @param t the new type of water
     */
    public void setWaterCondition(WaterSafety t) {
        condition.set(t);
    }

    /**
     * Gets this water report's water condition
     * @return the water type property
     */
    public ObjectProperty<WaterSafety> getWaterConditionProperty() {
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
    public ObjectProperty<User> getAuthorProperty() {
        return author;
    }

    /**
     * Gets the virus PPM for this water report
     * @return the virus PPM
     */
    public double getVirusPPM() {
        return virusPPM.get();
    }

    /**
     * Sets the virus PPM for this water source
     * @param virus the new virus PPM
     */
    public void setVirusPPM(double virus) {
        virusPPM.set(virus);
    }

    /**
     * Gets the virus PPM property for this water report
     * @return the virus PPM property
     */
    public SimpleDoubleProperty getVirusProperty() {
        return virusPPM;
    }

    /**
     * Gets the contaminant PPM for this water report
     * @return the contaminant PPM
     */
    public double getContaminantPPM() {
        return contaminantPPM.get();
    }

    /**
     * Sets the contaminant PPM for this water report
     * @param contaminant the new contaminant PPM
     */
    public void setContaminantPPM(double contaminant) {
        contaminantPPM.set(contaminant);
    }

    /**
     * Gets the contaminant PPM property for this water report
     * @return the contaminant PPM property
     */
    public SimpleDoubleProperty getContaminantProperty() {
        return contaminantPPM;
    }

    /**
     * Gets the water source report attached to this quality report
     * @return the water source report
     */
    public WaterReport getWaterReport() {
        return waterReport.get();
    }

    /**
     * Sets the water source report to a new one
     * @param wr the new water source report attached to this quality report
     */
    public void setWaterReport(WaterReport wr) {
        waterReport.set(wr);
    }

    /**
     * Gets the water source report property
     * @return the water source report property
     */
    public SimpleObjectProperty<WaterReport> getWaterReportProperty() {
        return  waterReport;
    }

    /**
     * String representation for this water quality report
     * @return a string representation of this water quality report
     */
    @Override
    public String toString() {
        return "Quality report number: " + reportNum.get() + "\n"
                + "Date and time: " + dateTime.get() + "\n"
                + "Condition of water: " + condition.get()
                + "Virus PPM: " + virusPPM.get() + "\n"
                + "Contaminant PPM " + contaminantPPM.get() + "\n"
                + waterReport.get().toString();
    }

    /**
     * Compare method for comparing water quality reports based
     * on the time created
     * @param report the water quality report to be compared
     * @return the integer value from comparison
     */
    @Override
    public int compareTo(QualityReport report) {
        return this.dateTime.get().compareTo(report.getDateTimeProperty().get());
    }
}
