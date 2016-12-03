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

    public PersistentJsonNetwork() {
        super("host", 111);
    }

    public void terminate() throws IOException {
        disconnect();
    }

    public boolean authenticateUser(Credential c) throws IOException {
        credential = c;
        Command command = new Command();
        command.setCredential(c);
        command.setCommandType(Command.CommandType.AUTHENTICATE);
        //sendText(toJson(command, Command.class));   TODO not recognized for some reason
        String test = receiveMessage();
        return false;
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
}
