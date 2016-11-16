import model.*;

import java.io.File;

import org.junit.Test;
import persistence.*;

import org.junit.Before;
import persistence.json.PersistentJsonFile;
import org.omg.CORBA.TIMEOUT;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


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
    @Test(timeout = TIMEOUT)
    public void testfalseCredential() {
        Credential c = new Credential("user", "pass");
        assertFalse(authenticationManager.authenticate((c)));
    }
    @Test(timeout = TIMEOUT)
    public void testauthenticated() {
        Credential c = new Credential("user1", "pass1");
        boolean ignore = authenticationManager.authenticate(c);
        assertTrue(authenticationManager.authenticate((c)));
    }
    @Test(timeout = TIMEOUT)
    public void isAuthenticatedTesterFalse() {
        assertFalse(authenticationManager.isAuthenticated("user2"));
    }
    @Test(timeout = TIMEOUT)
    public void isAuthenticatedTestTrue() {
        Credential c = new Credential("user3", "pass3");
        boolean ignore = authenticationManager.authenticate(c);
        assertTrue(authenticationManager.isAuthenticated("user3"));
    }
    @Test(timeout = TIMEOUT)
    public void logout() {
        Credential c = new Credential("user4", "pass4");
        authenticationManager.authenticate(c);
        assertTrue(authenticationManager.isAuthenticated("user4"));
        authenticationManager.logout("user4");
        assertFalse(authenticationManager.isAuthenticated("user4"));
    }
}
