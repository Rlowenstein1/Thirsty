package persistence.json.net;

import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.io.IOException;

import lib.Debug;
import model.Credential;
import model.QualityReport;
import model.ReportManager;
import model.User;
import model.UserManager;
import model.WaterReport;
import persistence.json.PersistentJsonInterface;


/**
 */
public class PersistentJsonNetwork extends PersistentJsonNetworkInterface {

    private Credential credential;

    public PersistentJsonNetwork(String hostname, int port) {
        super(hostname, port);
    }

    public void terminate() throws IOException {
        disconnect();
    }

    public User authenticateUser(Credential c) throws IOException {
        credential = c;
        Command resp = sendCommandAndAwaitResponse(Command.CommandType.AUTHENTICATE, null, c);
        
        if (!resp.isSuccessful()) {
            Debug.debug("Unsuccessful authentication: %s", resp.getMessage());
            //maybe throw an exception so the message can be displayed to the user?
        }
        return (resp.isSuccessful() ? fromJson(resp.getData(), User.class) : null);
    }

    public void deauthenticateUser(String username) throws IOException {
        Command resp = sendCommandAndAwaitResponse(Command.CommandType.DEAUTHENTICATE, null, credential);
        
        if (!resp.isSuccessful()) {
            Debug.debug("Unsuccessful deauthentication: %s", resp.getMessage());
            //maybe throw an exception so the message can be displayed to the user?
        } else {
            Debug.debug("Successful deauthentication");
            //maybe throw an exception so the message can be displayed to the user?
        }
    }

    public boolean isUserAuthenticated(String username) throws IOException {
        // net stuff
        return false;
    }

    public void saveUserCredential(Credential c) throws IOException {
        // net stuff
    }

    public boolean userExists(String username) throws IOException {
        // user manager should already be loaded with users
        return UserManager.userExists(username);
    }

    public void saveUser(User u) throws IOException {
        Command resp = sendCommandAndAwaitResponse(Command.CommandType.SAVE_USER, toJson(u), credential);
        
        if (!resp.isSuccessful()) {
            Debug.debug("Unsuccessful user save: %s", resp.getMessage());
            //maybe throw an exception so the message can be displayed to the user?
        } else {
            Debug.debug("Sucessfully saved user");
        }
    }

    public void deleteUser(User u) throws IOException {
        Command resp = sendCommandAndAwaitResponse(Command.CommandType.DELETE_USER, toJson(u), credential);
        
        if (!resp.isSuccessful()) {
            Debug.debug("Unsuccessful user delete: %s", resp.getMessage());
            //maybe throw an exception so the message can be displayed to the user?
        } else {
            Debug.debug("Sucessfully deleted user");
        }
    }

    public void deleteUser(String username) throws IOException {
        User u = UserManager.getUser(username);
        Command resp = sendCommandAndAwaitResponse(Command.CommandType.DELETE_USER, toJson(u), credential);
        
        if (!resp.isSuccessful()) {
            Debug.debug("Unsuccessful user delete: %s", resp.getMessage());
            //maybe throw an exception so the message can be displayed to the user?
        } else {
            Debug.debug("Sucessfully deleted user");
        }
        // net stuff
    }

    public void saveWaterReport(WaterReport wr) throws IOException {
        Command resp = sendCommandAndAwaitResponse(Command.CommandType.SAVE_WATER_REPORT, toJson(wr), credential);
        
        if (!resp.isSuccessful()) {
            Debug.debug("Unsuccessful water report save: %s", resp.getMessage());
            //maybe throw an exception so the message can be displayed to the user?
        } else {
            Debug.debug("Sucessfully saved water report");
        }
    }

    public void deleteWaterReport(WaterReport wr) throws IOException {
        Command resp = sendCommandAndAwaitResponse(Command.CommandType.DELETE_WATER_REPORT, toJson(wr), credential);
        
        if (!resp.isSuccessful()) {
            Debug.debug("Unsuccessful water report delete: %s", resp.getMessage());
            //maybe throw an exception so the message can be displayed to the user?
        } else {
            Debug.debug("Sucessfully deleted water report");
        }
    }

    public void saveQualityReport(WaterReport wr) throws IOException {
        saveWaterReport(wr); // using the water report to delete
    }

    public void deleteQualityReport(WaterReport wr) throws IOException {
        deleteWaterReport(wr); // using the water report to delete
    }

    @Override
    public void addUser(User user) {
        UserManager.addUser(user);
    }

    @Override
    public void addWaterReport(WaterReport wr) {
        Debug.debug("adding water report: %s", wr);
        ReportManager.addWaterReport(wr);
    }

    @Override
    public void addQualityReport(QualityReport qr) {
        WaterReport parent = ReportManager.filterWaterReportByNumber(qr.getParentReportNum());
        if (parent != null) {
            ReportManager.addQualityReport(parent, qr);
        }
    }
}
