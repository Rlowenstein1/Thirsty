/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lib.Debug;
import model.Authenticator;
import model.Credential;
import model.CredentialManager;
import model.QualityReport;
import model.ReportManager;
import model.User;
import model.UserManager;
import model.WaterReport;

/**
 *
 * @author tybrown
 */
public class PersistentJSONFile extends PersistentJSONInterface {

    private String pathName;
    private Writer writerUsers;
    private Writer writerCredentials;
    private Writer writerReports;
    //private HashMap<WaterReport, Writer> writerQualityReports = new HashMap<>();
    private CredentialManager credentialManager;
    private Authenticator authenticator;

    private static final String FILE_EXTENSION = ".json";
    private static final String USER_FILE_NAME = "users" + FILE_EXTENSION;
    private static final String CREDENTIAL_FILE_NAME = "credentials" + FILE_EXTENSION;
    private static final String WR_FILE_NAME = "waterReports" + FILE_EXTENSION;

    public PersistentJSONFile(String pathName) {
        this.pathName = pathName;
        if (!pathName.endsWith("/")) {
            this.pathName += "/";
        }
        //TODO: make sure this directory actually exists; try to create if not; throw exception on failure
        credentialManager = new CredentialManager();
        authenticator = new Authenticator(credentialManager);
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
            try {
                File f = new File(filename);
                f.createNewFile();
            } catch (AccessDeniedException ee) {
                Debug.debug("Permission denied while creating file: %s\nMessage: %s", filename, ee.toString());
            } catch (IOException ee) {
                Debug.debug("IOException while creating new file: %s\nMessage: %s", filename, ee.toString());
                e.printStackTrace(System.out);
            }
        } catch (AccessDeniedException e) {
            Debug.debug("Permission denied while reading file: %s", filename);
        } catch (Exception e) {
            Debug.debug("Exception while loading from file: %s\nException message: %s", filename, e.getMessage());
            e.printStackTrace(System.out);
            // error reading the file
        }
        return (null);
    }

    private BufferedWriter openFile(String filename) {
        try {
            Debug.debug("opening file for writing: %s", filename);
            BufferedWriter res = new BufferedWriter(new FileWriter(filename, true)); //true = append
            return (res);
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
                Debug.debug("loaded user: %s", user);
                UserManager.addUser(user);
            }
        }
        writerUsers = openFile(usersFile);

        String credentialsFile = pathName + CREDENTIAL_FILE_NAME;
        List<Credential> credentials = loadAll(credentialsFile, Credential.class);
        if (credentials != null) {
            for (Credential credential : credentials) {
                Debug.debug("loaded credential: %s", credential);
                credentialManager.addCredential(credential);
            }
        }
        writerCredentials = openFile(credentialsFile);

        String wrFile = pathName + WR_FILE_NAME;
        List<WaterReport> wrs = loadAll(wrFile, WaterReport.class);
        if (wrs != null) {
            for (WaterReport wr : wrs) {
                Debug.debug("loaded water report: %s", wr);
                ReportManager.addWaterReport(wr);
                List<QualityReport> qrs = wr.getQualityReportList();
                Debug.debug("Contains quality reports: ");
                for (QualityReport qr : qrs) {
                    Debug.debug("  %s", qr);
                }
                /*
                String qrFile = pathName + QR_FILE_NAME + Integer.toString(wr.getReportNum()) + FILE_EXTENSION;
                List<QualityReport> qrs = loadAll(qrFile, QualityReport.class);
                if (qrs != null) {
                    for (QualityReport qr : qrs) {
                        wr.addQualityReport(qr);
                        qr.setParentReport(wr);
                    }
                }
                writerQualityReports.put(wr, openFile(qrFile));
                */
            }
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

    private boolean writeToFile(Writer writer, String s) {
        try {
            writer.append(s);
            writer.flush();
            return (true);
        } catch (IOException e) {
            Debug.debug("Exception while writing user: %s", e.getMessage());
        }
        return (false);
    }

    @Override
    public void saveUser(User u) {
        writeToFile(writerUsers, toJSON(u, User.class) + "\n");
    }

    @Override
    public User loadUser(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean authenticateUser(Credential c) {
        return (authenticator.authenticate(c));
    }

    @Override
    public boolean isUserAuthenticated(String username) {
        return (authenticator.isAuthenticated(username));
    }

    @Override
    public void saveUserCredential(Credential c) {
        //TODO: update password instead of just writing the new one
        writeToFile(writerCredentials, toJSON(c, Credential.class) + "\n");
    }

    @Override
    public void deauthenticateUser(String username) {
        authenticator.logout(username);
    }

    @Override
    public boolean userExists(String username) {
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
