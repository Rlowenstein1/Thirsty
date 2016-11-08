package model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tybrown
 */
public class CredentialManager {
    private Map<String, Credential> credentials;

    /**
     * Default constructor
     */
    public CredentialManager() {
        credentials = new HashMap<>();
    }

    /**
     * Checks to see if a given username has ever been added to the credentials list
     * @param username username to check existence of 
     * @return boolean, true if user exists, false otherwise
     */
    public boolean userExists(String username) {
        return (credentials.containsKey(username));
    }

    /**
     * Saves credentials in the credentials list.
     * If the username already existed, updates the credential
     * @param c The credential to save
     */
    public void saveCredential(Credential c) {
        credentials.put(c.getUsername(), c);
    }

    /**
     * Removes the user's password from the credentials list
     * @param username The username of the user whose credentials will be removed
     */
    public void deleteCredential(String username) {
        credentials.remove(username);
    }

    /**
     * Checks to see if the given credential matches the known credential
     * @param c The credential to check
     * @return Returns true if the given credential matched the known credential
     */
    public boolean matchCredential(Credential c) {
        String username = c.getUsername();
        return (userExists(username) && credentials.get(username).equals(c));
    }

}
