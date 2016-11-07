package model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tybrown
 */
public class CredentialManager {
    private Map<String, Credential> credentials;

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
     * Loads an existing credential into the credentials list. Does not update the persistence layer
     * @param c The credential to load
     */
    public void addCredential(Credential c) {
        credentials.put(c.getUsername(), c);
    }

    /**
     * Creates a new user's credentials in the credentials list.
     * @param c The credentials of the new user
     * @return True if the user was added, False if the user already exists
     */
    public boolean createCredential(Credential c) {
        return (saveCredential(c, true));
    }

    /**
     * Updates credentials, only if user exists. Does not check if password is valid
     * @param c The new Credentials of the user
     * @return true if password was successfully updated
     */
    public boolean updateCredential(Credential c) {
        return (saveCredential(c, false));
    }

    /**
     * Saves credentials in the credentials list.
     * If create == true, and the user already exists, returns false
     * If create == false, and the user doesn't exist, returns false
     * Otherwise returns true
     * @param c The credential to save
     * @param create A modifier to determine the behavior when the user already exists
     * @return True if the credential was saved
     */
    public boolean saveCredential(Credential c, boolean create) {
        if (c == null) {
            throw new IllegalArgumentException("Credential cannot be null!");
        }
        String username = c.getUsername();
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null!");
        }

        if (create == userExists(username)) { //boolean logic combination of updateCredential and addCredential -- should actually be !(create XOR userExists), but == is good enough for 2 booleans
            return (false);
        }
        addCredential(c);
        return (true);
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
