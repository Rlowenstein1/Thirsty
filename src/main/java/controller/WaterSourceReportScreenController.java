package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import lib.TextFormatterFactory;
import model.ReportManager;
import model.User;
import model.WaterType;
import model.WaterCondition;
import model.WaterReport;

/**
 * FXML Controller class
 *
 * @author tybrown
 */
public class WaterSourceReportScreenController implements Initializable {

    @FXML
    private Label latErrorLabel;
    @FXML
    private Label longErrorLabel;
    @FXML
    private Label submitErrorLabel;

    @FXML
    private TextField latTextField;
    @FXML
    private TextField longTextField;

    @FXML
    private ComboBox<WaterType> typeComboBox;
    @FXML
    private ComboBox<WaterCondition> conditionComboBox;

    public static final WaterType WATER_TYPE_DEFAULT = WaterType.OTHER;
    public static final WaterCondition WATER_CONDITION_DEFAULT = WaterCondition.MUDDY;

    private User activeUser;
    private Stage stage;

    /**
     * Set the stage for the Main Screen Controller
     * @param stage The stage being set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Greets the user with a welcome message
     * @param activeUser The user who is being greeted
     */
    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    /**
     * Returns the text to put on the tab containing this screen
     * @return The String text to put on the tab
     */
    public String getTabText() {
        return ("Submit Water Report");
    }

    /**
     * Resets error fields
     */
    private void resetErrors() {
        latErrorLabel.setText("");
        longErrorLabel.setText("");
        submitErrorLabel.setText("");
    }

    /**
     * Resets all fields
     */
    private void resetFields() {
        resetErrors();
        latTextField.setText("");
        longTextField.setText("");
        typeComboBox.setValue(WATER_TYPE_DEFAULT);
        conditionComboBox.setValue(WATER_CONDITION_DEFAULT);
    }

    /**
     * Handles canceling a water report submission
     * @param event Button push that triggers the code
     */
    @FXML
    private void handleResetButtonAction(ActionEvent event) {
        resetFields();
    }


    /**
     * Handles saving a water report
     * @param event Button push that triggers the code
     */
    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        resetErrors();
        double latD;
        double longD;
        String latS = latTextField.getText();
        String longS = longTextField.getText();
        
        if (!latS.isEmpty()) {
            latD = Double.parseDouble(latS);
            if (Math.abs(latD) > 90) {
                latErrorLabel.setText("Invalid lattitude!");
                return;
            }
        } else {
            latErrorLabel.setText("Cannot be blank!");
            return;
        }
        if (!longS.isEmpty()) {
            longD = Double.parseDouble(longS);
            if (Math.abs(longD) > 180) {
                longErrorLabel.setText("Invalid longitude!");
                return;
            }
        } else {
            longErrorLabel.setText("Cannot be blank!");
            return;
        }

        WaterReport r = ReportManager.createWaterReport(new Point2D(latD, longD), typeComboBox.getValue(), conditionComboBox.getValue(), activeUser);
        if (r == null) {
            submitErrorLabel.setText("Error during report creation!");
        } else {
            MasterSingleton.updateReportScreen();
            resetFields();
            submitErrorLabel.setText(String.format("Report #%d created successfully!", r.getReportNum()));
        }
    }

    /**
     * Initializes controller class
     * @param url the url
     * @param rb the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeComboBox.setItems(WaterType.getAllObservableList());
        conditionComboBox.setItems(WaterCondition.getAllObservableList());

        latTextField.setTextFormatter(TextFormatterFactory.decimalOnlyTextFormatter());
        longTextField.setTextFormatter(TextFormatterFactory.decimalOnlyTextFormatter());

        resetFields();
    }    
    
}
