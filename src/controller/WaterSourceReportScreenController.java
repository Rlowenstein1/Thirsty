/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.User;

/**
 * FXML Controller class
 *
 * @author tybrown
 */
public class WaterSourceReportScreenController implements Initializable {

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
    }

    public String getTabText() {
        return ("Submit Water Report");
    }

    /**
     * Handles canceling a water report submission
     * @param event Button push that triggers the code
     */
    @FXML
    private void handleResetButtonAction(ActionEvent event) {
    }


    /**
     * Handles saving a water report
     * @param event Button push that triggers the code
     */
    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
