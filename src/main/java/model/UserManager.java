package model;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;

import persistence.PersistenceInterface;
import lib.Debug;

/**
 * Manager for the user objects of app
 */
public class UserManager {
    private static Map<String, User> usernameMap;
    private static PersistenceInterface persist = null;

    /**
     * Sets up the user manager. This method should only be called once
     * @param persist The PersistenceInterface to use to manage users
     */
    public static void initialize(PersistenceInterface persist) {
        usernameMap = new HashMap<>();
        UserManager.persist = persist;
    }

    /**
     * Saves the given credential in the persistence layer. If user already exists, updates password
     * @param c The credentials of the new user
     */
    private static void saveCredential(Credential c) {
        try {
            persist.saveUserCredential(c);
        } catch (IOException e) {
            Debug.debug("Error in saving credential");
        }
    }

    /**
     * Checks if password is valid
     * @param password to be validated
     * @return boolean representing the validity
     */
    public static boolean isPasswordInvalid(CharSequence password) {
        //do any password length/complexity checks here
        return ((password == null) || (password.length() < 1));
    }

    /**
     * Registers a new user. Adds the new user to the persistence layer
     * User will be logged in if this method returns non-null
     * If user already exists or password is invalid, returns null
     * @param username of new user
     * @param password of new user
     * @param fullname of new user
     * @param emailAddress of new user
     * @param userLevel of new user
     * @return the new user object after registration
     *         returns null if the attempt was unsuccessful
     *         (already used username, invalid password, authentication failed)
     */
    public static User createUser(String username, String password,
                                  String fullname, String emailAddress, UserLevel userLevel) {
        if (userExists(username) || isPasswordInvalid(password)) {
            return (null);
        }
        User u = new User(username, fullname, emailAddress, userLevel);
        Credential c = new Credential(username, password);
        return (saveUser(u, c));
    }
    
    /**
     * Updates an existing user by matching on the username of the passed user
     * @param user The new user object
     */
    public static void updateUser(User user) {
        if ((user != null) && userExists(user.getUsername())) {
            try {
                persist.saveUser(user);
            } catch (IOException e) {
                Debug.debug("Error in updating user");
            }
        }
    }

    /**
     * Saves a user and updates password. Adds if doesn't exist, updates if exists.
     * @param u The user to save
     * @param c The new Credential
     * @return Returns the passed in user, or null if:
     *      - any of the arguments was null
     *      - authenticating with the persistence layer failed
     */
    public static User saveUser(User u, Credential c) {
        if ((u == null) || (c == null)) {
            return null;
        }
        try {
            u = persist.saveUser(u);
            if (u == null) {
                return (null);
            }
        } catch (IOException e) {
            Debug.debug("Error in saving user");
        }
        saveCredential(c);
        try {
            u = persist.authenticateUser(c);
            if (u == null) {
                return null;
            }
        } catch (IOException e) {
            Debug.debug("Error in saving user credential");
            return null;
        }
        addUser(u);
        return u;
    }
    
    /**
     * Loads an existing user into the username map.
     * Does not add to the persistence layer. Overwrites user if user already exists
     * @param user to be added
     */
    public static void addUser(User user) {
        usernameMap.put(user.getUsername(), user);
    }

    /**
     * Gets a user by username
     * @param username The username of the User to get
     * @return The User object with the matching username, or null of no user matched this username
     */
    public static User getUser(String username) {
        return (usernameMap.get(username));
    }

    /**
     * Checks if a given username exists in the list of known usernames
     * @param username The username to check
     * @return True if the username exists, false if no such user exists
     */
    public static boolean userExists(String username) {
        return (usernameMap.containsKey(username));
    }

    /**
     * Removes a user from the list of known users
     * @param username The username of the user to find
     */
    public static void deleteUser(String username) {
        usernameMap.remove(username);
    }

    /**
     * Attempts to log in an existing user
     * @param c The Credentials of the user logging in
     * @return The User object if successful, or null otherwise
     */
    public static User login(Credential c) {
        if (c != null) {
            try {
                User u = persist.authenticateUser(c);
                if (u != null) {
                    addUser(u);
                    return (u);
                }
            } catch (IOException e) {
                Debug.debug("Error in logging in");
            }
            /*
            String username = c.getUsername();
            if (username != null) {
                try {
                    ;
                } catch (IOException e) {
                    Debug.debug("Error in logging in");
                }
                User u = getUser(username);
                try {
                    if ((u != null) && persist.authenticateUser(c)) {
                        return (u);
                    }
                } catch (IOException e) {
                    Debug.debug("Error in logging in");
                    return null;
                }
            }
            */
        }
        return (null);
    }

    /**
     * Logs user out through the persistence layer
     * @param username The username of the user to logout
     */
    public static void logout(String username) {
        User u = getUser(username);
        if (u != null) {
            try {
                persist.saveUser(u);
                persist.deauthenticateUser(username);
            } catch (IOException e) {
                Debug.debug("Error in logging out");
            }
        }
    }

    /**
     * Returns true if the given user is allowed to submit quality reports
     * @param u the user to check
     * @return True if the user is allowed to submit quality reports
     */
    public static boolean isUserQualityReportAuthorized(User u) {
        return (u.getUserLevel().compareTo(UserLevel.WORKER) >= 0);
    }

    /**
     * Gets the list of users registered
     * @return list of users
     */
    public static List<User> getUserList() {
        return new ArrayList<>(usernameMap.values());
    }

    /**
     * Returns true if the given user is allowed to view history reports
     * This is business logic - what each user is allowed to see should be
     * controlled in the controller(?) where we do other business logic - this
     * couples this class with pretty much everything...
     * @param u the user to check
     * @return True if the user is allowed to view history reports
     */
    public static boolean isUserHistoryReportAuthorized(User u) {
        return (u.getUserLevel().compareTo(UserLevel.MANAGER) >= 0);
    }
}
