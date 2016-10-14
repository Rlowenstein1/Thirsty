package controller;

import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import model.User;
import model.WaterCondition;
import model.WaterReport;
import model.WaterType;

/**
 * FXML Controller class
 *
 * @author tybrown
 */
public class WaterReportScreenController implements Initializable {

    @FXML
    private JFXTreeTableView<WaterReport> reportTreeTable;
    @FXML
    private TreeTableColumn<WaterReport, Number> reportLattitudeColumn;
    @FXML
    private TreeTableColumn<WaterReport, Number> reportLongitudeColumn;
    @FXML
    private TreeTableColumn<WaterReport, Number> reportNumberColumn;
    @FXML
    private TreeTableColumn<WaterReport, LocalDateTime> reportDateColumn;
    @FXML
    private TreeTableColumn<WaterReport, String> reportReporterColumn;
    @FXML
    private TreeTableColumn<WaterReport, WaterType> reportTypeColumn;
    @FXML
    private TreeTableColumn<WaterReport, WaterCondition> reportConditionColumn;
    @FXML
    private TreeTableColumn<WaterReport, String> reportSafetyColumn;
    @FXML
    private TreeTableColumn<WaterReport, Number> reportVppmColumn;
    @FXML
    private TreeTableColumn<WaterReport, Number> reportCppmColumn;

    private User activeUser;
    private Stage stage;
    private TreeItem<WaterReport> root;

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
        return ("View Water Reports");
    }

    /**
     * Gets the root node of the TreeTableView
     * @return the root node
     */
    public TreeItem<WaterReport> getRoot() {
        return (root);
    }

    /**
     * Initializes the controller class.
     * @param url the url
     * @param rb the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root = new TreeItem<>();
        reportTreeTable.setShowRoot(false);
        reportTreeTable.setRoot(root);

        reportLattitudeColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<WaterReport, Number> param)
                -> (param.getValue().getValue().getLatitudeProperty())
        );
 
        reportLongitudeColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<WaterReport, Number> param)
                -> (param.getValue().getValue().getLongitudeProperty())
        );
 
        reportNumberColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<WaterReport, Number> param)
                -> (param.getValue().getValue().getReportNumProperty())
        );
        
        reportDateColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<WaterReport, LocalDateTime> param)
                -> (param.getValue().getValue().getDateTimeProperty())
        );

        reportReporterColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<WaterReport, String> param)
                -> param.getValue().getValue().getAuthor().getUsernameProperty()
        );

        reportTypeColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<WaterReport, WaterType> param)
                -> (param.getValue().getValue().getWaterTypeProperty())
        );

        reportConditionColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<WaterReport, WaterCondition> param)
                -> (param.getValue().getValue().getWaterConditionProperty())
        );
    }    
}
