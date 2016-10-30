package main.java.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.SortType;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import main.java.model.DisplayableReport;
import main.java.model.QualityReport;
import main.java.model.ReportManager;
import main.java.model.User;
import main.java.model.UserManager;
import main.java.model.WaterCondition;
import main.java.model.WaterReport;
import main.java.model.WaterSafety;
import main.java.model.WaterType;

/**
 * FXML Controller class
 *
 * @author tybrown
 */
public class WaterReportScreenController implements Initializable {

    @FXML
    private TreeTableView<DisplayableReport> reportTreeTable;
    @FXML
    private TreeTableColumn<DisplayableReport, Number> reportLattitudeColumn;
    @FXML
    private TreeTableColumn<DisplayableReport, Number> reportLongitudeColumn;
    @FXML
    private TreeTableColumn<DisplayableReport, Number> reportNumberColumn;
    @FXML
    private TreeTableColumn<DisplayableReport, LocalDateTime> reportDateColumn;
    @FXML
    private TreeTableColumn<DisplayableReport, String> reportReporterColumn;
    @FXML
    private TreeTableColumn<DisplayableReport, WaterType> reportTypeColumn;
    @FXML
    private TreeTableColumn<DisplayableReport, WaterCondition> reportConditionColumn;
    @FXML
    private TreeTableColumn<DisplayableReport, WaterSafety> reportSafetyColumn;
    @FXML
    private TreeTableColumn<DisplayableReport, Number> reportVppmColumn;
    @FXML
    private TreeTableColumn<DisplayableReport, Number> reportCppmColumn;

    private User activeUser;
    private Stage stage;
    private TreeItem<DisplayableReport> root;

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
     * Clears the reports
     */
    public void clearReports() {
        root.getChildren().clear();
    }

    /**
     * Updates the reports with the given list of reports
     * @param reportList the list of reports to display
     */
    public synchronized void updateReports(List<WaterReport> reportList) {
        ObservableList<TreeTableColumn<DisplayableReport, ?>> sortColumns = new SimpleListProperty<>(FXCollections.observableArrayList());
        //if (reportTreeTable.getSortOrder().isEmpty()) {
        reportNumberColumn.setSortType(SortType.DESCENDING);
        sortColumns.add(reportNumberColumn);
        /*        
        } else {
            for (TreeTableColumn<DisplayableReport, ?> c : reportTreeTable.getSortOrder()) {
                sortColumns.add(c);
            }
        }
        */
        clearReports();
        ObservableList<TreeItem<DisplayableReport>> children = root.getChildren();
        for (WaterReport rr : reportList) {
            if (rr == null) {
                continue;
            }
            TreeItem<DisplayableReport> rT = new TreeItem<>(rr);
            children.add(rT);
            if (UserManager.isUserQualityReportAuthorized(activeUser)) {
                ObservableList<TreeItem<DisplayableReport>> rChildren = rT.getChildren();
                List<QualityReport> q = ReportManager.getQualityReportList(rr);
                for (QualityReport qq : q) {
                    rChildren.add(new TreeItem<>(qq));
                }
            }
        }
        reportTreeTable.getSortOrder().clear();
        reportTreeTable.getSortOrder().addAll(sortColumns);
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
        
        reportNumberColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<DisplayableReport, Number> param)
                -> (param.getValue().getValue().getReportNumProperty())
        );
        
        reportDateColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<DisplayableReport, LocalDateTime> param)
                -> (param.getValue().getValue().getDateTimeProperty())
        );

        reportLattitudeColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<DisplayableReport, Number> param)
                -> (param.getValue().getValue().getLatitudeProperty())
        );
 
        reportLongitudeColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<DisplayableReport, Number> param)
                -> (param.getValue().getValue().getLongitudeProperty())
        );

        reportReporterColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<DisplayableReport, String> param)
                -> param.getValue().getValue().getAuthorUsernameProperty()
        );

        reportTypeColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<DisplayableReport, WaterType> param)
                -> (param.getValue().getValue().getWaterTypeProperty())
        );

        reportConditionColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<DisplayableReport, WaterCondition> param)
                -> (param.getValue().getValue().getWaterConditionProperty())
        );

        reportSafetyColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<DisplayableReport, WaterSafety> param)
                -> (param.getValue().getValue().getWaterSafetyProperty())
        );

        reportVppmColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<DisplayableReport, Number> param)
                -> (param.getValue().getValue().getVppmProperty())
        );

        reportCppmColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<DisplayableReport, Number> param)
                -> (param.getValue().getValue().getCppmProperty())
        );

        reportTreeTable.setRoot(root);
    }    
}
