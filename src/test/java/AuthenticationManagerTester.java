import java.io.File;
import model.AuthenticationManager;
import model.Credential;
import model.CredentialManager;
import model.UserLevel;
import model.UserManager;

import org.junit.Test;

import org.junit.Before;
import persistence.json.PersistentJsonFile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import persistence.PersistenceInterface;


/**
 * Created by Jeremy Peterson
 */
public class AuthenticationManagerTester {
    public static final int TIMEOUT = 200;

    private static final String FILE_PATH = "src/test/resources/db/";

    private File userTestFile = new File(FILE_PATH + PersistentJsonFile.USER_FILE_NAME);
    private File credentialsTestFile = new File(FILE_PATH + PersistentJsonFile.CREDENTIAL_FILE_NAME);
    private File reportsTestFile = new File(FILE_PATH + PersistentJsonFile.WR_FILE_NAME);

    private PersistenceInterface persist = new PersistentJsonFile(FILE_PATH);
    private CredentialManager credentialManager = new CredentialManager();
    private AuthenticationManager authenticationManager = new AuthenticationManager(credentialManager);

    @Before
    public void setup() {
        userTestFile.delete();
        credentialsTestFile.delete();
        reportsTestFile.delete();
        persist.initialize();
        UserManager.initialize(persist);
        UserManager.createUser("Bob", "pw", "Bob Bob", "blah", UserLevel.WORKER);

        Credential c = new Credential("user1", "pass1");
        credentialManager.saveCredential(c);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullCredential() {
        authenticationManager.authenticate(null);
    }
    @Test(timeout = TIMEOUT)
    public void testfalseCredential() {
        Credential c = new Credential("user", "pass");
        assertFalse(authenticationManager.authenticate((c)));
    }
    @Test(timeout = TIMEOUT)
    public void testauthenticated() {
        Credential c = new Credential("user1", "pass1");
        credentialManager.saveCredential(c);
        assertTrue(authenticationManager.authenticate(c));
    }
    @Test(timeout = TIMEOUT)
    public void isAuthenticatedTesterFalse() {
        assertFalse(authenticationManager.isAuthenticated("user2"));
    }
    @Test(timeout = TIMEOUT)
    public void isAuthenticatedTestTrue() {
        Credential c = new Credential("user3", "pass3");
        credentialManager.saveCredential(c);
        assertTrue(authenticationManager.authenticate(c));
        assertTrue(authenticationManager.isAuthenticated("user3"));
    }
    @Test(timeout = TIMEOUT)
    public void logout() {
        Credential c = new Credential("user4", "pass4");
        credentialManager.saveCredential(c);
        assertTrue(authenticationManager.authenticate(c));
        assertTrue(authenticationManager.isAuthenticated("user4"));
        authenticationManager.logout("user4");
        assertFalse(authenticationManager.isAuthenticated("user4"));
    }
}
