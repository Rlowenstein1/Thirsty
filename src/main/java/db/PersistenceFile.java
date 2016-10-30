package main.java.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import com.google.gson.Gson;

import main.java.model.User;
import main.java.model.UserManager;
import main.java.model.QualityReport;
import main.java.model.WaterReport;
import main.java.model.ReportManager;

/**
 * Class that implements persistence by saving and loading from a plain text JSON file
 * 
 */
public class PersistenceFile implements PersistenceInterface {
    private static String userFilename = "../../resources/db/user.json";
    private static String qualityReportFilename = "../../resources/db/qualityReport.json";
    private static String waterReportFilename = "../../resources/db/waterReport.json";

    /**
     * Initializes the persistent entity - not needed for file persistence
     * @return boolean status
     */
    public static boolean initialize() {
        // nothing needed to do here - all local persistence
        return true;
    }

    /**
     * Saves all objects in the manager classes to json files
     * @return boolean status
     */
    public static boolean save() {
        Gson gson = new Gson();
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(userFilename))) {
            List<User> userList = UserManager.getUserList();
            for (User user : userList) {
                // for all current users convert to Json and write to file
                wr.write(gson.toJson(user));
                wr.newLine();
            }
        } catch (Exception e) {
            // error serializing or writing to file
            return false;
        }
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(qualityReportFilename))) {
            List<QualityReport> reportList = ReportManager.getQualityReportList();
            for (QualityReport report : reportList) {
                // for all current users convert to Json and write to file
                wr.write(gson.toJson(report));
                wr.newLine();
            }
        } catch (Exception e) {
            // error serializing or writing to file
            return false;
        }
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(waterReportFilename))) {
            List<WaterReport> reportList = ReportManager.getWaterReportList();
            for (WaterReport report : reportList) {
                // for all current users convert to Json and write to file
                wr.write(gson.toJson(report));
                wr.newLine();
            }
        } catch (Exception e) {
            // error serializing or writing to file
            return false;
        }
        return true;
    }

    /**
     * Loads all objects in Json files to manager classes
     * @return boolean status
     */
    public static boolean load() {
        Gson gson = new Gson();
        // load and read each file - desearialize - add to managers
        try (BufferedReader rd = new BufferedReader(new FileReader(userFilename))) {
            String line;
            while ((line = rd.readLine()) != null) {
                User user = gson.fromJson(line, User.class);
                UserManager.addUser(user);
            }
        } catch (Exception e) {
            // error reading the file
            return false;
        }
        try (BufferedReader rd = new BufferedReader(new FileReader(qualityReportFilename))) {
            String line;
            while ((line = rd.readLine()) != null) {
                QualityReport report = gson.fromJson(line, QualityReport.class);
                ReportManager.addQualityReport(report);
            }
        } catch (Exception e) {
            // error reading the file
            return false;
        }
        try (BufferedReader rd = new BufferedReader(new FileReader(waterReportFilename))) {
            String line;
            while ((line = rd.readLine()) != null) {
                WaterReport report = gson.fromJson(line, WaterReport.class);
                ReportManager.addWaterReport(report);
            }
        } catch (Exception e) {
            // error reading the file
            return false;
        }
        return true;
    }
}
