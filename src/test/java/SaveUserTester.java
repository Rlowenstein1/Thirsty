import model.UserLevel;
import model.UserManager;
import model.User;
import model.Credential;
import java.io.File;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

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
    private CredentialManager manager;
    
    @Before
    public void setup() {
        userTestFile.delete();
        credentialsTestFile.delete();
        reportsTestFile.delete();
        persist.initialize();
        UserManager.initialize(persist); 
        credential = new Credential("Kenny", "Bae");
        manager = new CredentialManager();
    }
    
    @Test(timeout = TIMEOUT)
    public void testNullCredential() {
    	User kenny = UserManager.createUser("Kenny", "Bae", "Kenny is Bae", "kscharm@hitmeup.com", UserLevel.WORKER);
        assertEquals("User should not be saved", null, UserManager.saveUser(kenny, null));
    }

    @Test(timeout = TIMEOUT)
    public void testNullUser() {
    	User kenny = UserManager.createUser("Kenny", "Bae", "Kenny is Bae", "kscharm@hitmeup.com", UserLevel.WORKER);
        assertEquals("User should not be saved", null, UserManager.saveUser(null, credential));
    }

    @Test(timeout = TIMEOUT)
    public void testValidCredentials() {
    	User kenny = UserManager.createUser("Kenny", "Bae", "Kenny is Bae", "kscharm@hitmeup.com", UserLevel.WORKER);
        assertEquals("User should be saved", kenny, UserManager.saveUser(kenny, credential));
    }

    @Test(timeout = TIMEOUT)
    public void testUpdatePassword() {
    	User kenny = UserManager.createUser("Kenny", "Bae", "Kenny is Bae", "kscharm@hitmeup.com", UserLevel.WORKER);
    	UserManager.saveUser(kenny, credential);
    	manager.saveCredential(credential);
    	Credential c = new Credential("Kenny", "Swag");
    	UserManager.saveUser(kenny, c);
    	manager.saveCredential(c);
        assertTrue("User should still be authenticated", persist.isUserAuthenticated(kenny.getUsername()));
        assertTrue("User password should be updated", manager.matchCredential(c));

    }

}
