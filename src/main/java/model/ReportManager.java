package model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDateTime;

/**
 * Manager for the Report classes
 */
public class ReportManager {
    private static Set<WaterReport> waterReportSet = new HashSet<>(); 
    private static Set<QualityReport> qualityReportSet = new HashSet<>();
    private static int reportNumber = 1;
    private static int qualityReportNumber = 1;

    /**
     * Creates a water report
     * @param latitude GPS latitude coordinate of the report
     * @param longitude GPS longitude coordinate of the report
     * @param type of the report
     * @param condition of the report
     * @param author Author of the report
     * @return the status of the operation
     */
    public static WaterReport createWaterReport(double latitude, double longitude, WaterType type,
                WaterCondition condition, User author) {
        WaterReport r = new WaterReport(reportNumber++, latitude, longitude, type, condition, author);
        if (waterReportSet.add(r)) {
            return (r);
        } else {
            return (null);
        }
    }

    /**
     * Creates a water report
     * @param dateTime of the report
     * @param latitude GPS latitude coordinate of the report
     * @param longitude GPS longitude coordinate of the report
     * @param type of the report
     * @param condition of the report
     * @param author Author of the report
     * @return The WaterReport added, or null if the report already exists
     */
    public static WaterReport createWaterReport(LocalDateTime dateTime, double latitude, double longitude,
                WaterType type, WaterCondition condition, User author) {
        WaterReport r = new WaterReport(reportNumber++, dateTime, latitude, longitude, type, condition, author);
        if (waterReportSet.add(r)) {
            return (r);
        } else {
            return (null);
        }
    }

    /**
     * Creates quality report
     * @param waterReport availability report to add quality report to
     * @param safety of the water source
     * @param vppm virus parts per million
     * @param cppm contaminent parts per million
     * @param author of the report
     * @return the quality report added, or null if the report already exists
     */
    public static QualityReport createWaterQualityReport(WaterReport waterReport, WaterSafety safety,
                double vppm, double cppm, User author) {
        QualityReport report = new QualityReport(qualityReportNumber++, author, safety, vppm, cppm, waterReport);
        if (qualityReportSet.add(report)) {
            return (report);
        } else {
            return (null);
        }
    }

    /**
     * Creates quality report
     * @param dateTime of the report creation
     * @param waterReport availability report to add quality report to
     * @param safety of the water source
     * @param vppm virus parts per million
     * @param cppm contaminent parts per million
     * @param author of the report
     * @return the quality report added, or null if the report already exists
     */
    public static QualityReport createWaterQualityReport(LocalDateTime dateTime, WaterReport waterReport, WaterSafety safety,
                double vppm, double cppm, User author) {
        QualityReport report = new QualityReport(dateTime, qualityReportNumber++, author, safety, vppm, cppm, waterReport);
        if (qualityReportSet.add(report)) {
            return (report);
        } else {
            return (null);
        }
    }

    /**
     * Deletes specified report from the list
     * @param waterReport to be deleted
     */
    public static void deleteWaterReport(WaterReport waterReport) {
        waterReportSet.remove(waterReport);
    }

    /**
     * Deletes specified report from the list
     * @param report to be deleted
     */
    public static void deleteQualityReport(QualityReport report) {
        qualityReportSet.remove(report);
    }

    /**
     * Returns the list of water reports in order they were created
     * @return water report list
     */
    public static List<WaterReport> getWaterReportlist() {
        return new ArrayList<>(waterReportSet);
    }

    /**
     * Returns the list of quality reports in order they were created
     * @return quality report list
     */
    public static List<QualityReport> getQualityReportList() {
        return new ArrayList<>(qualityReportSet);
    }

    /**
     * Sorts the list of water reports
     * @return sorted water report list by data (natural ordering)
     */
    public static List<WaterReport> sortWaterReportByDate() {
        List<WaterReport> list = new ArrayList<>(waterReportSet);
        Collections.sort(list);
        return list;
    }

    /**
     * Sorts the list of quality reports
     * @return sorted quality report list by date (natural ordering)
     */
    public static List<QualityReport> sortQualityReportByDate() {
        List<QualityReport> list = new ArrayList<>(qualityReportSet);
        Collections.sort(list);
        return list;
    }

    /**
     * Filters the report list by the author
     * @param user that authored reports
     * @return list containing only reports authored by the user
     */
    public static List<WaterReport> filterWaterReportByUser(User user) {
        List<WaterReport> list = new ArrayList<>(waterReportSet.size());
        for (WaterReport report : waterReportSet) {
            if (user.equals(report.getAuthor())) {
                list.add(report);
            }
        }
        return list;
    }

    /**
     * Filters the quality report list by the author
     * @param user that authored reports
     * @return list containing only quality reports authored by the user
     */
    public static List<QualityReport> filterQualityReportByUser(User user) {
        List<QualityReport> list = new ArrayList<>(qualityReportSet.size());
        for (QualityReport report : qualityReportSet) {
            if (user.equals(report.getAuthor())) {
                list.add(report);
            }
        }
        return list;
    }

    /**
     * Filters the report list by the report number
     * @param num the number of the report
     * @return either the report, or null if not found
     */
    public static WaterReport filterWaterReportByNumber(int num) {
        if (num > 0 && num < reportNumber) {
            for (WaterReport report: waterReportSet) {
                if (report.getReportNum() == num) {
                    return (report);
                }
            }
        }
        return null;
    }

    /**
     * Filters the quality report list by the report number
     * @param num the number of the report
     * @return either the report, or null if not found
     */
    public static QualityReport filterQualityReportByNumber(int num) {
        if (num > 0 && num < qualityReportNumber) {
            for (QualityReport report: qualityReportSet) {
                if (report.getReportNum() == num) {
                    return (report);
                }
            }
        }
        return null;
    }

    /**
     * Sorts the water reports by alphabetical order of the name of the author
     * @return the sorted report list
     */
    public static List<WaterReport> sortWaterReportByName() {
        List<WaterReport> list = new ArrayList<>(waterReportSet);
        Collections.sort(list, (WaterReport a, WaterReport b) ->
            a.getAuthor().getName().compareTo(b.getAuthor().getName())
        );
        return list;
    }

    /**
     * Sorts the quality reports by alphabetical order of the name of the author
     * @return the sorted report list
     */
    public static List<QualityReport> sortQaultiyReportByName() {
        List<QualityReport> list = new ArrayList<>(qualityReportSet);
        Collections.sort(list, (QualityReport a, QualityReport b) ->
            a.getAuthor().getName().compareTo(b.getAuthor().getName())
        );
        return list;
    }
}
