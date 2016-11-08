package model;

import com.google.gson.annotations.Expose;
import java.util.Objects;

/**
 *
 * @author tybrown
 */
public class Credential {
    @Expose
    private Integer credential;
    @Expose
    private String username;

    /**
     * Returns the credential
     * @return the credential
     */
    public Integer getCredential() {
        return (credential);
    }

    /**
     * Returns the username
     * @return the username
     */
    public String getUsername() {
        return (username);
    }

    /**
     * Returns the a one-way-hash of the password
     * @param password The password to hash
     * @return The hash of the password
     */
    public static Integer hashPassword(String password) {
        return (password.hashCode());
    }

    /**
     * Constructs a credential
     * @param username The username of the user whose password this is
     * @param password The password of the user
     */
    public Credential(String username, String password) {
        this(username, hashPassword(password));
    }

    /**
     * Constructs a credential. Normally should not be called (designed for loading an existing credential from disk/network)
     * @param username The username of the user whose password this is
     * @param credential The credential hash code of the password 
     */
    public Credential(String username, Integer credential) {
        this.credential = credential;
        this.username = username;
    }

    @Override
    public String toString() {
        return (String.format("Credential: [username: \"%s\"; credential: %s]", username, credential.toString()));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Credential)) {
            return (false);
        }
        Credential c = (Credential) obj;
        String usernameT = c.getUsername();
        Integer credentialT = c.getCredential();
        return (usernameT != null && usernameT.equals(this.username)
                && credentialT != null && credentialT.equals(this.credential));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.credential);
        hash = 97 * hash + Objects.hashCode(this.username);
        return hash;
    }

}
