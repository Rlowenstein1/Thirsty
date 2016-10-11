package fxapp;

import controller.MasterSingleton;
import javafx.application.Application;
import javafx.stage.Stage;
import model.UserLevel;
import model.UserManager;

public class Thirsty extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        MasterSingleton.setMainScreen(stage);
        MasterSingleton.showSplashScreen();

        UserManager.register("asdf", "qwerty", "A Silently Deadly Ferret", "a@b.c", UserLevel.ADMINISTRATOR);
        UserManager.register("qwerty", "asdf", "Queen With Evil Rats There Yonder", "a@b.c", UserLevel.USER);

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
