package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

/**
 * A class representing a User in the water source application.
 *
 */
public class User {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty emailAddress = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final ObjectProperty<UserLevel> level = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> profilePicture = new SimpleObjectProperty<>();

    /**
     * No param constructor -- DO NOT CALL NORMALLY.
     * This constructor only for GUI use
     */
    public User() {
        this("Username");
    }

    /**
     * 1-param constructor for creating a new User object
     * with sample information and an authority level of USER
     *
     * @param username the username of the User when they first register
     */
    public User(String username) {
        this(username, "Name", "Sample Title",
                "sample@example.com", UserLevel.USER,
                new Image("file:defaultProfilePicture.png"));
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
     * Constructor used to create a complete User
     * object with a title and profile picture
     *
     * @param username the username of the User
     * @param name the name of the User
     * @param title the tile of the User
     * @param emailAddress the User's email address
     * @param level the authority level of the User
     * @param profilePicture the User's profile picture
     */
    public User(String username, String name, String title, String emailAddress, UserLevel level, Image profilePicture) {
        this(username, name, emailAddress, level);
        this.title.set(title);
        this.profilePicture.set(profilePicture);
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
     * Gets the name property for this User
     * @return the name
     */
    public StringProperty getNameProperty() {
        return name;
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
     * Gets the username property for this User
     *
     * @return the username property
     */
    public StringProperty getUsernameProperty() {
        return username;
    }
    /**
     * Gets the User's title
     *
     * @return the title
     */
    public String getTitle() {
        return title.get();
    }

    /**
     * Sets the User's new title
     *
     * @param t the new title
     */
    public void setTitle(String t) {
        title.set(t);
    }

    /**
     * Gets the title property for this User
     *
     * @return the title property
     */
    public StringProperty getTitleProperty() {
        return title;
    }
    /**
     * Gets the User's current profile picture
     *
     * @return the User's profile picture
     */
    public Image getProfilePicture() {
        return profilePicture.get();
    }

    /**
     * Sets the User's profile picture to
     * a new one
     *
     * @param i the new image
     */
    public void setProfilePicture(Image i) {
        profilePicture.set(i);
    }

    /**
     * Gets the image property for this User
     *
     * @return the image property
     */
    public ObjectProperty<Image> getImageProperty() {
        return profilePicture;
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
     * Gets the email address property for this User
     * @return the email address property
     */
    public StringProperty getEmailAddressProperty() {
        return emailAddress;
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
     * Gets the level property for this User
     *
     * @return the level property
     */
    public ObjectProperty<UserLevel>  getUserLevelProperty() {
        return level;
    }

    /**
     * Returns a String representation of a User object
     *
     * @return the String representation
     */
    public String toString() {
        return "Name: " + name.get() + "\n"
                + "Username: " + username.get() + "\n"
                + "Title: " + title.get() + "\n"
                + "Email address: " + emailAddress.get() + "\n"
                + "Authorization level: " + level.get();
    }
}