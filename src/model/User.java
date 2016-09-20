package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A class representing a User in the water source application.
 *
 */
public class User {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty emailAddress = new SimpleStringProperty();
    private final ObjectProperty<UserLevel> level = new SimpleObjectProperty<>();

    /**
     * No param constructor -- DO NOT CALL NORMALLY.
     * This constructor only for GUI use
     */
    public User() {
        this("Username");
        /*
        name.set("Name");
        username.set("Username");
        emailAddress.set("sample.gmail.com");
        */
    }

    /**
     * 1-param constructor for creating a new User object
     * with sample information and an authority level of USER
     *
     * @param username the username of the User when they first register
     */
    public User(String username) {
        this(username, "Name", "sample@example.com", UserLevel.USER);
    }

    /**
     * Constructor used to create a new User object
     *
     * @param username the username of the User
     * @param name the name of the User
     * @param emailAddress the User's email address
     * @param level the authority level of the User
     */
    public User(String username, String name, String emailAddress, UserLevel level) {
        this.username.set(username);
        this.name.set(name);
        this.emailAddress.set(emailAddress);
        this.level.set(level);
    }

    /**
     * Gets the name of the User
     *
     * @return the name of the User
     */
    public String getName() {
        return name.get();
    }

    /**
     * Sets the name of the User
     *
     * @param n the new name
     */
    public void setName(String n) {
        name.set(n);
    }

    /**
     * Gets the User's username
     *
     * @return the username
     */
    public String getUsername() {
        return username.get();
    }

    /**
     * Sets the username of the User
     *
     * @param n the new username
     */
    public void setUsername(String n) {
        username.set(n);
    }

    /**
     * Gets the email address of the User
     *
     * @return the email address
     */
    public String getEmailAddress() {
        return emailAddress.get();
    }

    /**
     * Sets the User's email address to a new one
     *
     * @param n the new email address
     */
    public void setEmailAddress(String n) {
        emailAddress.set(n);
    }

    /**
     * Gets the authority level of the User
     *
     * @return the User's level
     */
    public UserLevel getUserLevel() {
        return level.get();
    }

    /**
     * Sets the User's authority level to a new one
     *
     * @param l the new level to be set
     */
    public void setUserLevel(UserLevel l) {
        level.set(l);
    }

    /**
     * Returns a String representation of a User object
     *
     * @return the String representation
     */
    public String toString() {
        return "Name: " + name.get() + "\n" +
                "Username: " + username.get() + "\n" +
                "Email address: " + emailAddress.get() + "\n" +
                "Authorization level: " + level.get();
    }
}