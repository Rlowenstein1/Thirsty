package main.java.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.java.model.User;

/**
 * FXML Controller class
 *
 * @author tybrown
 */
public class HomeScreenController implements Initializable {

    @FXML
    private Label homeName;

    /**
     * Set the stage for the Main Screen Controller
     * @param stage The stage being set
     */
    public void setStage(Stage stage) {
    }

    /**
     * Greets the user with a welcome message
     * @param activeUser The user who is being greeted
     */
    public void setActiveUser(User activeUser) {
        homeName.textProperty().bind(activeUser.getNameProperty());
    }

    /**
     * Returns the text to put on the tab containing this screen
     * @return The String text to put on the tab
     */
    public String getTabText() {
        return ("Home");
    }

    /**
     * Initializes the controller
     * @param url the url
     * @param rb the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
