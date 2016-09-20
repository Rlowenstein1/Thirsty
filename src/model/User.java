package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty emailAddress = new SimpleStringProperty();
    private final ObjectProperty<UserLevel> level = new SimpleObjectProperty();


    public User(String username) {
        this(username, "Name", "sample.gmail.com", UserLevel.USER);
    }

    public User(String username, String name, String emailAddress, UserLevel level) {
        this.username.set(username);
        this.name.set(name);
        this.emailAddress.set(emailAddress);
        this.level.set(level);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String n) {
        name.set(n);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String n) {
        username.set(n);
    }

    public String getEmailAddress() {
        return emailAddress.get();
    }

    public void setEmailAddress(String n) {
        emailAddress.set(n);
    }

    public UserLevel getUserLevel() {
        return level.get();
    }

    public void setUserLevel(UserLevel l) {
        level.set(l);
    }

    public String toString() {
        return "Name: " + name.get() + "\n" +
                "Username: " + username.get() + "\n" +
                "Email address: " + emailAddress.get() + "\n" +
                "Authorization level: " + level.get();
    }
}