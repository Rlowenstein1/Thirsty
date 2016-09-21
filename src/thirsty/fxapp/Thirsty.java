package thirsty.fxapp;

import controller.MasterSingleton;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Authenticator;
import model.UserLevel;

public class Thirsty extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        MasterSingleton.setMainScreen(stage);
        MasterSingleton.showSplashScreen();

        Authenticator.register("asdf", "A Silent Death Fart", "a@b.c", "qwerty", UserLevel.ADMINISTRATOR);

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
