import model.Credential;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

/**
 * A JUnit test class for testing the Credential class
 * @author Dominic Pattison
 */
public class CredentialTester {
    public static final int TIMEOUT = 200;

    @Test(timeout = TIMEOUT)
    public void equalsMethodTest() {
        Credential c = new Credential("username", "password");
        Credential cEqual = new Credential("username", "password");
        Credential cPNotEqual = new Credential("username", "password1");
        Credential cUNotEqual = new Credential("username1", "password");
        Credential cNeitherEqual = new Credential("username1", "password1");

        assertFalse(c.equals(null));
        assertFalse(c.equals("Not a credential object"));
        assertFalse(c.equals(1));
        assertFalse(c.equals(cPNotEqual));
        assertFalse(c.equals(cUNotEqual));
        assertFalse(c.equals(cNeitherEqual));
        assertTrue(c.equals(cEqual));
        assertTrue(c.equals(c));
    }

    @Test(timeout = TIMEOUT)
    public void hashCodeMethodTest() {
        Credential c = new Credential("username", "password");
        Credential cEqual = new Credential("username", "password");
        Credential cPNotEqual = new Credential("username", "password1");
        Credential cUNotEqual = new Credential("username1", "password");
        Credential cNeitherEqual = new Credential("username1", "password1");

        assertNotEquals(c.hashCode(), cPNotEqual.hashCode());
        assertNotEquals(c.hashCode(), cUNotEqual.hashCode());
        assertNotEquals(c.hashCode(), cNeitherEqual.hashCode());
        assertEquals(c.hashCode(), cEqual.hashCode());
        assertEquals(c.hashCode(), c.hashCode());
    }

    @Test(timeout = TIMEOUT)
    public void hashPasswordMethodTest() {
        assertEquals(Credential.hashPassword("password"), Credential.hashPassword("password"));
        assertNotEquals(Credential.hashPassword("password"), Credential.hashPassword("password1"));
    }
}
