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

    @Test(timeout = TIMEOUT)
    public void testUserCredentialMatchesSingle1() {
        Credential c = new Credential("testUsername", "testPassword");
        cm.saveCredential(c);
        assertTrue(cm.matchCredential(new Credential("testUsername", "testPassword")));
    }
   
    @Test(timeout = TIMEOUT)
    public void testUserNotCredentialMatchesSingle1() {
        Credential c = new Credential("testUsername", "testPassword");
        cm.saveCredential(c);
        assertFalse(cm.matchCredential(new Credential("NOTREAL", "testPassword")));
    }
   
    @Test(timeout = TIMEOUT)
    public void testUserNotCredentialMatchesSingle2() {
        Credential c = new Credential("testUsername", "testPassword");
        cm.saveCredential(c);
        assertFalse(cm.matchCredential(new Credential("testUsername", "bad password")));
    }

    @Test(timeout = TIMEOUT)
    public void testUserCredentialMatchesMultiple1() {
        Credential[] cs = {
            new Credential("testUsername", "testPassword"),
            new Credential("testUsername2", "testPassword2"),
            new Credential("123G*&euthtboo", "th3*g*983@3!"),
            new Credential("TestMe!", "Password lol"),
        };
        for (Credential c : cs) {
            cm.saveCredential(c);
        }
        assertTrue(cm.matchCredential(new Credential("123G*&euthtboo", "th3*g*983@3!")));
    }
   
    @Test(timeout = TIMEOUT)
    public void testUserCredentialMatchesMultiple2() {
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
            assertTrue(String.format("Credential: %s did not exist when it should!", c), cm.matchCredential(new Credential(c.getUsername(), c.getCredential())));
        }
    }
   
    @Test(timeout = TIMEOUT)
    public void testUserNotCredentialMatchesMultiple1() {
        Credential[] cs = {
            new Credential("testUsername", "testPassword"),
            new Credential("testUsername2", "testPassword2"),
            new Credential("123G*&euthtboo", "th3*g*983@3!"),
            new Credential("TestMe!", "Password lol"),
        };
        for (Credential c : cs) {
            cm.saveCredential(c);
        }
        assertFalse(cm.matchCredential(new Credential("not real!", "Password lol")));
    }
   
    @Test(timeout = TIMEOUT)
    public void testUserNotCredentialMatchesMultiple2() {
        Credential[] cs = {
            new Credential("testUsername", "testPassword"),
            new Credential("testUsername2", "testPassword2"),
            new Credential("123G*&euthtboo", "th3*g*983@3!"),
            new Credential("TestMe!", "Password lol"),
        };
        for (Credential c : cs) {
            cm.saveCredential(c);
        }
        assertFalse(cm.matchCredential(new Credential("TestMe!", "testPassword")));
    }

    @Test(timeout = TIMEOUT)
    public void testUserDeletedSingle1() {
        Credential c = new Credential("testUsername", "testPassword");
        cm.saveCredential(c);
        cm.deleteCredential(c.getUsername());
        assertFalse(cm.userExists(c.getUsername()));
        assertFalse(cm.matchCredential(c));
    }

    @Test(timeout = TIMEOUT)
    public void testUserDeletedMultiple1() {
        Credential[] cs = {
            new Credential("testUsername", "testPassword"),
            new Credential("testUsername2", "testPassword2"),
            new Credential("123G*&euthtboo", "th3*g*983@3!"),
            new Credential("TestMe!", "Password lol"),
        };
        for (Credential c : cs) {
            cm.saveCredential(c);
        }
        cm.deleteCredential("testUsername");
        cm.deleteCredential("testUsername2");
        cm.deleteCredential("TestMe!");
        assertTrue(cm.matchCredential(new Credential("123G*&euthtboo", "th3*g*983@3!")));
    }
   
    @Test(timeout = TIMEOUT)
    public void testUserDeletedMultiple2() {
        Credential[] cs = {
            new Credential("testUsername", "testPassword"),
            new Credential("testUsername2", "testPassword2"),
            new Credential("123G*&euthtboo", "th3*g*983@3!"),
            new Credential("TestMe!", "Password lol"),
        };
        for (Credential c : cs) {
            cm.saveCredential(c);
        }
        cm.deleteCredential("123G*&euthtboo");
        assertFalse(cm.matchCredential(new Credential("123G*&euthtboo", "th3*g*983@3!")));
    }
 
}
