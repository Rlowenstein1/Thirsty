import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import junit.framework.Assert;
import model.UserLevel;
import model.UserManager;
import model.Authenticator;
import model.User;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.TIMEOUT;

public class ThirstyTester {
    public static final int TIMEOUT = 200;
    @Before
    public void setup() {
        Authenticator.register("user", "123");
        UserManager.register("asdf", "qwerty", "A Silently Deadly Ferret", "a@b.c", UserLevel.ADMINISTRATOR);
    }
    @Test(timeout = TIMEOUT)
    public void testAuthenticatorRegisterSuccess() {
        assertTrue(Authenticator.register("newUser", "123"));
    }

    @Test(timeout = TIMEOUT)
    public void testAuthenticatorRegisterFailure() {
        assertFalse(Authenticator.register("user", "1234"));
        assertTrue(Authenticator.register("new", "1"));
        assertFalse(Authenticator.register("new", "2"));
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testAuthenticatorRegisterUserIllegalArg() {
        Authenticator.register(null, "1");
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testAuthenticatorRegisterPasswordIllegalArg() {
        Authenticator.register("newUser2", null);
    }

    @Test(timeout = TIMEOUT)
    public void testUserLoginTrue() {
        UserManager.register("newUser3", "newPass", "Name", "Email", UserLevel.USER);
        User test = UserManager.login("newUser3", "newPass");
        assertTrue(test != null);
    }
    /* Possibly Irrelevant
    @Test(timeout = TIMEOUT)
    public void testUserLoginTrue2() {
        Authenticator.register("newUser4", "newPass2");
        User test = UserManager.login("newUser4", "newPass2");
        assertFalse(test == null);
    }
    */
    @Test(timeout = TIMEOUT)
    public void testUserLoginFalse() {
        UserManager.register("newUser5", "newPass3", "Name", "Email", UserLevel.USER);
        User test = UserManager.login("newUser5", "newPass300000");
        assertTrue(test == null);
    }
}