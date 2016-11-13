package main.java.fxapp;

import main.java.controller.MasterSingleton;
import javafx.application.Application;
import javafx.stage.Stage;
import main.java.persistence.PersistenceInterface;
import main.java.persistence.json.PersistentJsonFile;

/**
 * Tyler Brown
 */
public class Thirsty extends Application {


    private final PersistenceInterface persist = new PersistentJsonFile("src/main/resources/db/");

    @Override
    public void start(Stage stage) throws Exception {
        MasterSingleton.initialize(persist);
        MasterSingleton.setMainScreen(stage);
        MasterSingleton.showSplashScreen();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        persist.terminate();
    }
}
