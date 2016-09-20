package model;

import java.util.HashSet;
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

    private Hashtable<String, User> userHashTable;

    /**
     * set of authenticated users to handle sessions.
     */
    private HashSet<model.User> authenticatedUsers;

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
        authenticatedUsers = new HashSet<>();
        userHashTable = new Hashtable<>();
    }

    /**
     * attempts to authenticate a user with a given username
     * and password.
     * @param user the User object of user attempting to log in
     * @param username of the user attempting to be authenticated
     * @param password of the user attempting to be authenticated
     * @return boolean value whether the attempt was successful
     */
    public static boolean authenticate(User user, String username,
                                       String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("arguments cannot be null");
        }
        if (authenticator.credentialTable.containsKey(username)
                && authenticator.credentialTable.get(username).
                equals(password.hashCode())
                && authenticator.userHashTable.get(user.getUsername()).
                equals(user)) {
            authenticator.authenticatedUsers.add(user);
            return true;
        }
        return false;
    }

    /**
     * Checks the authentication status of user
     * @param user User object to check status of user
     * @return boolean value, true if logged in
     */
    public static boolean isAuthenticated(User user) {
        return authenticator.authenticatedUsers.contains(user);
    }

    /**
     * Ends a user session
     * @param user User object of the user requesting to logout
     * @return boolean status of if the procedure was successful
     */
    public static boolean logout(User user) {
        return authenticator.authenticatedUsers.remove(user);
    }

    /**
     * registers a user's username and password and stores the credentials
     * a user session is created if successful
     * @param user User object to register
     * @param username of the new user
     * @param password of the new user
     * @return boolean representing if the user was saved
     *         if the username is already in use the user is not saved
     *         to prevent a user overriding another's password
     */
    public static boolean register(User user, String username,
                                   String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("arguments cannot be null");
        }
        if (!authenticator.credentialTable.contains(username)) {
            authenticator.credentialTable.put(username, password.hashCode());
            authenticator.userHashTable.put(user.getUsername(), user);
            authenticator.authenticatedUsers.add(user);
            return true;
        }
        return false;
    }
}
