/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.Collection;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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

    private static StackPane mainPane;
    private static TabPane tabPane;

    private static GridPane profilePane;
    private static ProfileScreenController profileController;

    private static AnchorPane homePane;
    private static HomeScreenController homeController;

    private static User activeUser = null;

    /**
     * Constructor
     */
    private MasterSingleton() {

    }

    /**
     * The main screen doesn't like resizing itself. This does it properly
     */
    public static void fixMainScreenBounds() {
        mainStage.sizeToScene();
        mainStage.setWidth(mainStage.getWidth());
        mainStage.setHeight(mainStage.getHeight());
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
            loader.setLocation(Thirsty.class.getResource("/LoginScreen.fxml"));
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
            loader.setLocation(Thirsty.class.getResource("/MainScreen.fxml"));
            //load the master page with toolbar
            // -- inside is a StackPane which will hold the other pages
            AnchorPane page = loader.load();
            MainScreenController controller = loader.getController();
            mainStage.setTitle("Thirsty?");
            controller.setStage(mainStage);
            controller.greetUser(activeUser);

            mainPane = controller.getMainPane();

            loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/ProfileScreen.fxml"));
            profilePane = loader.load();
            profileController = loader.getController();


            loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/HomeScreen.fxml"));
            homePane = loader.load();
            homeController = loader.getController();

            showHomeScreen();

            Scene scene = new Scene(page);
            mainStage.setScene(scene);

            fixMainScreenBounds();

        } catch (IOException e) {
            Debug.error("Exception while creating/showing main screen! Reason: %s", e.toString());
        }
    }

    /**
     * Displays the main screen
     */
    public static void showMainTabbedScreen() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(Thirsty.class.getResource("/MainTabbedScreen.fxml"));
            //load the master page with toolbar
            // -- inside is a StackPane which will hold the other pages
            AnchorPane page = loader.load();
            MainTabbedScreenController controller = loader.getController();
            mainStage.setTitle("Thirsty?");
            controller.setStage(mainStage);
            controller.setActiveUser(activeUser);

            tabPane = controller.getTabPane();

            ObservableList<Tab> tabList = tabPane.getTabs();

            loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/HomeScreen.fxml"));
            homePane = loader.load();
            homeController = loader.getController();
            homeController.setActiveUser(activeUser);
            homeController.setStage(mainStage);

            Tab homeTab = new Tab();
            homeTab.setText(homeController.getTabText());
            homeTab.setContent(homePane);
            tabList.add(homeTab);

            loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/ProfileScreen.fxml"));
            profilePane = loader.load();
            profileController = loader.getController();
            profileController.setActiveUser(activeUser);
            profileController.setStage(mainStage);

            Tab profileTab = new Tab();
            profileTab.setText(profileController.getTabText());
            profileTab.setContent(profilePane);
            tabList.add(profileTab);

            loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/WaterSourceReportScreen.fxml"));
            GridPane waterSourceReportPane = loader.load();
            WaterSourceReportScreenController waterSourceReportController = loader.getController();
            waterSourceReportController.setActiveUser(activeUser);
            waterSourceReportController.setStage(mainStage);

            Tab waterSourceReportTab = new Tab();
            waterSourceReportTab.setText(waterSourceReportController.getTabText());
            waterSourceReportTab.setContent(waterSourceReportPane);
            tabList.add(waterSourceReportTab);


            Scene scene = new Scene(page);
            mainStage.setScene(scene);

            fixMainScreenBounds();

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
            loader.setLocation(Thirsty.class.getResource("/SplashScreen.fxml"));
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
            loader.setLocation(Thirsty.class.getResource("/RegistrationScreen.fxml"));
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
    public static void showHomeScreen() {
        //mainPane.getChildren().remove(homePane);
        //mainPane.getChildren().add(homePane);
        mainPane.getChildren().setAll(homePane);
        mainStage.setTitle("Thirsty?");
    }


    /**
     * Shows the profile screen
     */
    public static void showProfileScreen() {
        //mainPane.getChildren().remove(profilePane);
        //mainPane.getChildren().add(profilePane);
        mainPane.getChildren().setAll(profilePane);
        mainStage.setTitle("Thirsty? Profile");
        profileController.setStage(mainStage);
        profileController.setActiveUser(activeUser);


        /*
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
<<<<<<< HEAD
            loader.setLocation(Thirsty.class.getResource("/ProfileScreen.fxml"));
=======
            loader.setLocation(Thirsty.class.getResource("/view/ProfileScreen.fxml"));
            //page.getChildren().setAll((Node)loader.load());
>>>>>>> d57d8f95c3040e042605ac5d49adb0d713faa151
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
            controller.setStage(mainStage);
            controller.setActiveUser(activeUser);

            // Show the dialog and wait until the user closes it
            stage.showAndWait();

            return;

        } catch (IOException e) {
            Debug.error("Exception while creating/showing login screen! Reason: %s", e.toString());
        }
        return;
*/
    }
}
