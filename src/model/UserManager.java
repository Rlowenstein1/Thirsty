package model;

import java.util.HashMap;
import lib.Debug;

/**
 * Manager for the user objects of app
 */
public class UserManager {
    /**
     * singleton instance
     */
    private static UserManager manager = new UserManager();

    private HashMap<String, User> usernameMap;

    /**
     *
     */
    private UserManager() {
        usernameMap = new HashMap<>();
    }

    /**
     * registers a new user
     * @param username of new user
     * @param password of new user
     * @param fullname of new user
     * @param emailAddress of new user
     * @param userLevel of new user
     * @return the new user object after registration
     *         returns null if the attempt was unsuccessful (already used username)
     */
    public static User register(String username, String password, String fullname,
                                String emailAddress, UserLevel userLevel) {
        Debug.debug("registering new user:\n  username: %s\n  password: %s\n  fullname: %s\n  emailAddress: %s\n  userLevel: %s\n", username, password, fullname, emailAddress, userLevel);
        if (manager.usernameMap.containsKey(username)) {
            Debug.debug("User already exists!");
            return null;
        }
        User newUser = new User(username, fullname, emailAddress, userLevel);
        if (Authenticator.register(username, password)
                && Authenticator.authenticate(newUser, username, password)) {
            Debug.debug("Successfully registered!");
            manager.usernameMap.put(username, newUser);
            return newUser;
        } else {
            Debug.debug("Failed to register user!");
        }
        return null;
    }

    /**
     * Attempts to log in a user
     * @param username of prospective user
     * @param password of prospective user
     * @return user object if successful or null if not
     */
    public static User login(String username, String password) {
        User user = manager.usernameMap.get(username);
        if (user != null) {
            Debug.debug("User exists in usernameMap");
            if (Authenticator.authenticate(user, username, password)) {
                Debug.debug("User successfully authenticated!");
                return user;
            } else {
                Debug.debug("User failed to authenticate ");
            }
        } else {
            Debug.debug("User does not exist in usernameMap");
        }
        return null;
    }
}
