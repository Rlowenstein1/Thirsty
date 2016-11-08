package model;

import com.google.gson.annotations.Expose;
import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import lib.Debug;

/**
 * A class representing a User in the water source application.
 *
 */
public class User {
    /**
     * Primative properties of the object
     */
    @Expose
    private String name;
    @Expose
    private String username;
    @Expose
    private String emailAddress;
    @Expose
    private String title;
    @Expose
    private UserLevel level;
    private transient Image profilePicture;
    @Expose
    private double lastCoordsLat;
    @Expose
    private double lastCoordsLng;

    /**
     * Transient javafx properties - follow primitives
     */
    private final transient StringProperty nameProperty = new SimpleStringProperty();
    private final transient StringProperty usernameProperty = new SimpleStringProperty();
    private final transient StringProperty emailAddressProperty = new SimpleStringProperty();
    private final transient StringProperty titleProperty = new SimpleStringProperty();
    private final transient ObjectProperty<UserLevel> levelProperty = new SimpleObjectProperty<>();
    private final transient ObjectProperty<Image> profilePictureProperty = new SimpleObjectProperty<>();

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
        this(username, name, "Sample Title", emailAddress, level, null);
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
        setUsername(username);
        setName(name);
        setEmailAddress(emailAddress);
        setUserLevel(level);
        setTitle(title);
        setProfilePicture(profilePicture);
        setLastCoords(33.7756, -84.3963);
    }

    /**
     * Gets the name of the User
     *
     * @return the name of the User
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the User
     *
     * @param n the new name
     */
    public void setName(String n) {
        name = n;
        nameProperty.set(n);
    }

    /**
     * Gets the name property for this User
     * @return the name
     */
    public StringProperty getNameProperty() {
        return nameProperty;
    }

    /**
     * Gets the User's username
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the User
     *
     * @param n the new username
     */
    public void setUsername(String n) {
        username = n;
        usernameProperty.set(n);
    }

    /**
     * Gets the username property for this User
     *
     * @return the username property
     */
    public StringProperty getUsernameProperty() {
        return usernameProperty;
    }
    /**
     * Gets the User's title
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the User's new title
     *
     * @param t the new title
     */
    public void setTitle(String t) {
        title = t;
        titleProperty.set(t);
    }

    /**
     * Gets the title property for this User
     *
     * @return the title property
     */
    public StringProperty getTitleProperty() {
        return titleProperty;
    }
    /**
     * Gets the User's current profile picture
     *
     * @return the User's profile picture
     */
    public Image getProfilePicture() {
        return profilePicture;
    }

    /**
     * Sets the User's profile picture to
     * a new one
     *
     * @param i the new image
     */
    public void setProfilePicture(Image i) {
        profilePicture = i;
        profilePictureProperty.set(i);
    }

    /**
     * Gets the image property for this User
     *
     * @return the image property
     */
    public ObjectProperty<Image> getImageProperty() {
        return profilePictureProperty;
    }

    /**
     * Gets the email address of the User
     *
     * @return the email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the User's email address to a new one
     *
     * @param n the new email address
     */
    public void setEmailAddress(String n) {
        emailAddress = n;
        emailAddressProperty.set(n);
    }

    /**
     * Gets the email address property for this User
     * @return the email address property
     */
    public StringProperty getEmailAddressProperty() {
        return emailAddressProperty;
    }

    /**
     * Gets the authority level of the User
     *
     * @return the User's level
     */
    public UserLevel getUserLevel() {
        return level;
    }

    /**
     * Sets the User's authority level to a new one
     *
     * @param l the new level to be set
     */
    public void setUserLevel(UserLevel l) {
        level = l;
        levelProperty.set(l);
    }

    /**
     * Gets the level property for this User
     *
     * @return the level property
     */
    public ObjectProperty<UserLevel>  getUserLevelProperty() {
        return levelProperty;
    }

    /**
     * Gets the last viewed latitude coordinate
     * @return Latitude
     */
    public double getLastCoordsLat() {
        return (lastCoordsLat);
    }

    /**
     * Gets the last viewed longitude coordinate
     * @return Longitude
     */
    public double getLastCoordsLng() {
        return (lastCoordsLng);
    }

    /**
     * Set the last viewed coordinates
     * @param newCoords the new coordinates
     */
    public void setLastCoords(LatLong newCoords) {
        setLastCoords(newCoords.getLatitude(), newCoords.getLongitude());
    }

    /**
     * Set the last viewed coordinates
     * @param lat Latitude
     * @param lng Longitude
     */
    public void setLastCoords(double lat, double lng) {
        lastCoordsLat = lat;
        lastCoordsLng = lng;
    }


    public User clone() {
        return (new User(username, name, title, emailAddress, level, profilePicture));
    }

    /**
     * Returns a String representation of a User object
     *
     * @return the String representation
     */
    public String toString() {
        return "Name: " + name + "\n"
                + "Username: " + username + "\n"
                + "Title: " + title + "\n"
                + "Email address: " + emailAddress + "\n"
                + "Authorization level: " + level;
    }
}
