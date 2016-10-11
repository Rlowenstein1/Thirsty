package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

/**
 * FXML Controller class
 *
 * @author tybrown
 */
public class HomeScreenController implements Initializable {

    @FXML
    private Label homeName;

    private User activeUser;
    private Stage stage;

    /**
     * Set the stage for the Main Screen Controller
     * @param stage The stage being set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Greets the user with a welcome message
     * @param activeUser The user who is being greeted
     */
    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
        homeName.textProperty().bind(activeUser.getNameProperty());
    }

    public String getTabText() {
        return ("Home");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
