package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lib.Debug;
import model.Authenticator;
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

    /**
     * Checks if user is valid, and if so, sends them to the Main Screen
     * @param event Button push that triggers the code
     */
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = pwField.getText();
        Debug.debug("Verifying user credentials: \"%s\":\"%s\"", username, password);
        loggedUser = UserManager.login(username, password);
        if (loggedUser != null) {
            Debug.debug("User authentication successful!");
            authed = true;
            stage.close();
        } else {
            Debug.debug("User authentication failed!");
        }
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
