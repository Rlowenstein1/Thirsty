package model;

//import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.HashMap;
//import java.util.stream.Collectors;
import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import persistence.PersistenceInterface;
import lib.Debug;

/**
 * Manager for the Report classes
 */
public class ReportManager {
    private static SortedSet<WaterReport> waterReports;
    private static int reportNumber = 0;
    private static HashMap<WaterReport, Integer> qualityReportNumberMap;

    private static PersistenceInterface persist;

    /**
     * Sets up the user manager. This method should only be called once
     * @param persist The PersistenceInterface to use to manage users
     */
    public static void initialize(PersistenceInterface persist) {
        ReportManager.persist = persist;
        waterReports = Collections.synchronizedSortedSet(new TreeSet<WaterReport>());
        qualityReportNumberMap = new HashMap<>();
    }

    /**
     * Sets the max water report number. Assists with adding full reports from persistence
     * @param maxReportNumber the new number
     */
    public static void setMaxWaterReportNumber(int maxReportNumber) {
        ReportManager.reportNumber = maxReportNumber;
    }

    /**
     * Sets the max quality report number. Assists with adding full reports from persistence
     * @param wr water report to set max quality report number for
     * @param maxReportNumber the new number
     */
    public static void setMaxQualityReportNumber(WaterReport wr, int maxReportNumber) {
        ReportManager.qualityReportNumberMap.put(wr, maxReportNumber);
    }

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
        reportNumber = reportNumber + 1;
        WaterReport r = new WaterReport(reportNumber, latitude, longitude, type, condition, author);
        try {
            persist.saveWaterReport(r);
        } catch (IOException e) {
            Debug.debug("Error in saving water report");
        }
        addWaterReport(r);
        qualityReportNumberMap.put(r, 0);
        return (r);
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
        reportNumber = reportNumber + 1;
        WaterReport r = new WaterReport(reportNumber, dateTime, latitude, longitude, type, condition, author);
        try {
            persist.saveWaterReport(r);
        } catch (IOException e) {
            Debug.debug("Error in saving water report");
        }
        addWaterReport(r);
        qualityReportNumberMap.put(r, 0);
        return (r);
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
        waterReport.addQualityReport(report);
        try {
            persist.saveQualityReport(waterReport);
        } catch (IOException e) {
            Debug.debug("Error in saving water report");
        }
        return (report);
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
    public static QualityReport createWaterQualityReport(LocalDateTime dateTime,
                                                         WaterReport waterReport, WaterSafety safety,
                double vppm, double cppm, User author) {
        Integer qualityReportNum = qualityReportNumberMap.get(waterReport) + 1;
        qualityReportNumberMap.put(waterReport, qualityReportNum);
        QualityReport report = new QualityReport(dateTime, qualityReportNum, author, safety, vppm, cppm, waterReport);
        waterReport.addQualityReport(report);
        try {
            persist.saveQualityReport(waterReport);
        } catch (IOException e) {
            Debug.debug("Error in saving water report");
        }
        return (report);
    }

    /**
     * Add existing quality report to water report
     * @param report WaterReport object to add to list
     */
    public static void addQualityReport(WaterReport parent, QualityReport report) {
        if (qualityReportNumberMap.containsKey(parent)) {
            Integer qualityReportNum = qualityReportNumberMap.get(parent) + 1;
            if (report.getReportNum() > qualityReportNum) {
                qualityReportNumberMap.put(parent, qualityReportNum);
            }
        } else {
            qualityReportNumberMap.put(parent, 1);
        }
        parent.addQualityReport(report);
    }

    /**
     * Add existing water report to list
     * @param report WaterReport object to add to list
     */
    public static void addWaterReport(WaterReport report) {
        if (report.getReportNum() > reportNumber) {
            reportNumber = report.getReportNum();
        }
        waterReports.add(report);
    }

    /**
     * Deletes specified report from the list
     * @param waterReport to be deleted
     */
    public static void deleteWaterReport(WaterReport waterReport) {
        try {
            persist.deleteWaterReport(waterReport);
        } catch (IOException e) {
            Debug.debug("Error in saving water report");
        }
        waterReports.remove(waterReport);
    }

    /**
     * Deletes specified report from its parent
     * @param qualityReport the QualityReport to be deleted from its parent
     */

    public static void deleteQualityReport(QualityReport qualityReport) {
        WaterReport waterReport = qualityReport.getParentReport();
        if (waterReport != null) {
            deleteQualityReport(waterReport, qualityReport);
        }
    }


    /**
     * Deletes specified report from the given WaterReport
     * @param waterReport The parent WaterReport to add this QualityReport to
     * @param qualityReport the QualityReport to be deleted
     */
    private static void deleteQualityReport(WaterReport waterReport, QualityReport qualityReport) {
        try {
            persist.deleteQualityReport(waterReport);
        } catch (IOException e) {
            Debug.debug("Error in saving water report");
        }
        waterReport.removeQualityReport(qualityReport);
    }

    /**
     * Returns the list of water reports in order they were created
     * @return water report list
     */
    public static List<WaterReport> getWaterReportList() {
        return new ArrayList<>(waterReports);
    }

    /**
     * Sorts the list of water reports
     * @return sorted water report list by data (natural ordering)
     */
    /*
    public static List<WaterReport> sortWaterReportByDate() {
        List<WaterReport> list = new ArrayList<>(waterReports);
        Collections.sort(list);
        return list;
    }
    */

    /**
     * Filters the report list by the author
     * @param user that authored reports
     * @return list containing only reports authored by the user
     */
    /*
    public static List<WaterReport> filterWaterReportByUser(User user) {
        List<WaterReport> list = new ArrayList<>(waterReports.size());
        list.addAll(waterReports.stream().filter(report ->
                user.equals(report.getAuthor())).collect(Collectors.toList()));
        return list;
    }
    */

    /**
     * Filters the quality report list by the author
     * @param user that authored reports
     * @return list containing only quality reports authored by the user
     */
    /*
    public static List<QualityReport> filterQualityReportByUser(User user) {
        List<QualityReport> list = new ArrayList<>();
        for (WaterReport wr : waterReports) {
            for (QualityReport qr : wr.getQualityReportList()) {
                if (user.equals(qr.getAuthor())) {
                    list.add(qr);
                }

            }
        }
        return (list);
    }
    */

    /**
     * Filters the report list by the report number
     * @param num the number of the report
     * @return either the report, or null if not found
     */
    public static WaterReport filterWaterReportByNumber(int num) {
        if ((num > 0) && (num <= reportNumber)) {
            for (WaterReport report: waterReports) {
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
    /*
    public static List<WaterReport> sortWaterReportByName() {
        List<WaterReport> list = new ArrayList<>(waterReports);
        Collections.sort(list, (WaterReport a, WaterReport b) ->
            a.getAuthor().getName().compareTo(b.getAuthor().getName())
        );
        return list;
    }
    */
}
