package model;

import java.util.HashSet;
import java.util.HashMap;
import lib.Debug;

/**
 * Authenticator class is used to authenticate and register users.
 */
public final class Authenticator {
    /**
     * singleton instance.
     */
    public static Authenticator authenticator = new Authenticator();

    /**
     * hash table with the users username and password hashes.
     */
    private HashMap<String, Integer> credentials;

    /**
     * set of authenticated users to handle sessions.
     */
    private HashSet<model.User> authenticatedUsers;


    /**
     * private constructor for class.
     */
    private Authenticator() {
        credentials = new HashMap<>();
        authenticatedUsers = new HashSet<>();
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
        Integer credential = authenticator.credentials.get(username);
        Debug.debug("password = \"%s\"[%d] == credentials = \"%d\"", password, password.hashCode(), credential);
        if (password.hashCode() == credential) {
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
    public boolean isAuthenticated(User user) {
        return authenticator.authenticatedUsers.contains(user);
    }

    /**
     * Ends a user session
     * @param user User object of the user requesting to logout
     * @return boolean status of if the procedure was successful
     */
    public boolean logout(User user) {
        return authenticator.authenticatedUsers.remove(user);
    }

    /**
     * registers a user's username and password and stores the credentials
     * a user session is created if successful
     * @param username of the new user
     * @param password of the new user
     * @return boolean representing if the user was saved
     *         if the username is already in use the user is not saved
     *         to prevent a user overriding another's password
     */
    public static boolean register(String username,
                                   String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("arguments cannot be null");
        }
        Debug.debug("Registering a user: \"%s\" with password \"%s\"", username, password);
        if (!authenticator.credentials.containsKey(username)) {
            Debug.debug("Successfully registered!");
            authenticator.credentials.put(username, password.hashCode());
            return true;
        } else {
            Debug.debug("Failed to register user! User already registered!");
        }
        return false;
    }
}