package main.java.controller;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.java.lib.Debug;

/**
 *
 * @author tybrown
 */
public class SplashScreenController implements Initializable {

    @FXML
    private ImageView riverImageView;

    /**
     * Set the stage for the Splash Screen Controller
     * @param stage The stage being set
     */
    public void setStage(Stage stage) {
    }

    /**
     * Handles when the Login button is pressed
     */
    @FXML
    public void handleLoginButtonAction() {
        boolean loggedIn = MasterSingleton.showLoginScreen();
        if (loggedIn) {
            MasterSingleton.showMainTabbedScreen();
        } else {
            Debug.debug("User cancelled login");
        }
    }

    /**
     * Handles when the Registration button is pressed
     */
    @FXML
    public void handleRegisterButtonAction() {
        boolean loggedIn = MasterSingleton.showRegistrationScreen();
        if (loggedIn) {
            MasterSingleton.showMainTabbedScreen();
        } else {
            Debug.debug("User cancelled registration");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //File riverFile = new File(Thirsty.class.getResource("/resources/img/river.png").toURI());
            InputStream riverIS = this.getClass().getResourceAsStream("/img/thirstyNewLogo.png");
            //Debug.debug("Exists: %b", riverFile.exists());
            Image riverImage = new Image(riverIS);
            riverImageView.setImage(riverImage);
        } catch (Exception e) {
            Debug.error("Error while loading logo image! Reason: %s", e.toString());
        }
    }
   
}
