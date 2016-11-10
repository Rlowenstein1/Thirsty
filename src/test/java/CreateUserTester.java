package test.java;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import junit.framework.Assert;
import model.UserLevel;
import model.Credential;
import persistence.PersistenceInterface;
import model.UserManager;
import model.Authenticator;
import model.User;
import model.UserLevel;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.TIMEOUT;


/**
 * Author: Rudy Lowenstein
 */
public class CreateUserTester {
	public static final int TIMEOUT = 200;

	private UserManager um;
	private PersistenceInterface persistTest = null;

	@Before
	public void setup() {
		um = new UserManager();
		um.initialize(persistTest);

	}


	@Test(timeout = TIMEOUT)
	public void testCreateUser() {
		User u = new User("UN", "Bob", "blah@blah.net", UserLevel.WORKER);
		Credential c = new Credential(u.getUsername(), "PW");
		/*um.saveCredential(c);
		um.persistTest.saveUser(u);
		um.userNameMap.put(u.getUsername(), u);
		*/
		assertTrue(um.userExists(u.getUsername()));

	}

	@Test(timeout = TIMEOUT)
	public void testCreateUserExists() {
		User u = new User("UN", "Bob", "blah@blah.net", UserLevel.WORKER);
		assertTrue(um.userExists("UN"));
	}

	@Test(timeout = TIMEOUT)
	public void testCreateUserInvalidPassword() {
		User u = new User("UN", "Bob", "blah@blah.net", UserLevel.WORKER);
		assertFalse(Authenticator.isValidPassword("PW"));
	}
	/*
	@Test(timeout = TIMEOUT)
	public void testCreateUserCannotAuthenticate() {
		User u = new User("UN", "Bob", "blah@blah.net", UserLevel.WORKER);
		Credential c = new Credential(u.getUsername(), "PW");
		assertFalse(um.persistTest.authenticateUser(c));
	}*/







}
