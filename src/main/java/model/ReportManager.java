package main.java.model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Manager for the Report classes
 */
public class ReportManager {
    private static List<WaterReport> waterReportList = new ArrayList<>(); 
    private static List<QualityReport> qualityReportList = new ArrayList<>();
    private static int reportNumber = 1;
    private static HashMap<WaterReport, Integer> qualityReportNumberMap = new HashMap<>();

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
        if (waterReportList.add(r)) {
            qualityReportNumberMap.put(r, 0);
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
        if (waterReportList.add(r)) {
            qualityReportNumberMap.put(r, 0);
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
     * @param cppm contaminant parts per million
     * @param author of the report
     * @return the quality report added, or null if the report already exists
     */
    public static QualityReport createWaterQualityReport(WaterReport waterReport, WaterSafety safety,
                double vppm, double cppm, User author) {
        Integer qualityReportNum = qualityReportNumberMap.get(waterReport) + 1;
        qualityReportNumberMap.put(waterReport, qualityReportNum);
        QualityReport report = new QualityReport(qualityReportNum, author, safety, vppm, cppm, waterReport);
        if (qualityReportList.add(report)) {
            waterReport.addQualityReport(report);
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
     * @param cppm contaminant parts per million
     * @param author of the report
     * @return the quality report added, or null if the report already exists
     */
    public static QualityReport createWaterQualityReport(LocalDateTime dateTime, WaterReport waterReport, WaterSafety safety,
                double vppm, double cppm, User author) {
        Integer qualityReportNum = qualityReportNumberMap.get(waterReport) + 1;
        qualityReportNumberMap.put(waterReport, qualityReportNum);
        QualityReport report = new QualityReport(dateTime, qualityReportNum, author, safety, vppm, cppm, waterReport);
        if (qualityReportList.add(report)) {
            waterReport.addQualityReport(report);
            return (report);
        } else {
            return (null);
        }
    }

    /**
     * Add existing water report to list
     * @param report WaterReport object to add to list
     */
    public static void addWaterReport(WaterReport report) {
        waterReportList.add(report);
    }

    /**
     * Add existing quality report to list
     */
    public static void addQualityReport(QualityReport report) {
        qualityReportList.add(report);
    }

    /**
     * Deletes specified report from the list
     * @param waterReport to be deleted
     */
    public static void deleteWaterReport(WaterReport waterReport) {
        waterReportList.remove(waterReport);
    }

    /**
     * Deletes specified report from the list
     * @param report to be deleted
     */
    public static void deleteQualityReport(QualityReport report) {
        WaterReport parent = report.getParentReport();
        if (parent != null) {
            parent.removeQualityReport(report);
        }
        qualityReportList.remove(report);
    }

    /**
     * Returns the list of water reports in order they were created
     * @return water report list
     */
    public static List<WaterReport> getWaterReportList() {
        return new ArrayList<>(waterReportList);
    }

    /**
     * Returns the list of all quality reports in order they were created
     * @return a list of all quality reports
     */
    public static List<QualityReport> getQualityReportList() {
        return new ArrayList<>(qualityReportList);
    }

    /**
     * Returns the list of quality reports associated with a WaterReport in order they were created
     * @param r the water report to get the list of quality reports from
     * @return quality report list
     */
    public static List<QualityReport> getQualityReportList(WaterReport r) {
        return r.getQualityReportList();
    }

    /**
     * Sorts the list of water reports
     * @return sorted water report list by data (natural ordering)
     */
    public static List<WaterReport> sortWaterReportByDate() {
        List<WaterReport> list = new ArrayList<>(waterReportList);
        Collections.sort(list);
        return list;
    }

    /**
     * Sorts the list of quality reports
     * @return sorted quality report list by date (natural ordering)
     */
    public static List<QualityReport> sortQualityReportByDate() {
        List<QualityReport> list = new ArrayList<>(qualityReportList);
        Collections.sort(list);
        return list;
    }

    /**
     * Filters the report list by the author
     * @param user that authored reports
     * @return list containing only reports authored by the user
     */
    public static List<WaterReport> filterWaterReportByUser(User user) {
        List<WaterReport> list = new ArrayList<>(waterReportList.size());
        for (WaterReport report : waterReportList) {
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
        List<QualityReport> list = new ArrayList<>(qualityReportList.size());
        for (QualityReport report : qualityReportList) {
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
            for (WaterReport report: waterReportList) {
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
     * @param waterReport the parent WaterReport to search through
     * @return either the report, or null if not found
     */
    public static QualityReport filterQualityReportByNumber(WaterReport waterReport, int num) {
        return (waterReport.getQualityReportByNumber(num));
    }

    /**
     * Sorts the water reports by alphabetical order of the name of the author
     * @return the sorted report list
     */
    public static List<WaterReport> sortWaterReportByName() {
        List<WaterReport> list = new ArrayList<>(waterReportList);
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
        List<QualityReport> list = new ArrayList<>(qualityReportList);
        Collections.sort(list, (QualityReport a, QualityReport b) ->
            a.getAuthor().getName().compareTo(b.getAuthor().getName())
        );
        return list;
    }
}
