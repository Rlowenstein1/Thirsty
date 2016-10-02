/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
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
import javafx.stage.Stage;
import lib.Debug;
import model.Authenticator;
import model.User;
import model.UserLevel;
import model.UserManager;
import thirsty.fxapp.Thirsty;

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

    private Image userProfileImage;

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
                InputStream profileImageIS = this.getClass().getResourceAsStream("/resources/img/defaultProfilePicture.png");
                userProfileImage = new Image(profileImageIS);
                activeUser.setProfilePicture(userProfileImage);
            } catch (Exception e) {
                Debug.error("Error while loading river image! Reason: %s", e.toString());
            }

        }
        profileImageView.setImage(userProfileImage);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accountTypeProfileBox.setItems(UserLevel.getAllObservableList());
    }    

    private void setFields(boolean enabled) {
        titleProfileField.setDisable(enabled);
        fullnameProfileField.setDisable(enabled);
        //usernameProfileField.setDisable(enabled); //can't change your username
        emailProfileField.setDisable(enabled);
        accountTypeProfileBox.setDisable(enabled);
        pwProfileField.setDisable(enabled);
        pwConfProfileField.setDisable(enabled);
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
            } else if (password.length() == 0) {
                Debug.debug("Password field cannot be left blank!");
                pwProfileErrorLabel.setText("Password cannot be left blank!");
            } else {
                if (!password.equals(passwordConf)) {
                    Debug.debug("Password and password confirmation do not match!");
                    pwConfProfileErrorLabel.setText("Passwords do not match!");
                } else if (!Authenticator.isValidPassword(password)) {
                    Debug.debug("Password is invalid!");
                    pwProfileErrorLabel.setText("Password is invalid!");
                } else {
                    if (UserManager.updateLogin(username, password)) {
                        Debug.debug("No errors during profile update check!");
                        editing = false;
                        editButton.setText("Edit profile");
                        setFields(true);
                        activeUser.setTitle(title);
                        activeUser.setName(fullname);
                        activeUser.setEmailAddress(email);
                        activeUser.setUserLevel(userLevel);
                    } else {
                        Debug.debug("Failed to update password! Likely cause: username \"%s\" does not exist!", username);
                    }

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


}
