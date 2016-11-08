package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages authentication and authenticated users
 */
public class Authenticator {
    private Set<String> authenticatedUsers;
    private CredentialManager credentialManager;

    /**
     * Constructor for Authenticator
     * @param cm a credential manager for this authenticator
     */
    public Authenticator(CredentialManager cm) {
        this.authenticatedUsers = new HashSet<>();
        this.credentialManager = cm;
    }

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
     * Attempts to authenticate a user with a given Credential.
     * If the authentication was successful, the user will be added to the authenticated users list (currently logged on users list)
     * @param c The Credentials of the user to test
     * @return Returns true if the user existed and the credential matched, or if the user was already authenticated
     */
    public boolean authenticate(Credential c) {
        if (c == null) {
            throw new IllegalArgumentException("Credential cannot be null!");
        }
        String username = c.getUsername();
        if (isAuthenticated(username)) {
            return (true);
        }
        if (!credentialManager.matchCredential(c)) {
            return (false);
        }
        authenticatedUsers.add(username);
        return (true);
        /*
        String username = c.getUsername();
        Integer credential = credentials.get(username);
        Integer testCredential = c.getCredential();
        if (credential == null || testCredential == null || !userExists(username) || !credential.equals(testCredential)) {
            return (false);
        }
        if (!persist.authenticateUser(c)) {
            return (false);
        }
        authenticatedUsers.add(username);
        return (true);
        */
    }

    /**
     * Checks the if the given username is in the authenticated users list
     * @param username Username of user to check
     * @return True if the user is on the authenticated users list
     */
    public boolean isAuthenticated(String username) {
        return (authenticatedUsers.contains(username));
    }

    /**
     * Ends a user session by removing the user from the authenticated users list
     * @param username The username of the user to logout
     * @return boolean True if the user was logged in
     */
    public boolean logout(String username) {
        return (authenticatedUsers.remove(username));
    }
}
