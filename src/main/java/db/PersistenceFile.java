package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import com.google.gson.Gson;

import model.User;
import model.UserManager;
import model.QualityReport;
import model.WaterReport;
import model.ReportManager;

/**
 * Class that implements persistence by saving and loading from a plain text JSON file
 * 
 */
public class PersistenceFile extends PersistenceAbstractObject {
    private static PersistenceAbstractObject instance = new PersistenceFile();

    private static String userFilename = "src/main/resources/db/user.json";
    private static String qualityReportFilename = "src/main/resources/db/qualityReport.json";
    private static String waterReportFilename = "src/main/resources/db/waterReport.json";


    /**
     * Gets the static instance - initializes if not initialized
     * @return the static instance
     */
    public static PersistenceAbstractObject getInstance() {
        if (!instance.isInitialized)
            instance.isInitialized = instance.initialize();
        return (instance.isInitialized) ? instance : null;
    }

    @Override
    protected boolean initialize() {
        // nothing to do here - all local persistence
        return true;
    }

    @Override
    public boolean save() {
        Gson gson = new Gson();
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(userFilename))) {
            List<User> userList = UserManager.getUserList();
            for (User user : userList) {
                // for all current users convert to Json and write to file
                wr.write(gson.toJson(user));
                wr.newLine();
            }
            wr.close();
        } catch (Exception e) {
            System.out.println("exception saving users: " + e.getMessage());
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
            System.out.println("exception saving quality reports: " + e.getMessage());
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
            System.out.println("exception saving water reports: " + e.getMessage());
            // error serializing or writing to file
            return false;
        }
        return true;
    }

    @Override
    public boolean load() {
        return loadUsers() && loadReports();
    }

    @Override
    public boolean saveUser(User user) {
        return false;
    }

    @Override
    public boolean loadUsers() {
        Gson gson = new Gson();
        // load and read each file - desearialize - add to managers
        try (BufferedReader rd = new BufferedReader(new FileReader(userFilename))) {
            String line;
            while ((line = rd.readLine()) != null) {
                User user = gson.fromJson(line, User.class);
                UserManager.addUser(user);
            }
        } catch (Exception e) {
            System.out.println("exception loading users: " + e.getMessage());
            e.printStackTrace(System.out);
            // error reading the file
            return false;
        }
        return true;
    }
    @Override
    public boolean saveReport(WaterReport report) {
        return false;
    }

    @Override
    public boolean saveReport(QualityReport report) {
        return false;
    }

    @Override
    public boolean loadReports() {
        Gson gson = new Gson();
        // load and read each file - deserialize - add to managers
        try (BufferedReader rd = new BufferedReader(new FileReader(qualityReportFilename))) {
            String line;
            while ((line = rd.readLine()) != null) {
                QualityReport report = gson.fromJson(line, QualityReport.class);
                ReportManager.addQualityReport(report);
            }
        } catch (Exception e) {
            System.out.println("exception loading quality reports: " + e.getMessage());
            e.printStackTrace(System.out);
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
            System.out.println("exception loading water reports: " + e.getMessage());
            e.printStackTrace(System.out);
            // error reading the file
            return false;
        }
        return true;
    }
}
