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
    private boolean authed = false;

    public PersistentJsonNetwork(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public void terminate() throws IOException {
        disconnect();
    }

    @Override
    public User authenticateUser(Credential c) throws IOException {
        Command resp = sendCommandAndAwaitResponse(Command.CommandType.AUTHENTICATE, toJson(c), null);
        
        if (!resp.isSuccessful()) {
            Debug.debug("Unsuccessful authentication: %s", resp.getMessage());
            //maybe throw an exception so the message can be displayed to the user?
        } else {
            credential = resp.getCredential();
            Debug.debug("user authenticated (raw): %s", fromJson(resp.getData(), User.class));
            Debug.debug("user authenticated (clone): %s", fromJson(resp.getData(), User.class).cloneIt());
        }
        authed = resp.isSuccessful();
        return (resp.isSuccessful() ? fromJson(resp.getData(), User.class).cloneIt() : null);
    }

    @Override
    public void deauthenticateUser(String username) throws IOException {
        authed = false;
        Command resp = sendCommandAndAwaitResponse(Command.CommandType.DEAUTHENTICATE, null, credential);
        
        if (!resp.isSuccessful()) {
            Debug.debug("Unsuccessful deauthentication: %s", resp.getMessage());
            //maybe throw an exception so the message can be displayed to the user?
        } else {
            Debug.debug("Successful deauthentication");
            //maybe throw an exception so the message can be displayed to the user?
        }
    }

    @Override
    public boolean isUserAuthenticated(String username) throws IOException {
        return (authed);
    }

    @Override
    public void saveUserCredential(Credential c) throws IOException {
        sendCommandAndAwaitResponse(Command.CommandType.SAVE_CREDENTIAL, toJson(c), credential);
    }

    public boolean userExists(String username) throws IOException {
        // user manager should already be loaded with users
        return UserManager.userExists(username);
    }

    public User saveUser(User u) throws IOException {
        Command resp = sendCommandAndAwaitResponse(Command.CommandType.SAVE_USER, toJson(u), credential);
        if (resp.isSuccessful()) {
            Debug.debug("Sucessfully saved user");
            return (fromJson(resp.getData(), User.class).cloneIt());
        } else {
            Debug.debug("Failed to save user: %s", resp.getMessage());
            return (null);
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

    @Override
    public void deleteUser(String username) throws IOException {
        User u = UserManager.getUser(username);
        if (u != null) {
            deleteUser(u);
        }
    }

    @Override
    public WaterReport saveWaterReport(WaterReport wr) throws IOException {
        Command resp = sendCommandAndAwaitResponse(Command.CommandType.SAVE_WATER_REPORT, toJson(wr), credential);
        if (resp.isSuccessful()) {
            return (fromJson(resp.getData(), WaterReport.class).cloneIt());
        } else {
            Debug.debug("Failed to save water report: %s", resp.getMessage());
            return (null);
        }
    }

    @Override
    public void deleteWaterReport(WaterReport wr) throws IOException {
        Command resp = sendCommandAndAwaitResponse(Command.CommandType.DELETE_WATER_REPORT, toJson(wr), credential);
        if (!resp.isSuccessful()) {
            Debug.debug("Unsuccessful water report delete: %s", resp.getMessage());
            //maybe throw an exception so the message can be displayed to the user?
        } else {
            Debug.debug("Sucessfully deleted water report");
        }
    }

    @Override
    public QualityReport saveQualityReport(QualityReport qr) throws IOException {
        Command resp = sendCommandAndAwaitResponse(Command.CommandType.SAVE_QUALITY_REPORT, toJson(qr), credential);
        if (resp.isSuccessful()) {
            return (fromJson(resp.getData(), QualityReport.class).cloneIt());
        } else {
            Debug.debug("Failed to save user: %s", resp.getMessage());
            return (null);
        }
    }

    @Override
    public void deleteQualityReport(QualityReport qr) throws IOException {
        sendCommandAndAwaitResponse(Command.CommandType.DELETE_QUALITY_REPORT, toJson(qr), credential);
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
        Debug.debug("adding quality report: %s", qr);
        WaterReport parent = ReportManager.filterWaterReportByNumber(qr.getParentReportNum());
        if (parent != null) {
            ReportManager.addQualityReport(parent, qr);
        }
    }
}
