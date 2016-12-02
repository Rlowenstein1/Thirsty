package persistence.json.net;

import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import lib.Debug;
import model.Credential;
import model.QualityReport;
import model.ReportManager;
import model.User;
import model.UserManager;
import model.WaterReport;


/**
 */
public class PersistentJsonNetwork extends PersistentJsonNetworkInterface {

    public PersistentJsonNetwork() {
        super("host", 111);
    }

    public void terminate() {
        // net stuff
    }

    public boolean authenticateUser(Credential c) {
        // net stuff
        return false;
    }

    public void deauthenticateUser(String username) {
        // net stuff
    }

    public boolean isUserAuthenticated(String username) {
        // net stuff
        return false;
    }

    public void saveUserCredential(Credential c) {
        // net stuff
    }

    public boolean userExists(String username) {
        // net stuff?
        return false;
    }

    public void saveUser(User u) {
        // net stuff
    }

    public void deleteUser(User u) {
        // net stuff
    }

    public void deleteUser(String username) {
        // net stuff
    }

    public void saveWaterReport(WaterReport wr) {
        // net stuff
    }

    public void deleteWaterReport(WaterReport wr) {
        // net stuff
    }

    public void saveQualityReport(WaterReport wr) {
        // net suff
    }

    public void deleteQualityReport(WaterReport qr) {
        // net stuff
    }


}
