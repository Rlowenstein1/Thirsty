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
        // net stuff
    }

    public boolean isUserAuthenticated(String username) throws IOException {
        // net stuff
        return false;
    }

    public void saveUserCredential(Credential c) throws IOException {
        // net stuff
    }

    public boolean userExists(String username) throws IOException {
        // net stuff?
        return false;
    }

    public void saveUser(User u) throws IOException {
        // net stuff
    }

    public void deleteUser(User u) throws IOException {
        // net stuff
    }

    public void deleteUser(String username) throws IOException {
        // net stuff
    }

    public void saveWaterReport(WaterReport wr) throws IOException {
        // net stuff
    }

    public void deleteWaterReport(WaterReport wr) throws IOException {
        // net stuff
    }

    public void saveQualityReport(WaterReport wr) throws IOException {
        // net suff
    }

    public void deleteQualityReport(WaterReport qr) throws IOException {
        // net stuff
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
