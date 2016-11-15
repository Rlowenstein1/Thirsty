import model.CredentialManager;
import model.UserLevel;
import model.UserManager;
import model.AuthenticationManager;
import java.io.File;

import org.junit.Test;
import persistence.*;

import org.junit.Before;
import persistence.json.PersistentJsonFile;
import org.omg.CORBA.TIMEOUT;

import static org.junit.Assert.*;


/**
 * Created by Jeremy Peterson
 */
public class AuthenticationManagerTester {
    public final int TIMEOUT = 200;

    private final String FILE_PATH = "src/test/resources/db/";

    public File userTestFile = new File(FILE_PATH + PersistentJsonFile.USER_FILE_NAME);
    public File credentialsTestFile = new File(FILE_PATH + PersistentJsonFile.CREDENTIAL_FILE_NAME);
    public File reportsTestFile = new File(FILE_PATH + PersistentJsonFile.WR_FILE_NAME);

    public PersistenceInterface persist = new PersistentJsonFile();
    CredentialManager credentialManager = new CredentialManager();
    AuthenticationManager authenticationManager = new AuthenticationManager(credentialManager);

    @Before
    public void setup() {
        userTestFile.delete();
        credentialsTestFile.delete();
        reportsTestFile.delete();
        persist.initialize();
        UserManager.initialize(persist);
        UserManager.createUser("Bob", "pw", "Bob Bob", "blah", UserLevel.WORKER);

    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullCredential() {
        authenticationManager.authenticate(null);
    }
}
