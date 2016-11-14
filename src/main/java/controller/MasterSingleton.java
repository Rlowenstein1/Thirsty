package main.java.controller;

import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.lib.Debug;
import main.java.model.User;
import main.java.fxapp.Thirsty;
import main.java.model.ReportManager;
import main.java.model.UserManager;
import main.java.persistence.PersistenceInterface;

/**
 *
 * @author tybrown
 */
public class MasterSingleton {
   
    private static Stage mainStage;

    private static TabPane tabPane;

    private static WaterSourceReportScreenController waterSourceReportController;
    private static Tab waterSourceReportTab;

    private static WaterQualityReportScreenController waterQualityReportController;
    private static Tab waterQualityReportTab = null;

    private static WaterReportScreenController waterReportController;

    private static MapScreenController mapController;

    private static User activeUser = null;

    private static ObservableList<Tab> tabList;

    private static PersistenceInterface persist;

    private static final int QUALITY_REPORT_TAB_INDEX = 3;

    /**
     * Initializes the master singleton. Should only be called once
     * @param persist The PersistenceInterface to perform persistence operations with
     */
    public static void initialize(PersistenceInterface persist) {
        MasterSingleton.persist = persist;
        persist.initialize();
        UserManager.initialize(persist);
        ReportManager.initialize(persist);
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
    private static void setActiveUser(User activeUser) {
        MasterSingleton.activeUser = activeUser;
    }

    /**
     * Updates the menus the user is able to view
     */
    public static void updateUserPrivileges() {
        if (waterQualityReportTab != null) {
            if (UserManager.isUserQualityReportAuthorized(activeUser) && !tabList.contains(waterQualityReportTab)) {
                tabList.add(QUALITY_REPORT_TAB_INDEX, waterQualityReportTab);
            } else {
                if (!UserManager.isUserQualityReportAuthorized(activeUser) && tabList.contains(waterQualityReportTab)) {
                    tabList.remove(waterQualityReportTab);
                }
            }
        }
        updateReportScreen();
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
     * Loads all the elements on the map screen
     * @return a Tab with all the loaded elements
     */
    private static Tab loadMapScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/MapScreen.fxml"));
            AnchorPane mapPane = loader.load();
            mapController = loader.getController();
            mapController.setActiveUser(activeUser);
            mapController.setStage(mainStage);

            Tab mapTab = new Tab();
            mapTab.setText(mapController.getTabText());
            mapTab.setContent(mapPane);

            tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
                if (newTab.equals(mapTab) && mapController.isMapInitialized()) {
                    mapController.updateMap();
                }
            });
            return (mapTab);
        } catch (IOException e) {
            Debug.error("Exception while creating/showing map screen! Reason: %s", e.toString());
        }
        return (null);
    }

    /**
     * Loads all the elements on the profile screen
     * @return a Tab with all the loaded elements
     */
    private static Tab loadProfileScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/ProfileScreen.fxml"));
            GridPane profilePane = loader.load();
            ProfileScreenController profileController = loader.getController();
            profileController.setActiveUser(activeUser);
            profileController.setStage(mainStage);

            
            Tab res = new Tab();
            res.setText(profileController.getTabText());
            res.setContent(profilePane);
            return (res);
        } catch (IOException e) {
            Debug.error("Exception while creating/showing profile screen! Reason: %s", e.toString());
        }
        return (null);
    }

    /**
     * Loads all the elements on the water source report screen
     * @return a Tab with all the new elements
     */
    private static Tab loadWaterSourceReportScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/WaterSourceReportScreen.fxml"));
            GridPane waterSourceReportPane = loader.load();
            waterSourceReportController = loader.getController();
            waterSourceReportController.setActiveUser(activeUser);
            waterSourceReportController.setStage(mainStage);

            Tab res = new Tab();
            res.setText(waterSourceReportController.getTabText());
            res.setContent(waterSourceReportPane);
            return (res);
        } catch (IOException e) {
            Debug.error("Exception while creating/showing profile screen! Reason: %s", e.toString());
        }
        return (null);
    }

    /**
     * Loads all the elements on the water quality report screen
     * @return a Tab with all the new elements
     */
    private static Tab loadWaterQualityReportScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/WaterQualityReportScreen.fxml"));
            GridPane waterQualityReportPane = loader.load();
            waterQualityReportController = loader.getController();
            waterQualityReportController.setActiveUser(activeUser);
            waterQualityReportController.setStage(mainStage);

            Tab res = new Tab();
            res.setText(waterQualityReportController.getTabText());
            res.setContent(waterQualityReportPane);
            return (res);
        } catch (IOException e) {
            Debug.error("Exception while creating/showing profile screen! Reason: %s", e.toString());
        }
        return (null);
    }

    /**
     * Loads all the elements on the water report screen
     * @return a Tab with all the new elements
     */
    private static Tab loadWaterReportScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thirsty.class.getResource("/view/WaterReportScreen.fxml"));
            AnchorPane waterReportPane = loader.load();
            waterReportController = loader.getController();
            waterReportController.setActiveUser(activeUser);
            waterReportController.setStage(mainStage);

            Tab res = new Tab();
            res.setText(waterReportController.getTabText());
            res.setContent(waterReportPane);
            return (res);
        } catch (IOException e) {
            Debug.error("Exception while creating/showing profile screen! Reason: %s", e.toString());
        }
        return (null);
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

            tabList = tabPane.getTabs();

            Tab mapTab = loadMapScreen();
            tabList.add(mapTab);
            
            Tab waterReportTab = loadWaterReportScreen();
            tabList.add(waterReportTab);

            waterSourceReportTab = loadWaterSourceReportScreen();
            tabList.add(waterSourceReportTab);

            waterQualityReportTab = loadWaterQualityReportScreen();
            //let water quality report tab be added in updateUserPrivileges()

            Tab profileTab = loadProfileScreen();
            tabList.add(profileTab);

            updateUserPrivileges();

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
