package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lib.TextFormatterFactory;
import model.ReportManager;
import model.User;
import model.WaterReport;
import model.QualityReport;
import model.WaterSafety;

/**
 * FXML Controller class
 *
 * @author tybrown
 */
public class WaterQualityReportScreenController implements Initializable {
    @FXML
    private Label vppmErrorLabel;
    @FXML
    private Label cppmErrorLabel;

    @FXML
    private Label reportNumErrorLabel;
    @FXML
    private Label submitErrorLabel;

    @FXML
    private TextField reportNumTextField;

    @FXML
    private TextField vppmTextField;
    @FXML
    private TextField cppmTextField;

    @FXML
    private ComboBox<WaterSafety> safetyComboBox;

    public static final WaterSafety WATER_SAFETY_DEFAULT = WaterSafety.UNKNOWN;

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
        return ("Submit Water Quality Report");
    }

    /**
     * Resets error fields
     */
    private void resetErrors() {
        reportNumErrorLabel.setText("");
        submitErrorLabel.setText("");
        vppmErrorLabel.setText("");
        cppmErrorLabel.setText("");
    }

    /**
     * Resets all fields
     */
    private void resetFields() {
        resetErrors();
        reportNumTextField.setText("");
        vppmTextField.setText("");
        cppmTextField.setText("");
        safetyComboBox.setValue(WATER_SAFETY_DEFAULT);
    }

    /**
     * Sets up the fields with data from elsewhere
     * @param reportNum The report number to add this report to
     */
    public void populateReport(int reportNum) {
        resetFields();
        reportNumTextField.setText(String.format("%d", reportNum));
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

        WaterReport report;
        Double vppm;
        Double cppm;

        String reportNumS = reportNumTextField.getText();
        String vppmS = vppmTextField.getText();
        String cppmS = cppmTextField.getText();
        
        if (!reportNumS.isEmpty()) {
            Integer reportNum = Integer.valueOf(reportNumS);
            if (reportNum < 1) {
                reportNumErrorLabel.setText("Invalid report number!");
                return;
            }
            report = ReportManager.filterWaterReportByNumber(reportNum);
            if (report == null) {
                reportNumErrorLabel.setText("Report does not exist!");
                return;
            }
        } else {
            reportNumErrorLabel.setText("Cannot be blank!");
            return;
        }

        if (!vppmS.isEmpty()) {
            vppm = Double.valueOf(vppmS);
            if (vppm.compareTo(0.0) < 0.0) {
                vppmErrorLabel.setText("Invalid value!");
                return;
            } else if (vppm.compareTo(1000000.0) >= 0.0) {
                vppmErrorLabel.setText("Value too large!");
                return;
            }
        } else {
            vppmErrorLabel.setText("Cannot be blank!");
            return;
        }

        if (!cppmS.isEmpty()) {
            cppm = Double.valueOf(cppmS);
            if (cppm.compareTo(0.0) < 0.0) {
                cppmErrorLabel.setText("Invalid value!");
                return;
            } else if (cppm.compareTo(1000000.0) >= 0.0) {
                cppmErrorLabel.setText("Value too large!");
                return;
            }

        } else {
            cppmErrorLabel.setText("Cannot be blank!");
            return;
        }

        if (Double.valueOf(cppm + vppm).compareTo(1000000.0) >= 0.0) {
            cppmErrorLabel.setText("Combined value too large!");
            return;
        }
        
        QualityReport r = ReportManager.createWaterQualityReport(report, safetyComboBox.getValue(), vppm, cppm, activeUser);
        if (r == null) {
            submitErrorLabel.setText("Error during report creation!");
        } else {
            MasterSingleton.updateReportScreen();
            resetFields();
            submitErrorLabel.setText(String.format("Quality report #%d created on availability report #%d!", r.getReportNum(), report.getReportNum()));
        }
    }

    /**
     * Initializes controller class
     * @param url the url
     * @param rb the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        safetyComboBox.setItems(WaterSafety.getAllObservableList());

        reportNumTextField.setTextFormatter(TextFormatterFactory.integerOnlyTextFormatter());

        vppmTextField.setTextFormatter(TextFormatterFactory.decimalOnlyTextFormatter());
        cppmTextField.setTextFormatter(TextFormatterFactory.decimalOnlyTextFormatter());

        resetFields();
    }    
    
}
