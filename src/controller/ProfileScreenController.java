/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import lib.Debug;
import model.Authenticator;
import model.User;
import model.UserLevel;
import model.UserManager;

/**
 *
 * @author tybrown
 */
public class ProfileScreenController implements Initializable {

    private boolean editing = false;

    private User activeUser = null;

    @FXML
    private Label titleProfileErrorLabel;

    @FXML
    private Label fullnameProfileErrorLabel;

    @FXML
    private Label usernameProfileErrorLabel;

    @FXML
    private Label emailProfileErrorLabel;

    @FXML
    private Label accountTypeProfileErrorLabel;

    @FXML
    private Label pwProfileErrorLabel;

    @FXML
    private Label pwConfProfileErrorLabel;

    @FXML
    private Label imageProfileErrorLabel;

    @FXML
    private TextField usernameProfileField;
    
    @FXML
    private PasswordField pwProfileField;
 
    @FXML
    private PasswordField pwConfProfileField;
 
    @FXML
    private TextField emailProfileField;
    
    @FXML
    private TextField titleProfileField;
    
    @FXML
    private TextField fullnameProfileField;
    
    @FXML
    private ChoiceBox<UserLevel> accountTypeProfileBox;

    @FXML
    private Button editButton;

    @FXML
    private ImageView profileImageView;

    @FXML
    private Button profileImageChangeButton;

    private Image userProfileImage;

    private FileChooser fileChooser = new FileChooser();

    private ExtensionFilter imageFilter = new ExtensionFilter("Images (bmp, gif, jpeg, png)", "*.bmp", "*.gif", "*.jpeg", "*.jpg", "*.png");

    private ExtensionFilter allFilter = new ExtensionFilter("All files (*.*)", "*.*");

    /**
     * Resets error fields
     */
    private void resetErrors() {
        titleProfileErrorLabel.setText("");
        fullnameProfileErrorLabel.setText("");
        usernameProfileErrorLabel.setText("");
        emailProfileErrorLabel.setText("");
        accountTypeProfileErrorLabel.setText("");
        pwProfileErrorLabel.setText("");
        pwConfProfileErrorLabel.setText("");
        imageProfileErrorLabel.setText("");
    }

   
    private Stage stage;

    /**
     * Set the stage for the Profile Screen Controller
     * @param stage The stage being set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the user that is accessing the application
     * @param activeUser The User that is using the application
     */
    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;

        titleProfileField.setText(activeUser.getTitle());
        fullnameProfileField.setText(activeUser.getName());
        usernameProfileField.setText(activeUser.getUsername());
        emailProfileField.setText(activeUser.getEmailAddress());
        accountTypeProfileBox.setValue(activeUser.getUserLevel());
        pwProfileField.setText("");
        pwConfProfileField.setText("");
        userProfileImage = activeUser.getProfilePicture();
        if (userProfileImage == null) {
            try {
                InputStream profileImageIS = this.getClass().getResourceAsStream("/defaultProfilePicture.png");
                userProfileImage = new Image(profileImageIS);
                activeUser.setProfilePicture(userProfileImage);
            } catch (Exception e) {
                Debug.error("Error while loading river image! Reason: %s", e.toString());
                return;
            }

        }
        profileImageView.setImage(userProfileImage);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accountTypeProfileBox.setItems(UserLevel.getAllObservableList());
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.getExtensionFilters().add(allFilter);
    }

    /**
     * Allows the user to edit profile information
     * @param enabled Whether edit profile button has been pressed
     */
    private void setFields(boolean enabled) {
        titleProfileField.setDisable(enabled);
        fullnameProfileField.setDisable(enabled);
        //usernameProfileField.setDisable(enabled); //can't change your username
        emailProfileField.setDisable(enabled);
        accountTypeProfileBox.setDisable(enabled);
        pwProfileField.setDisable(enabled);
        pwConfProfileField.setDisable(enabled);
        profileImageChangeButton.setDisable(enabled);
    }
 
    /**
     * Handles editing and saving a user's profile
     * @param event Button push that triggers the code
     */
    @FXML
    private void handleEditButtonAction(ActionEvent event) {
        if (editing) {
            resetErrors();
            String title = titleProfileField.getText();
            String fullname = fullnameProfileField.getText();
            String username = usernameProfileField.getText();
            String email = emailProfileField.getText();
            UserLevel userLevel = accountTypeProfileBox.getValue();
            String password = pwProfileField.getText();
            String passwordConf = pwConfProfileField.getText();
            Debug.debug("Attempting to modify user: title: \"%s\"; username: \"%s\"; fullname: \"%s\"; email: \"%s\"; password: \"%s\"; passwordConf: \"%s\"; type: \"%s\"", title, username, fullname, email, password, passwordConf, userLevel.toString());
            if (fullname.length() == 0) {
                Debug.debug("Fullname field cannot be left blank!");
                fullnameProfileErrorLabel.setText("Fullname cannot be left blank!");
            } else if (username.length() == 0) {
                Debug.debug("Username field cannot be left blank!");
                usernameProfileErrorLabel.setText("Username cannot be left blank!");
            } else if (email.length() == 0) {
                Debug.debug("email field cannot be left blank!");
                emailProfileErrorLabel.setText("Email cannot be left blank!");
            } else if (password.length() != 0 && !Authenticator.isValidPassword(password)) {
                Debug.debug("Invalid password!");
                pwProfileErrorLabel.setText("Invalid Password!");

            } else {
                if (!password.equals(passwordConf)) {
                    Debug.debug("Password and password confirmation do not match!");
                    pwConfProfileErrorLabel.setText("Passwords do not match!");
                } else {
                    if (password.length() != 0) {
                        if (!UserManager.updateLogin(username, password)) {
                            Debug.debug("Failed to update password! Likely cause: username \"%s\" does not exist!", username);
                            return;
                        }
                    }
                    Debug.debug("No errors during profile update check!");
                    editing = false;
                    editButton.setText("Edit profile");
                    setFields(true);
                    activeUser.setTitle(title);
                    activeUser.setName(fullname);
                    activeUser.setEmailAddress(email);
                    activeUser.setUserLevel(userLevel);
                    return;

                    /*
                    newProfile = UserManager.register(username, password, fullname, email, userLevel);
                    if (newProfile != null) {
                        registered = true;
                        stage.close();
                        return;
                    }
                    Debug.debug("Username already taken!");
                    usernameProfileErrorLabel.setText("Username already taken!");
                    */
                }
            }
            Debug.debug("User registration failed!");

        } else {
            editing = true;
            setFields(false);
            editButton.setText("Save");
        }
        /*
        */
    }

    /**
     * Handles Change Image button press, opens file browser for user
     * @param event Button Press
     */
    @FXML
    private void handleProfileImageChangeButtonAction(ActionEvent event) {
        resetErrors();
        fileChooser.setTitle("Choose a new profile image");
        fileChooser.setSelectedExtensionFilter(imageFilter);
        File newProfileImage = fileChooser.showOpenDialog(stage);
        if (newProfileImage == null) {
            Debug.debug("No image selected!");
            return;
        }

        try {
            Debug.debug("Attempting to change profile image to \"%s\"", newProfileImage.getCanonicalPath());
        } catch (IOException e) {
            Debug.debug("IOException while attempting to get path of selected file! Reason: %s", e.toString());
            imageProfileErrorLabel.setText("Failed to get file path!");
            return;
        }

        InputStream profileImageIS;
        try {
            profileImageIS = new FileInputStream(newProfileImage);
        } catch (FileNotFoundException e) {
            Debug.debug("File not found! Reason: %s", e.toString());
            imageProfileErrorLabel.setText("File not found!");
            return;
        }
        Image newUserProfileImage = new Image(profileImageIS);
        if (newUserProfileImage.isError()) {
            Debug.debug("Error while loading image! Reason: %s", newUserProfileImage.getException().toString());
            imageProfileErrorLabel.setText("Failed to create image from file!");
            return;
        }
        userProfileImage = newUserProfileImage;
        activeUser.setProfilePicture(newUserProfileImage);
        profileImageView.setImage(newUserProfileImage);
    }

}
