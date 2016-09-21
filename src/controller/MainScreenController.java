package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import lib.Debug;

/**
 *
 * @author tybrown
 */
public class MainScreenController implements Initializable {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        MasterSingleton.showSplashScreen();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
}
