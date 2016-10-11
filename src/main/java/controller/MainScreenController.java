package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.User;

/**
 *
 * @author tybrown
 */
public class MainScreenController implements Initializable {

    private Stage stage;
    private User activeUser;

    @FXML
    private Label welcomeLabel;

    @FXML
    private StackPane mainPane;

    /**
     * Set the stage for th Main Screen Controller
     * @param stage The stage being set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Greets the user with a welcome message
     * @param activeUser The user who is being greeted
     */
    public void greetUser(User activeUser) {
        //if (activeUser != null) {
        //  welcomeLabel.setText(String.format("Welcome, %s! Your access level is: %s", activeUser.getName(),
        //        activeUser.getUserLevel().toString()));
        this.activeUser = activeUser;
        //} else {
        //  welcomeLabel.setText("Welcome, hacker! How did you get here?");
        //}
    }

    /**
     * Returns user to the Splash Screen
     * @param event Button push that triggers the code
     */
    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        MasterSingleton.showSplashScreen();
        MasterSingleton.fixMainScreenBounds();
    }

    /**
     * Changes to the home screen
     * @param event Button push that triggers the code
     */
    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        MasterSingleton.showHomeScreen();
        MasterSingleton.fixMainScreenBounds();
    }

    /**
     * Changes to the profile screen
     * @param event Button push that triggers the code
     */
    @FXML
    private void handleProfileButtonAction(ActionEvent event) {
        MasterSingleton.showProfileScreen();
        MasterSingleton.fixMainScreenBounds();
    }

    public StackPane getMainPane() {
        return (mainPane);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
}
