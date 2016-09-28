package model;

import java.util.HashMap;

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
        if (manager.usernameMap.containsKey(username)) {
            return null;
        }
        User newUser = new User(username, fullname, emailAddress, userLevel);
        if (Authenticator.register(username, password)
                && Authenticator.authenticate(newUser, username, password)) {
            manager.usernameMap.put(username, newUser);
            return newUser;
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
            if (Authenticator.authenticate(user, username, password)) {
                return user;
            }
        }
        return null;
    }
}
