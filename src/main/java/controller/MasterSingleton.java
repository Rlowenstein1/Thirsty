package controller;

import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lib.Debug;
import model.User;
import fxapp.Thirsty;
import java.util.List;
import javafx.scene.control.TreeItem;
import model.ReportManager;
import model.UserLevel;
import model.WaterReport;

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

    private static WaterSourceReportScreenController waterSourceReportController;
    private static Tab waterSourceReportTab;

    private static WaterQualityReportScreenController waterQualityReportController;
    private static Tab waterQualityReportTab;

    private static WaterReportScreenController waterReportController;

    private static MapScreenController mapController;

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
     *
     * @return Boolean stating whether or not the user was logged in
     * successfully
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
    public static void showMainTabbedScreen() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(Thirsty.class.getResource("/view/MainTabbedScreen.fxml"));
            //load the master page with toolbar
            // -- inside is a StackPane which will hold the other pages
            AnchorPane page = loader.load();
            MainTabbedScreenController controller = loader.getController();
            mainStage.setTitle("Thirsty?");
            controller.setStage(mainStage);
            controller.setActiveUser(activeUser);

            tabPane = controller.getTabPane();

            ObservableList<Tab> tabList = tabPane.getTabs();

            /*
            loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/HomeScreen.fxml"));
            homePane = loader.load();
            homeController = loader.getController();
            homeController.setActiveUser(activeUser);
            homeController.setStage(mainStage);

            Tab homeTab = new Tab();
            homeTab.setText(homeController.getTabText());
            homeTab.setContent(homePane);
            tabList.add(homeTab);
            */

            loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/ProfileScreen.fxml"));
            profilePane = loader.load();
            profileController = loader.getController();
            profileController.setActiveUser(activeUser);
            profileController.setStage(mainStage);

            Tab profileTab = new Tab();
            profileTab.setText(profileController.getTabText());
            profileTab.setContent(profilePane);
            tabList.add(profileTab);

            loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/WaterSourceReportScreen.fxml"));
            GridPane waterSourceReportPane = loader.load();
            waterSourceReportController = loader.getController();
            waterSourceReportController.setActiveUser(activeUser);
            waterSourceReportController.setStage(mainStage);

            waterSourceReportTab = new Tab();
            waterSourceReportTab.setText(waterSourceReportController.getTabText());
            waterSourceReportTab.setContent(waterSourceReportPane);
            tabList.add(waterSourceReportTab);

            if (activeUser.getUserLevel().compareTo(UserLevel.USER) > 0) {
                loader = new FXMLLoader();
                loader.setLocation(Thirsty.class.getResource("/view/WaterQualityReportScreen.fxml"));
                GridPane waterQualityReportPane = loader.load();
                waterQualityReportController = loader.getController();
                waterQualityReportController.setActiveUser(activeUser);
                waterQualityReportController.setStage(mainStage);

                waterQualityReportTab = new Tab();
                waterQualityReportTab.setText(waterQualityReportController.getTabText());
                waterQualityReportTab.setContent(waterQualityReportPane);
                tabList.add(waterQualityReportTab);
            }

            loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/WaterReportScreen.fxml"));
            AnchorPane waterReportPane = loader.load();
            waterReportController = loader.getController();
            waterReportController.setActiveUser(activeUser);
            waterReportController.setStage(mainStage);
            updateReportScreen();

            Tab waterReportTab = new Tab();
            waterReportTab.setText(waterReportController.getTabText());
            waterReportTab.setContent(waterReportPane);
            tabList.add(waterReportTab);

            loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/MapScreen.fxml"));
            AnchorPane mapPane = loader.load();
            mapController = loader.getController();
            mapController.setActiveUser(activeUser);
            mapController.setStage(mainStage);

            Tab mapTab = new Tab();
            mapTab.setText(mapController.getTabText());
            mapTab.setContent(mapPane);
            tabList.add(mapTab);

            tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
                if (newTab.equals(mapTab)) {
                    mapController.updateMap();
                }
            });

            Scene scene = new Scene(page);
            mainStage.setScene(scene);

            fixMainScreenBounds();

        } catch (IOException e) {
            Debug.error("Exception while creating/showing main screen! Reason: %s", e.toString());
        }
    }

    /**
     * Pre-populates a report and jumps to the tab
     * @param lat The latitude coordinate to pre-populate
     * @param lng The longitude coordinate to pre-populate
     */
    public static void populateNewAReport(double lat, double lng) {
        waterSourceReportController.populateReport(lat, lng);
        tabPane.getSelectionModel().select(waterSourceReportTab);
    }

    /**
     * Pre-populates a report and jumps to the tab
     * @param num The number of the report
     */
    public static void populateNewQReport(int num) {
        waterQualityReportController.populateReport(num);
        tabPane.getSelectionModel().select(waterQualityReportTab);
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
     * Updates the reports screen
     */
    public static void updateReportScreen() {
        waterReportController.updateReports(ReportManager.getWaterReportList());
    }
}
