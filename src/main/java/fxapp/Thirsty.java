package fxapp;

import controller.MasterSingleton;
import javafx.application.Application;
import javafx.stage.Stage;
import persistence.PersistenceInterface;
import persistence.json.PersistentJSONFile;

public class Thirsty extends Application {


    private PersistenceInterface persist = new PersistentJSONFile("src/main/resources/db/");

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
