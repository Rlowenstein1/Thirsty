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
import lib.Debug;
import model.User;
import thirsty.fxapp.Thirsty;

/**
 *
 * @author tybrown
 */
public class MasterSingleton {

    private static Stage mainStage;

    private static User activeUser = null;

    /**
     * Constructor
     */
    private MasterSingleton() {

    }

    /**
     * Sets the Main Screen
     * @param mainScreen Stage being set to the Main Screen
     */
    public static void setMainScreen(Stage mainScreen) {
        MasterSingleton.mainStage = mainScreen;
    }

    /**
     * Sets the user that is accessing the application
     * @param activeUser The User that is using the application
     */
    public static void setActiveUser(User activeUser) {
        MasterSingleton.activeUser = activeUser;
    }

    /**
     * Displays the login screen
     * @return Boolean stating whether or not the user was logged in successfully
     */
    public static boolean showLoginScreen() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/LoginScreen.fxml"));
            AnchorPane page = loader.load();


            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            Scene scene = new Scene(page);
            stage.setScene(scene);

            stage.setTitle("Thirsty? Login");

            // Set the person into the controller.
            LoginScreenController controller = loader.getController();
            controller.setStage(stage);

            // Show the dialog and wait until the user closes it
            stage.showAndWait();
            setActiveUser(controller.getLoggedUser());

            return (controller.loginSuccessful());

        } catch (IOException e) {
            Debug.error("Exception while creating/showing login screen! Reason: %s", e.toString());
            return (false);
        }
    }

    /**
     * Displays the main screen
     */
    public static void showMainScreen() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/MainScreen.fxml"));
            AnchorPane page = loader.load();

            mainStage.setTitle("Thirsty?");

            // Create the dialog Stage.
            Scene scene = new Scene(page);
            mainStage.setScene(scene);

            // Set the person into the controller.
            MainScreenController controller = loader.getController();
            controller.setStage(mainStage);

            controller.greetUser(activeUser);

        } catch (IOException e) {
            Debug.error("Exception while creating/showing main screen! Reason: %s", e.toString());
        }
    }

    /**
     * Display the splash screen
     */
    public static void showSplashScreen() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/SplashScreen.fxml"));
            AnchorPane page = loader.load();

            mainStage.setTitle("Thirsty?");

            // Create the dialog Stage.
            Scene scene = new Scene(page);
            mainStage.setScene(scene);

            // Set the person into the controller.
            SplashScreenController controller = loader.getController();
            controller.setStage(mainStage);

        } catch (IOException e) {
            Debug.error("Exception while creating/showing splash screen! Reason: %s", e.toString());
        }
    }

    /**
     * Shows the registration screen for the application
     * @return Boolean value of whether the user was registered
     */
    public static boolean showRegistrationScreen() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/RegistrationScreen.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            Scene scene = new Scene(page);
            stage.setScene(scene);

            stage.setTitle("Thirsty? Register an account");

            // Set the person into the controller.
            RegistrationScreenController controller = loader.getController();
            controller.setStage(stage);

            // Show the dialog and wait until the user closes it
            stage.showAndWait();
            setActiveUser(controller.getNewUser());

            return (controller.registrationSuccessful());

        } catch (IOException e) {
            Debug.error("Exception while creating/showing login screen! Reason: %s", e.toString());
        }
        return (false);
    }


    /**
     * Shows the profile screen
     */
    public static void showProfileScreen() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/ProfileScreen.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            Scene scene = new Scene(page);
            stage.setScene(scene);

            stage.setTitle("Thirsty? Profile");

            // Set the person into the controller.
            ProfileScreenController controller = loader.getController();
            controller.setStage(stage);
            controller.setActiveUser(activeUser);

            // Show the dialog and wait until the user closes it
            stage.showAndWait();

            return;

        } catch (IOException e) {
            Debug.error("Exception while creating/showing login screen! Reason: %s", e.toString());
        }
        return;
    }
}
