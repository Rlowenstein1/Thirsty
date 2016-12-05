package fxapp;

import controller.MasterSingleton;
import javafx.application.Application;
import javafx.stage.Stage;
import persistence.PersistenceInterface;
import persistence.json.PersistentJsonFile;
import java.io.IOException;
import persistence.json.net.PersistentJsonNetwork;

/**
 * Tyler Brown
 */
public class Thirsty extends Application {


    //private final PersistenceInterface persist = new PersistentJsonFile(PersistentJsonFile.DEFAULT_PATH);
    private final PersistenceInterface persist = new PersistentJsonNetwork("localhost", 9988);

    @Override
    public void start(Stage stage) throws Exception {
        MasterSingleton.setMainScreen(stage);
        if (MasterSingleton.initialize(persist)) {
            MasterSingleton.showSplashScreen();
            stage.show();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        try {
            persist.terminate();
        } catch (IOException e) {
            // TODO how to handle error
        }
    }
}
