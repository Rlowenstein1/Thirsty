import model.Credential;
import model.CredentialManager;
/*
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
*/
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tybrown
 */
public class CredentialManagerTester {
    public static final int TIMEOUT = 200;

    private CredentialManager cm;

    @Before
    public void setup() {
        cm = new CredentialManager();
    }

    @Test(timeout = TIMEOUT)
    public void testUserExistsSingle1() {
        Credential c = new Credential("testUsername", "testPassword");
        cm.saveCredential(c);
        assertTrue(cm.userExists("testUsername"));
    }
   
    @Test(timeout = TIMEOUT)
    public void testUserNotExistsSingle1() {
        Credential c = new Credential("testUsername", "testPassword");
        cm.saveCredential(c);
        assertFalse(cm.userExists("NOTREAL"));
    }

    @Test(timeout = TIMEOUT)
    public void testUserExistsMultiple1() {
        Credential[] cs = {
            new Credential("testUsername", "testPassword"),
            new Credential("testUsername2", "testPassword2"),
            new Credential("123G*&euthtboo", "th3*g*983@3!"),
            new Credential("TestMe!", "Password lol"),
        };
        for (Credential c : cs) {
            cm.saveCredential(c);
        }
        assertTrue(cm.userExists("TestMe!"));
    }
   
    @Test(timeout = TIMEOUT)
    public void testUserExistsMultiple2() {
        Credential[] cs = {
            new Credential("testUsername", "testPassword"),
            new Credential("testUsername2", "testPassword2"),
            new Credential("123G*&euthtboo", "th3*g*983@3!"),
            new Credential("TestMe!", "Password lol"),
        };
        for (Credential c : cs) {
            cm.saveCredential(c);
        }
        for (Credential c : cs) {
            assertTrue(String.format("User: %s did not exist when it should!", c.getUsername()), cm.userExists(c.getUsername()));
        }
    }
   
    @Test(timeout = TIMEOUT)
    public void testUserNotExistsMultiple1() {
        Credential[] cs = {
            new Credential("testUsername", "testPassword"),
            new Credential("testUsername2", "testPassword2"),
            new Credential("123G*&euthtboo", "th3*g*983@3!"),
            new Credential("TestMe!", "Password lol"),
        };
        for (Credential c : cs) {
            cm.saveCredential(c);
        }
        assertFalse(cm.userExists("not real!"));
    }
}
