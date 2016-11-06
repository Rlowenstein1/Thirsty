package model;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

/**
 * Authenticator class is used to authenticate and register users.
 */
public final class Authenticator {
    /**
     * hash table with the users username and password hashes.
     */
    private static Map<String, Integer> credentials = new HashMap<>();

    /**
     * set of authenticated users to handle sessions.
     */
    private static Set<model.User> authenticatedUsers = new HashSet<>();

    /**
     * Checks if password is valid
     * @param password to be validated
     * @return boolean represeting the validity
     */
    public static boolean isValidPassword(String password) {
        //do any password length/complexity checks here
        if (password == null || password.length() < 1) { 
            return (false);
        }
        return (true);
    }

    /**
     * Updates credentials, only if user exists and password is valid
     * @param username Username of user to update password 
     * @param password New password
     * @return true if password is valid and user exists
     */
    public static boolean updateCredential(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("arguments cannot be null");
        }
        if (isValidPassword(password) && userExists(username)) {
            credentials.put(username, password.hashCode());
            return (true);
        }
        return (false);
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
        Integer credential = credentials.getOrDefault(username, null);
        if (credential != null && password.hashCode() == credential) {
            authenticatedUsers.add(user);
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
        return authenticatedUsers.contains(user);
    }

    /**
     * Ends a user session
     * @param user User object of the user requesting to logout
     * @return boolean status of if the procedure was successful
     */
    public boolean logout(User user) {
        return authenticatedUsers.remove(user);
    }

    /**
     * 
     * @param username username to check existence of 
     * @return boolean, true if user exists, false otherwise
     */
    public static boolean userExists(String username) {
        return (credentials.containsKey(username));
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
        if (!userExists(username)) {
            
            credentials.put(username, password.hashCode());
            return true;
        }
        return false;
    }
}
