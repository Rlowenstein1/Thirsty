package main.java.model;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Manager for the user objects of app
 */
public class UserManager {
    private static HashMap<String, User> usernameMap = new HashMap<>();

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
        if (usernameMap.containsKey(username)) {
            return null;
        }
        User newUser = new User(username, fullname, emailAddress, userLevel);
        if (Authenticator.register(username, password)
                && Authenticator.authenticate(newUser, username, password)) {
            usernameMap.put(username, newUser);
            return newUser;
        }
        return null;
    }

    /**
     * Adds existing user to list
     * @param user to be added
     */
    public static void addUser(User user) {
        usernameMap.put(user.getUsername(), user);
    }

    /**
     * Attempts to log in a user
     * @param username of prospective user
     * @param password of prospective user
     * @return user object if successful or null if not
     */
    public static User login(String username, String password) {
        User user = usernameMap.get(username);
        if (user != null) {
            if (Authenticator.authenticate(user, username, password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Updates login credentials, only if user exists and password is valid
     * @param username Username of user to update password 
     * @param password New password
     * @return true if password is valid and user exists
     */
    public static boolean updateLogin(String username, String password) {
        return (Authenticator.updateCredential(username, password));
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
}
