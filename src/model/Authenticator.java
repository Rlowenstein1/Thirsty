import java.util.Hashtable;

/**
 * Authenticator class is used to authenticate and register users.
 */
public final class Authenticator {
    /**
     * singleton instance.
     */
    private static Authenticator authenticator = null;

    /**
     * hash table with the users username and password hashes.
     */
    private Hashtable<String, Integer> credentialTable;

    /**
     * initializes the singleton instance.
     */
    public static void init() {
        if (authenticator == null) {
            authenticator = new Authenticator();
        }
    }

    /**
     * private constructor for class.
     */
    private Authenticator() {
        credentialTable = new Hashtable<>();
    }

    /**
     * attempts to authenticate a user with a given username
     * and password.
     * @param username of the user attempting to be authenticated
     * @param password of the user attempting to be authenticated
     * @return boolean value whether the attempt was successful
     */
    public static boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("arguments cannot be null");
        }
        return authenticator.credentialTable.containsKey(username)
                && authenticator.credentialTable.get(username).equals(password.hashCode());
    }

    /**
     * registers a user's username and password and stores the credentials
     * @param username of the new user
     * @param password of the new user
     * @return boolean representing if the user was saved
     *         if the username is already in use the user is not saved
     *         to prevent a user overriding another's password
     */
    public static boolean register(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("arguments cannot be null");
        }
        if (!authenticator.credentialTable.contains(username)) {
            authenticator.credentialTable.put(username, password.hashCode());
            return true;
        }
        return false;
    }
}
