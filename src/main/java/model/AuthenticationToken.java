package main.java.model;

/**
 * Authentication object for creating and storing user credentials for a session
 */
public class AuthenticationToken {
    private final User user;
    private final String username;
    private final String password;

    /**
     * Constructor - must specify all fields
     * @param user of authentication token
     * @param username of authentication token
     * @param password of authentication token
     */
    public AuthenticationToken(User user, String username, String password) {
        this.user = user;
        this.username = username;
        this.password = password;
    }

    /**
     * Getter for authentication token user
     * @return User object
     */
    public User getUser() {
        return user;
    }

    /**
     * Getter for authentication token username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for authentication token password
     * @return password
     */
    public String getPassword() {
        return password;
    }
}
