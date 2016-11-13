import model.UserLevel;
import model.UserManager;
import model.User;

import java.io.File;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import persistence.*;

import org.junit.Before;
import persistence.json.PersistentJsonFile;


/**
 * Created by rudy on 11/13/16.
 */
public class CreateUserTester {

	public final int TIMEOUT = 200;

	private final String FILE_PATH = "src/test/resources/db/";

	public File userTestFile = new File(FILE_PATH + PersistentJsonFile.USER_FILE_NAME);
	public File credentialsTestFile = new File(FILE_PATH + PersistentJsonFile.CREDENTIAL_FILE_NAME);
	public File reportsTestFile = new File(FILE_PATH + PersistentJsonFile.WR_FILE_NAME);

	public PersistenceInterface persist = new PersistentJsonFile(FILE_PATH);

	@Before
	public void setup() {
		userTestFile.delete();
		credentialsTestFile.delete();
		reportsTestFile.delete();
		persist.initialize();
		UserManager.initialize(persist);
		UserManager.createUser("Bob", "pw", "Bob Bob", "blah", UserLevel.WORKER);
	}

	@Test
	public void testCreateUserExists() {
		assertEquals("User already exists", UserManager.createUser("Bob", "pw", "Bob Bob", "blah", UserLevel.WORKER), null);
	}

}
