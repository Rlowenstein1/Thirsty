package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lib.Debug;
import model.User;
import model.UserLevel;
import model.UserManager;

/**
 *
 * @author tybrown
 */
public class RegistrationScreenController implements Initializable {

    private Stage stage;
    private boolean registered = false;
    private User newReg = null;

    @FXML
    private Label fullnameRegErrorLabel;

    @FXML
    private Label usernameRegErrorLabel;

    @FXML
    private Label emailRegErrorLabel;

    @FXML
    private Label accountTypeRegErrorLabel;

    @FXML
    private Label pwRegErrorLabel;

    @FXML
    private Label pwConfRegErrorLabel;

    @FXML
    private TextField usernameRegField;
    
    @FXML
    private PasswordField pwRegField;
 
    @FXML
    private PasswordField pwConfRegField;
 
    @FXML
    private TextField emailRegField;
    
    @FXML
    private TextField fullnameRegField;
    
    @FXML
    private ComboBox<UserLevel> accountTypeBox;

    /**
     * Resets error fields
     */
    private void resetErrors() {
        fullnameRegErrorLabel.setText("");
        usernameRegErrorLabel.setText("");
        emailRegErrorLabel.setText("");
        accountTypeRegErrorLabel.setText("");
        pwRegErrorLabel.setText("");
        pwConfRegErrorLabel.setText("");
    }

    /**
     * Registers a user
     * @param event Button push that triggers the code
     */
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        resetErrors();
        String fullname = fullnameRegField.getText();
        String username = usernameRegField.getText();
        String email = emailRegField.getText();
        UserLevel userLevel = accountTypeBox.getValue();
        String password = pwRegField.getText();
        String passwordConf = pwConfRegField.getText();
        Debug.debug("Attempting to register user: username: \"%s\";"
                + "fullname: \"%s\"; email: \"%s\"; password: \"%s\"; passwordConf: \"%s\"; type: \"%s\"",
                username, fullname, email, password, passwordConf, userLevel.toString());
        if (fullname.isEmpty()) {
            Debug.debug("Fullname field cannot be left blank!");
            fullnameRegErrorLabel.setText("Fullname cannot be left blank!");
        } else if (username.isEmpty()) {
            Debug.debug("Username field cannot be left blank!");
            usernameRegErrorLabel.setText("Username cannot be left blank!");
        } else if (email.isEmpty()) {
            Debug.debug("email field cannot be left blank!");
            emailRegErrorLabel.setText("Email cannot be left blank!");
        } else if (password.isEmpty()) {
            Debug.debug("Password field cannot be left blank!");
            pwRegErrorLabel.setText("Password cannot be left blank!");
        /*
        } else if (passwordConf.length() == 0) {
            Debug.debug("Password confirmation field cannot be left blank!");
            pwConfRegErrorLabel.setText("Password confirmation cannot be left blank!");
        */
        } else {
            if (!password.equals(passwordConf)) {
                Debug.debug("Password and password confirmation do not match!");
                pwConfRegErrorLabel.setText("Passwords do not match!");
            } else {
                newReg = UserManager.createUser(username, password, fullname, email, userLevel);
                if (newReg != null) {
                    Debug.debug("User registration successful!");
                    registered = true;
                    stage.close();
                    return;
                }
                Debug.debug("Username already taken (newReg == null)!");
                usernameRegErrorLabel.setText("Username already taken!");
            }
        }
        Debug.debug("User registration failed!");
    }

    /**
     * Set the stage for the Registration Screen Controller
     * @param stage The stage being set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Returns whether the User Registration was successful
     * @return Boolean of whether a new user was registered
     */
    public boolean registrationSuccessful() {
        return (registered);
    }

    /**
     * Returns the newly registered user
     * @return A User
     */
    public User getNewUser() {
        return (newReg);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accountTypeBox.setItems(UserLevel.getAllObservableList());
        accountTypeBox.setValue(UserLevel.USER);
    }    
    
}
