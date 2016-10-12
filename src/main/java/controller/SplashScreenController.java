package controller;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lib.Debug;

/**
 *
 * @author tybrown
 */
public class SplashScreenController implements Initializable {

    private Stage stage;

    @FXML
    private ImageView riverImageView;

    /**
     * Set the stage for the Splash Screen Controller
     * @param stage The stage being set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles when the Login button is pressed
     */
    @FXML
    public void handleLoginButtonAction() {
        Debug.debug("Login button pressed");
        boolean loggedIn = MasterSingleton.showLoginScreen();
        if (loggedIn) {
            Debug.debug("User logged in!");
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
        Debug.debug("Registration button pressed");
        boolean loggedIn = MasterSingleton.showRegistrationScreen();
        if (loggedIn) {
            Debug.debug("User registered!");
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
