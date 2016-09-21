package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lib.Debug;
import model.User;

/**
 *
 * @author tybrown
 */
public class MainScreenController implements Initializable {

    private Stage stage;

    @FXML
    Label welcomeLabel;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void greetUser(User activeUser) {
        if (activeUser != null) {
            welcomeLabel.setText(String.format("Welcome, %s! Your access level is: %s", activeUser.getName(), activeUser.getUserLevel().toString()));
        } else {
            welcomeLabel.setText("Welcome, hacker! How did you get here, anyway?");
        }
    }

    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        MasterSingleton.showSplashScreen();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
}
