package persistence.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.SortedSet;

import lib.Debug;
import model.AuthenticationManager;
import model.Credential;
import model.CredentialManager;
import model.QualityReport;
import model.ReportManager;
import model.User;
import model.UserManager;
import model.WaterReport;

/**
 * Dominic Pattison
 */
public class PersistentJsonFile extends PersistentJsonInterface {

    private String pathName;
    private Writer writerUsers;
    private Writer writerCredentials;
    private Writer writerReports;
    private final CredentialManager credentialManager;
    private final AuthenticationManager authenticator;

    public static final String DEFAULT_PATH = "src/main/resources/db/";
    private static final String FILE_EXTENSION = ".json";
    public static final String USER_FILE_NAME = "users" + FILE_EXTENSION;
    public static final String CREDENTIAL_FILE_NAME = "credentials" + FILE_EXTENSION;
    public static final String WR_FILE_NAME = "waterReports" + FILE_EXTENSION;

    /**
     * Constructor that sets the pathname for json files
     * @param path The path folder for the database files to live in
     */
    public PersistentJsonFile(String path) {
        this.pathName = path;
        if (!"src/main/resources/db/".endsWith("/")) {
            this.pathName += "/";
        }
        //TO DO: make sure this directory actually exists; try to create if not; throw exception on failure
        credentialManager = new CredentialManager();
        authenticator = new AuthenticationManager(credentialManager);
    }

    /**
     * Generic function that loads all objects into a list from the given file of json objects
     * @param <T> type of the object to be created from the lines in the given file
     * @param filename to load
     * @param c class of objects in json file - must be consistent
     * @return list of objects of class c
     */
    private <T> List<T> loadAll(String filename, Class<T> c) {
        List<T> res = new ArrayList<>();
        try (BufferedReader rd = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = rd.readLine()) != null) {
                T t = fromJson(line, c);
                if (t != null) {
                    res.add(t);
                }
            }
            return res;
        } catch (FileNotFoundException e) {
            Debug.debug("File does not exist: %s", filename);
            try {
                File f = new File(filename);
                f.createNewFile();
            } catch (AccessDeniedException ee) {
                Debug.debug("Permission denied while creating file: %s\nMessage: %s", filename, ee.toString());
            } catch (IOException ee) {
                Debug.debug("IOException while creating new file: %s\nMessage: %s", filename, ee.toString());
                //e.printStackTrace(System.out);
            }
        } catch (AccessDeniedException e) {
            Debug.debug("Permission denied while reading file: %s", filename);
        } catch (Exception e) {
            Debug.debug("Exception while loading from file: %s\nException message: %s", filename, e.getMessage());
            //e.printStackTrace(System.out);
            // error reading the file
        }
        return (null);
    }

    /**
     * Opens a file for writing
     * @param filename The full (relative or absolute path) name of the file
     * @return null if the operation failed, or an open Writer if successful
     */
    private Writer openFile(String filename) {
        try {
            return (new BufferedWriter(new FileWriter(filename, true)));
        } catch (IOException e) {
            Debug.debug("Exception while opening file for writing: %s\nException: %s", filename, e.toString());
        }
        return (null);
    }

    @Override
    public void initialize() {
        //read all the files into memory
        String usersFile = pathName + USER_FILE_NAME;
        List<User> users = loadAll(usersFile, User.class);
        if (users != null) {
            for (User user : users) {
                UserManager.addUser(user.cloneIt());
            }
        }
        writerUsers = openFile(usersFile);

        String credentialsFile = pathName + CREDENTIAL_FILE_NAME;
        List<Credential> credentials = loadAll(credentialsFile, Credential.class);
        if (credentials != null) {
            credentials.forEach(credentialManager::saveCredential);
        }
        writerCredentials = openFile(credentialsFile);

        String wrFile = pathName + WR_FILE_NAME;
        int maxReportNumber = 0;
        List<WaterReport> wrs = loadAll(wrFile, WaterReport.class);
        Map<Integer, WaterReport> mud = new HashMap<>();
        if (wrs != null) {
            for (WaterReport wr : wrs) {
                mud.put(wr.getReportNum(), wr);
            }
            for (WaterReport wr : mud.values()) {
                WaterReport newWR = wr.cloneIt();
                ReportManager.addWaterReport(newWR);
                if (newWR.getReportNum() > maxReportNumber) {
                    maxReportNumber = newWR.getReportNum();
                }
                SortedSet<QualityReport> qrs = newWR.getQualityReportList();
                int maxQReportNumber = 0;
                for (QualityReport qr : qrs) {
                    if (qr.getReportNum() > maxQReportNumber) {
                        maxQReportNumber = qr.getReportNum();
                    }
                }
                ReportManager.setMaxQualityReportNumber(newWR, maxQReportNumber);
            }
            ReportManager.setMaxWaterReportNumber(maxReportNumber);
        }
        writerReports = openFile(wrFile);
    }

    @Override
    public void terminate() {
        try {
            writerUsers.flush();
            writerUsers.close();
        } catch (IOException e) {
            Debug.debug("Failed to flush and close users file: %s", e.toString());
        }

        try {
            writerCredentials.flush();
            writerCredentials.close();
        } catch (IOException e) {
            Debug.debug("Failed to flush and close credentials file: %s", e.toString());
        }

        try {
            writerReports.flush();
            writerReports.close();
        } catch (IOException e) {
            Debug.debug("Failed to flush and close water reports file: %s", e.toString());
        }
    }

    /**
     * Writes a string to a file
     * @param writer The open Writer to write with
     * @param s The string which shall be written
     */
    private void writeToFile(Writer writer, CharSequence s) {
        try {
            writer.append(s);
            writer.flush();
        } catch (IOException e) {
            Debug.debug("Exception while writing user: %s", e.getMessage());
        }
    }

    @Override
    public User saveUser(User u) {
        writeToFile(writerUsers, toJson(u) + "\n");
        UserManager.addUser(u);
        return (u);
    }

    @Override
    public User authenticateUser(Credential c) {
        Debug.debug("authenticating with %s: %b ? %s : null", c, authenticator.authenticate(c), UserManager.getUser(c.getUsername()));
        return (authenticator.authenticate(c) ? UserManager.getUser(c.getUsername()) : null);
    }

    @Override
    public boolean isUserAuthenticated(String username) {
        return (authenticator.isAuthenticated(username));
    }

    @Override
    public void saveUserCredential(Credential c) {
        credentialManager.saveCredential(c);
        writeToFile(writerCredentials, toJson(c) + "\n");
    }

    @Override
    public void deauthenticateUser(String username) {
        authenticator.logout(username);
    }

    @Override
    public boolean userExists(String username) {
        return (UserManager.userExists(username) && credentialManager.userExists(username));
    }

    @Override
    public void deleteUser(String username) {
        authenticator.logout(username);
        UserManager.deleteUser(username);
        credentialManager.deleteCredential(username);
        //TO DO: delete from file too
    }

    @Override
    public void deleteUser(User u) {
        deleteUser(u.getUsername());
    }

    @Override
    public WaterReport saveWaterReport(WaterReport wr) {
        if (wr == null) {
            return (null);
        }
        ReportManager.addWaterReport(wr);
        writeToFile(writerReports, toJson(wr) + "\n");
        //horrible, just appends the new report, which overwrites the old one when it gets loaded
        return (wr);
    }

    @Override
    public void deleteWaterReport(WaterReport wr) {
        if (wr == null) {
            return;
        }
        //pretty sure this doesn't work, need to completely re-write the file
        saveWaterReport(wr);
    }

    @Override
    public QualityReport saveQualityReport(QualityReport qr) {
        if (qr == null) {
            return (null);
        }
        WaterReport parent = ReportManager.filterWaterReportByNumber(qr.getParentReportNum());
        if (parent == null) {
            return (null);
        }
        ReportManager.addQualityReport(parent, qr);
        saveWaterReport(parent);
        return (qr);
    }

    @Override
    public void deleteQualityReport(QualityReport qr) {
        WaterReport parent = ReportManager.filterWaterReportByNumber(qr.getParentReportNum());
        if (parent == null) {
            return;
        }
        ReportManager.deleteWaterReport(parent);
        saveWaterReport(parent);
    }
}
