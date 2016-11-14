import main.java.model.UserLevel;
import main.java.model.UserManager;
import java.io.File;

import org.junit.Test;
import main.java.persistence.PersistenceInterface;

import org.junit.Before;
import main.java.persistence.json.PersistentJsonFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by rudy tudy fresh and frudy on 11/13/16.
 */
public class CreateUserTester {

    private static final int TIMEOUT = 200;
    
    private static final String FILE_PATH = "src/test/resources/db/";
    
    private final File userTestFile = new File(FILE_PATH + PersistentJsonFile.USER_FILE_NAME);
    private final File credentialsTestFile = new File(FILE_PATH + PersistentJsonFile.CREDENTIAL_FILE_NAME);
    private final File reportsTestFile = new File(FILE_PATH + PersistentJsonFile.WR_FILE_NAME);
    
    private final PersistenceInterface persist = new PersistentJsonFile(FILE_PATH);
    
    @Before
    public void setup() {
        userTestFile.delete();
        credentialsTestFile.delete();
        reportsTestFile.delete();
        persist.initialize();
        UserManager.initialize(persist);
        UserManager.createUser("Bob", "pw", "Bob Bob", "blah", UserLevel.WORKER);
    }
    
    @Test(timeout = TIMEOUT)
    public void testCreateUserExists() {
        assertEquals("User should have existed", UserManager.createUser("Bob", "pw", "Bob Bob", "blah", UserLevel.WORKER), null);
    }
    @Test(timeout = TIMEOUT)
    public void testCreateUserDoesNotExist() {
        assertNotEquals("User already existed", UserManager.createUser("Sally", "pw", "Bob Sally", "blah", UserLevel.WORKER), null);
    }
    
    @Test(timeout = TIMEOUT)
    public void testValidPassword() {
        assertFalse("Password should be valid", UserManager.isPasswordInvalid("pw"));
    }
    
    @Test(timeout = TIMEOUT)
    public void testInvalidPassword() {
        assertTrue("Password should be invalid", UserManager.isPasswordInvalid(""));
    }
}
