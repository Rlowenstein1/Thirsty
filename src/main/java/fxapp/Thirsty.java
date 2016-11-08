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

import com.google.gson.Gson;
import persistence.PersistenceInterface;
import persistence.json.PersistentJSONFile;


public class Thirsty extends Application {


    private PersistenceInterface persist = new PersistentJSONFile("src/main/resources/db/");

    @Override
    public void start(Stage stage) throws Exception {
        MasterSingleton.initialize(persist);
        MasterSingleton.setMainScreen(stage);
        MasterSingleton.showSplashScreen();

        /*
        User a = UserManager.register("asdf", "qwerty", "A Silently Deadly Ferret", "a@b.c", UserLevel.ADMINISTRATOR);
        User q = UserManager.register("qwerty", "asdf", "Queen With Evil Rats There Yonder", "a@b.c", UserLevel.USER);

        WaterReport r1 = ReportManager.createWaterReport(LocalDateTime.now().plusSeconds(10), 33.77908, -84.39616, WaterType.LAKE, WaterCondition.CLEAR, a);
        WaterReport r2 = ReportManager.createWaterReport(LocalDateTime.now().plusSeconds(36), 33.77367, -84.39616, WaterType.BOTTLED, WaterCondition.MUDDY, q);
        WaterReport r3 = ReportManager.createWaterReport(LocalDateTime.now().plusSeconds(52), 33.77634, -84.39616, WaterType.WELL, WaterCondition.POTABLE, q);
        WaterReport r4 = ReportManager.createWaterReport(LocalDateTime.now().plusSeconds(59), 33.77334, -84.39316, WaterType.WELL, WaterCondition.POTABLE, q);

        int p = 0;
        for (int i = 0; i < 40; i++) {
            int r = ((int) (Math.random() * 3)) % 4;
            WaterSafety safety;
            switch (r) {
            case 0:
                safety = WaterSafety.SAFE;
                break;
            case 1:
                safety = WaterSafety.TREATABLE;
                break;
            case 2:
                safety = WaterSafety.UNSAFE;
                break;
            default:
                safety = WaterSafety.UNKNOWN;
                break;
            }
            ReportManager.createWaterQualityReport(LocalDateTime.now().plusDays(p).plusHours(p).plusMinutes(p).plusSeconds(p), r1, safety, 1000 + Math.random() * 3, Math.random() * 3, a);
            p += 5 * Math.random();
        }

        ReportManager.createWaterQualityReport(LocalDateTime.now().plusMinutes(2).plusSeconds(2), r2, WaterSafety.UNSAFE, 45, 73, a);
        ReportManager.createWaterQualityReport(LocalDateTime.now().plusMinutes(4).plusSeconds(4), r2, WaterSafety.UNSAFE, 99, 42, a);
        ReportManager.createWaterQualityReport(LocalDateTime.now().plusMinutes(5).plusSeconds(6), r2, WaterSafety.UNSAFE, 33, 35, a);
        ReportManager.createWaterQualityReport(LocalDateTime.now().plusMinutes(39).plusSeconds(8), r2, WaterSafety.UNSAFE, 43, 29, a);

        ReportManager.createWaterQualityReport(LocalDateTime.now().plusMinutes(20).plusSeconds(8), r3, WaterSafety.SAFE, 0.0001, 0.000004, a);
        */

        //PersistenceFile.getInstance().load();

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
