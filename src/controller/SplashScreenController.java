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
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
   
}
