package thirsty.fxapp;

import controller.MasterSingleton;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Authenticator;
import model.UserLevel;
import model.UserManager;

public class Thirsty extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        MasterSingleton.setMainScreen(stage);
        MasterSingleton.showSplashScreen();

        UserManager.register("asdf", "A Silently Deadly Ferret", "a@b.c", "qwerty", UserLevel.ADMINISTRATOR);
        UserManager.register("qwerty", "Queen With Evil Rats There Yonder", "a@b.c", "asdf", UserLevel.USER);

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
