package persistence.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lib.Debug;
import model.QualityReport;
import model.ReportManager;
import model.User;
import model.UserManager;
import model.WaterReport;

import java.io.File;

public class PersistentJSONFile extends PersistentJSONInterface {

    private String pathName;
    private BufferedWriter writerUsers;
    private BufferedWriter writerWaterReports;
    private HashMap<WaterReport, BufferedWriter> writerQualityReports = new HashMap<>();

    private static final String FILE_EXTENSION = ".json";
    private static final String USER_FILE_NAME = "users";
    private static final String WR_FILE_NAME = "water_report";
    private static final String QR_FILE_NAME = "quality_report_";

    public PersistentJSONFile(String pathName) {
        this.pathName = pathName;
        if (!pathName.endsWith("/")) {
            this.pathName += "/";
        }
        //TODO: make sure this directory actually exists; try to create if not; throw exception on failure
    }

    private <T> List<T> loadAll(String filename, Class<T> c) {
        ArrayList<T> res = new ArrayList<>();
        try (BufferedReader rd = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = rd.readLine()) != null) {
                T t = fromJSON(line, c);
                if (t != null) {
                    res.add(t);
                }
            }
            return (res);
        } catch (FileNotFoundException e) {
            Debug.debug("File does not exist: %s", filename);
        } catch (AccessDeniedException e) {
            Debug.debug("Permission denied while reading file: %s", filename);
        } catch (Exception e) {
            Debug.debug("Exception while loading from file: %s\nException message: %s", filename, e.getMessage());
            e.printStackTrace(System.out);
            // error reading the file
        }
        return (null);
    }

    @Override
    public void initialize() {
        //read all the files into memory
        List<User> users = loadAll(pathName + USER_FILE_NAME + FILE_EXTENSION, User.class);
        for (User user : users) {
            UserManager.addUser(user);
        }

        List<WaterReport> wrs = loadAll(pathName + WR_FILE_NAME + FILE_EXTENSION, WaterReport.class);
        for (WaterReport wr : wrs) {
            ReportManager.addWaterReport(wr);
            List<QualityReport> qrs = loadAll(pathName + QR_FILE_NAME + Integer.toString(wr.getReportNum()) + FILE_EXTENSION, QualityReport.class);
            for (QualityReport qr : qrs) {
                wr.addQualityReport(qr);
            }
        }
    }

    @Override
    public void terminate() {
    }

    @Override
    public void saveUser(User u) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User loadUser(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteUser(User u) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveWaterReport(WaterReport wr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<WaterReport> loadWaterReports() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteWaterReport(WaterReport wr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveQualityReport(WaterReport wr, QualityReport qr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<QualityReport> loadQualityReports(WaterReport wr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteQualityReport(WaterReport wr, QualityReport qr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
