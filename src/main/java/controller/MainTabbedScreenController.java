package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import model.User;
import model.UserManager;

/**
 *
 * @author tybrown
 */
public class MainTabbedScreenController implements Initializable {

    private Stage stage;
    private User activeUser;

    @FXML
    private TabPane tabPane;

    /**
     * Set the stage for the Main Screen Controller
     * @param stage The stage being set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the currently logged in user
     * @param activeUser The user who is being greeted
     */
    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    /**
     * Returns user to the Splash Screen. Logs active user out
     * @param event Button push that triggers the code
     */
    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        UserManager.logout(activeUser.getUsername());
        MasterSingleton.showSplashScreen();
        MasterSingleton.fixMainScreenBounds();
    }

    /**
     * Returns the tab pane to add tabs to
     * @return the tab pane to add tabs to
     */
    public TabPane getTabPane() {
        return (tabPane);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
}
