import model.UserLevel;
import model.UserManager;
import model.Credential;
import model.CredentialManager;
import java.io.File;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import persistence.PersistenceInterface;
import persistence.json.PersistentJsonFile;

/**
 * Created by Kenny Scharm on 11/15/2016.
 */
public class SaveUserTester {

    private static final int TIMEOUT = 200;
    
    private static final String FILE_PATH = "src/test/resources/db/";
    
    private final File userTestFile = new File(FILE_PATH + PersistentJsonFile.USER_FILE_NAME);
    private final File credentialsTestFile = new File(FILE_PATH + PersistentJsonFile.CREDENTIAL_FILE_NAME);
    private final File reportsTestFile = new File(FILE_PATH + PersistentJsonFile.WR_FILE_NAME);
    
    private final PersistenceInterface persist = new PersistentJsonFile(FILE_PATH);
    private Credential credential;
    
    @Before
    public void setup() {
        userTestFile.delete();
        credentialsTestFile.delete();
        reportsTestFile.delete();
        persist.initialize();
        UserManager.initialize(persist); 
        credential = new Credential("Kenny", "Bae");
    }
    
    @Test(timeout = TIMEOUT)
    public void testNullCredentials() {
        assertEquals("User should not be saved", null, UserManager.saveUser(
            UserManager.createUser("Kenny", "Bae", "Kenny is Bae", "kscharm@hitmeup.com", UserLevel.WORKER), null));
    }

    @Test(timeout = TIMEOUT)
    public void testInvalidCredentials() {
        assertEquals("User should not be saved", null, UserManager.saveUser(
            UserManager.createUser("Kenny", "Bae", "Kenny is Bae", "kscharm@hitmeup.com", UserLevel.WORKER), new Credential("Rudy", "Swag")));
    }

    @Test(timeout = TIMEOUT)
    public void testValidCredentials() {
        assertEquals("User should be saved", UserManager.createUser("Kenny", "Bae", "Kenny is Bae", "kscharm@hitmeup.com", UserLevel.WORKER), UserManager.saveUser(
            UserManager.createUser("Kenny", "Bae", "Kenny is Bae", "kscharm@hitmeup.com", UserLevel.WORKER), credential));
    }
}