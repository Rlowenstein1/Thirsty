package model;

public class User {
    private String name;
    private String username;
    private String emailAddress;
    private UserLevel level;

    public User(String username) {
        this(username, "Name", "name.gmail.com", UserLevel.USER);
    }

    public User(String username, String name, String emailAddress, UserLevel level) {
        this.username = username;
        this.name = name;
        this.emailAddress = emailAddress;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String n) {
        username = n;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String n) {
        emailAddress = n;
    }

    public UserLevel getUserLevel() {
        return level;
    }

    public void setUserLevel(UserLevel l) {
        level = l;
    }

    public String toString() {
        return "Name: " + name + "\n" +
                "Username: " + username + "\n" +
                "Email address: " + emailAddress + "\n" +
                "Authorization level: " + level;
    }
}