package controller;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lib.Debug;
import thirsty.fxapp.Thirsty;

/**
 *
 * @author tybrown
 */
public class SplashScreenController implements Initializable {

    private Stage stage;

    @FXML
    ImageView riverImageView;

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
        try {
            //File riverFile = new File(Thirsty.class.getResource("/resources/img/river.png").toURI());
            InputStream riverIS = this.getClass().getResourceAsStream("/resources/img/river.png");;
            //Debug.debug("Exists: %b", riverFile.exists());
            Image riverImage = new Image(riverIS);
            riverImageView.setImage(riverImage);
        } catch (Exception e) {
            Debug.error("Error while loading river image! Reason: %s", e.toString());
        }
    }
   
}
