package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Credential;
import model.User;
import model.UserManager;

public class LoginScreenController implements Initializable {

    private Stage stage;
    private boolean authed = false;
    private User loggedUser = null;

    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField pwField;

    @FXML
    private Label errorLabel;

    /**
     * Resets all the error fields
     */
    private void resetErrors() {
        errorLabel.setText("");
    }

    /**
     * Checks if user is valid, and if so, sends them to the Main Screen
     * @param event Button push that triggers the code
     */
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        resetErrors();
        String username = usernameField.getText();
        String password = pwField.getText();
        loggedUser = UserManager.login(new Credential(username, password));
        if (loggedUser != null) {
            authed = true;
            stage.close();
        } else {
            errorLabel.setText("Authentication failed!");
        }
    }

    /**
     * Resets errors when a key is typed in any field
     * @param event the event
     */
    @FXML
    private void handleFieldKeyPressed(KeyEvent event) {
        resetErrors();
    }

    /**
     * Sets the stage for the Login Screen Controller
     * @param stage Stage that is being set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Returns whether the login is successful
     * @return Boolean value of the login state
     */
    public boolean loginSuccessful() {
        return (authed);
    }

    /**
     * Returns the User that is logged in
     * @return User that is logged in
     */
    public User getLoggedUser() {
        return (loggedUser);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
