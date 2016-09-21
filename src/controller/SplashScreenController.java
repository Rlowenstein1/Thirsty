package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import lib.Debug;

/**
 *
 * @author tybrown
 */
public class SplashScreenController implements Initializable {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleLoginButtonAction() {
        Debug.debug("Login button pressed");
        boolean loggedIn = MasterSingleton.showLoginScreen();
        if (loggedIn) {
            Debug.debug("User logged in!");
            MasterSingleton.showMainScreen();
        } else {
            Debug.debug("User cancelled login");
        }
    }

    @FXML
    public void handleRegisterButtonAction() {
        Debug.debug("Registration button pressed");
        boolean loggedIn = MasterSingleton.showRegistrationScreen();
        if (loggedIn) {
            Debug.debug("User registered!");
            MasterSingleton.showMainScreen();
        } else {
            Debug.debug("User cancelled registration");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
   
}
