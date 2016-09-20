/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import thirsty.fxapp.Thirsty;

/**
 *
 * @author tybrown
 */
public class MasterSingleton {

    private Stage mainScreen;

    public MasterSingleton (Stage mainScreen) {
        this.mainScreen = mainScreen;
    }
    
    public boolean showLoginScreen() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/LoginScreen.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainScreen);
            Scene scene = new Scene(page);
            stage.setScene(scene);

            // Set the person into the controller.
            LoginScreenController controller = loader.getController();
            controller.setStage(stage);

            // Show the dialog and wait until the user closes it
            stage.showAndWait();

            return controller.loginSuccessful();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
