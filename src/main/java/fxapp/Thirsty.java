package fxapp;

import controller.MasterSingleton;
import java.time.LocalDateTime;
import javafx.application.Application;
import javafx.stage.Stage;
import model.ReportManager;
import model.User;
import model.UserLevel;
import model.UserManager;
import model.WaterCondition;
import model.WaterReport;
import model.WaterSafety;
import model.WaterType;
import db.PersistenceAbstractObject;
import db.PersistenceFile;

import com.google.gson.Gson;


public class Thirsty extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        MasterSingleton.setMainScreen(stage);
        MasterSingleton.showSplashScreen();

        User a = UserManager.register("asdf", "qwerty", "A Silently Deadly Ferret", "a@b.c", UserLevel.ADMINISTRATOR);
        User q = UserManager.register("qwerty", "asdf", "Queen With Evil Rats There Yonder", "a@b.c", UserLevel.USER);

        WaterReport r1 = ReportManager.createWaterReport(LocalDateTime.now().plusSeconds(10), 33.77908, -84.39616, WaterType.LAKE, WaterCondition.CLEAR, a);
        WaterReport r2 = ReportManager.createWaterReport(LocalDateTime.now().plusSeconds(36), 33.77367, -84.39616, WaterType.BOTTLED, WaterCondition.MUDDY, q);
        WaterReport r3 = ReportManager.createWaterReport(LocalDateTime.now().plusSeconds(52), 33.77634, -84.39616, WaterType.WELL, WaterCondition.POTABLE, q);

        ReportManager.createWaterQualityReport(LocalDateTime.now().plusMinutes(1).plusSeconds(1), r1, WaterSafety.UNKNOWN, 0.003, 0.02, a);
        ReportManager.createWaterQualityReport(LocalDateTime.now().plusMinutes(2).plusSeconds(2), r1, WaterSafety.SAFE, 0.003, 0.02, a);
        ReportManager.createWaterQualityReport(LocalDateTime.now().plusMinutes(3).plusSeconds(3), r1, WaterSafety.TREATABLE, 0.03, 0.9, a);

        ReportManager.createWaterQualityReport(LocalDateTime.now().plusMinutes(2).plusSeconds(2), r2, WaterSafety.UNSAFE, 45, 73, a);
        ReportManager.createWaterQualityReport(LocalDateTime.now().plusMinutes(4).plusSeconds(4), r2, WaterSafety.UNSAFE, 99, 42, a);

        PersistenceFile.getInstance().load();

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
        PersistenceFile.getInstance().save();
    }
}
