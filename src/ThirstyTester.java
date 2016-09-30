import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import model.UserLevel;
import model.UserManager;
import model. Authenticator;
import org.junit.Before;
import org.junit.Test;

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
        Authenticator.register("new", "1");
        assertFalse(Authenticator.register("new", "2"));
    }
}