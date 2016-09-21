package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lib.Debug;
import model.Authenticator;
import model.User;
import model.UserLevel;

/**
 *
 * @author tybrown
 */
public class RegistrationScreenController implements Initializable {

    private Stage stage;
    private boolean registered = false;
    private User newReg = null;

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
    private ChoiceBox<UserLevel> accountTypeRegBox;
    
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        String fullname = fullnameRegField.getText();
        String username = usernameRegField.getText();
        String email = emailRegField.getText();
        UserLevel userLevel = accountTypeRegBox.getValue();
        String password = pwRegField.getText();
        String passwordConf = pwConfRegField.getText();
        Debug.debug("Attempting to register user: username: \"%s\"; fullname: \"%s\"; email: \"%s\"; password: \"%s\"; passwordConf: \"%s\"; type: \"%s\"", username, fullname, email, password, passwordConf, userLevel.toString());
        if (fullname.length() == 0) {
            Debug.debug("Fullname field cannot be left blank!");
        } else if (username.length() == 0) {
            Debug.debug("Username field cannot be left blank!");
        } else if (email.length() == 0) {
            Debug.debug("email field cannot be left blank!");
        } else if (password.length() == 0) {
            Debug.debug("Password field cannot be left blank!");
        } else if (passwordConf.length() == 0) {
            Debug.debug("Password confirmation field cannot be left blank!");
        } else {
            if (Authenticator.getUser(username) != null) {
                Debug.debug("Username already taken!");
            } else if (!password.equals(passwordConf)) {
                Debug.debug("Password and password confirmation do not match!");
            } else {
                Debug.debug("User registration successful!");
                newReg = Authenticator.register(username, fullname, email, password, userLevel);
                registered = true;
                stage.close();
                return;
            }
        }
        Debug.debug("User registration failed!");
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean registrationSuccessful() {
        return (registered);
    }

    public User getNewUser() {
        return (newReg);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accountTypeRegBox.setItems(UserLevel.getAllObservableList());
        accountTypeRegBox.setValue(UserLevel.USER);
    }    
    
}
