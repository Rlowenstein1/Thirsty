package controller;

import eu.hansolo.fx.DateAxis310;
import java.net.URL;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.SortType;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeTableView.TreeTableViewSelectionModel;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lib.Debug;
import model.DisplayableReport;
import model.QualityReport;
import model.ReportManager;
import model.User;
import model.UserManager;
import model.WaterCondition;
import model.WaterReport;
import model.WaterSafety;
import model.WaterType;

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
    
    @FXML
    private LineChart<LocalDateTime, Double> historyGraph;
    @FXML
    private ComboBox<LocalDateTime> fromDateBox;
    @FXML
    private ComboBox<LocalDateTime> toDateBox;
    @FXML
    private ComboBox<String> dataType;
    @FXML
    private VBox historyGraphVbox;
    @FXML
    private DateAxis310 xAxis;
    @FXML
    private NumberAxis yAxis;

    private User activeUser;
    private Stage stage;
    private TreeItem<DisplayableReport> root;
    private Popup popup;
    private WaterReport currentReport = null;
    private HashMap<DisplayableReport, TreeItem<DisplayableReport>> itemMap = new HashMap<>();

    private Label hoverLabel = new Label("");

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd.MM.yy ");

    private static final String DATA_TYPE_BOTH = "Both";
    private static final String DATA_TYPE_VPPM = "V PPM";
    private static final String DATA_TYPE_CPPM = "C PPM";

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
        itemMap.clear();
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
            itemMap.put(rr, rT);
            if (UserManager.isUserQualityReportAuthorized(activeUser)) {
                ObservableList<TreeItem<DisplayableReport>> rChildren = rT.getChildren();
                List<QualityReport> q = ReportManager.getQualityReportList(rr);
                for (QualityReport qq : q) {
                    TreeItem<DisplayableReport> qT = new TreeItem<>(qq);
                    rChildren.add(qT);
                    itemMap.put(qq, qT);
                }
            }
        }
        reportTreeTable.getSortOrder().clear();
        reportTreeTable.getSortOrder().addAll(sortColumns);
        boolean authed = UserManager.isUserHistoryReportAuthorized(activeUser);
        historyGraphVbox.setVisible(authed);
        historyGraphVbox.setManaged(authed);
    }

    /**
     * Handle the action when the "fromDateBox" selector changes
     * @param e The event triggering this function
     */
    @FXML
    public void handleFromDateAction(ActionEvent e) {
        //redrawHistoryGraph();
    }

    /**
     * Handle the action when the "toDateBox" selector changes
     * @param e The event triggering this function
     */
    @FXML
    public void handleToDateAction(ActionEvent e) {
        //redrawHistoryGraph();
    }

    /**
     * Handle the action when the "dataType" selector changes
     * @param e The event triggering this function
     */
    @FXML
    public void handleDataTypeAction(ActionEvent e) {
        drawNewGraph();
    }

    /**
     * If this report is different than currentReport, returns non-null
     * @param r the report to test
     * @return null if this report is the same parentage as currentReport
     */
    private WaterReport getNewReport(DisplayableReport r) {
        if (r == null) {
            return (currentReport);
        }
        WaterReport wr;
        if (r instanceof QualityReport) {
            wr = ReportManager.getWaterReport((QualityReport) r);
        } else if (r instanceof WaterReport) {
            wr = ((WaterReport) r);
        } else {
            throw (new InvalidParameterException(String.format("Unknown report type \"%s\"!", r.getClass().getName())));
        }
        return (currentReport == null ? wr : (currentReport.equals(wr) ? null : wr));
    }

    private void reportSelected(DisplayableReport r) {
        WaterReport newReport = getNewReport(r);
        if (newReport != null) {
            currentReport = newReport;
            drawNewGraph();
        }
    }

    private void drawNewGraph() {
        fromDateBox.setValue(null);
        toDateBox.setValue(null);
        redrawHistoryGraph();
    }

    public void redrawHistoryGraph() {
        if (currentReport == null) {
            return;
        }
        ObservableList<LineChart.Series<LocalDateTime, Double>> graphData = historyGraph.getData();
        graphData.clear();
        if (UserManager.isUserHistoryReportAuthorized(activeUser)) {
            List<QualityReport> qList = currentReport.getQualityReportList();

            LocalDateTime fromDate = fromDateBox.getValue();
            if (fromDate == null) {
                fromDate = LocalDateTime.MIN;
            }
            LocalDateTime toDate = toDateBox.getValue();
            if (toDate == null) {
                toDate = LocalDateTime.MAX;
            }
            Debug.debug("%s", fromDate);

            ObservableList<LocalDateTime> fDItems = fromDateBox.getItems();
            fDItems.clear();
            ObservableList<LocalDateTime> tDItems = toDateBox.getItems();
            tDItems.clear();

            String dTV = dataType.getValue();
            boolean vppm;
            boolean cppm;
            boolean both = dTV.equals(DATA_TYPE_BOTH);
            if (both) {
                vppm = true;
                cppm = true;
            } else {
                vppm = dTV.equals(DATA_TYPE_VPPM);
                cppm = dTV.equals(DATA_TYPE_CPPM);
            }

            if (qList.size() > 0) {
                LineChart.Series<LocalDateTime, Double> vPPMSeries = new LineChart.Series<>();
                LineChart.Series<LocalDateTime, Double> cPPMSeries = new LineChart.Series<>();
                ObservableList<LineChart.Data<LocalDateTime, Double>> dataV = vPPMSeries.getData();
                ObservableList<LineChart.Data<LocalDateTime, Double>> dataC = cPPMSeries.getData();
                if (vppm) {
                    vPPMSeries.setName("Virus PPM");
                    graphData.add(vPPMSeries);
                    Set<Node> lineNode = historyGraph.lookupAll(".series0");
                    for (final Node line : lineNode) {
                        line.setStyle("-fx-stroke: blue;");
                    }
                    applyDataPointMouseEvents(vPPMSeries, "Virus PPM");
                }
                if (cppm) {
                    cPPMSeries.setName("Contaminant PPM");
                    graphData.add(cPPMSeries);
                    Set<Node> lineNode = historyGraph.lookupAll(String.format(".series%s", vppm ? "1" : "0"));
                    for (final Node line : lineNode) {
                        line.setStyle("-fx-stroke: green;");
                    }
                    applyDataPointMouseEvents(cPPMSeries, "Contaminant PPM");
                }
                
                historyGraphVbox.setDisable(false);
                LocalDateTime minD = LocalDateTime.MAX;
                LocalDateTime maxD = LocalDateTime.MIN;
                double maxY = 0;
                double minY = Double.MAX_VALUE;
                for (QualityReport q : qList) {
                    LocalDateTime qD = q.getDateTime();
                    fDItems.add(qD);
                    tDItems.add(qD);
                    if ((qD.isAfter(fromDate) || qD.isEqual(fromDate)) && (qD.isBefore(toDate) || qD.isEqual(toDate))) {
                        if (qD.compareTo(maxD) > 0) {
                            maxD = qD;
                        }
                        if (qD.compareTo(minD) < 0) {
                            minD = qD;
                        }
                        if (vppm) {
                            double qV = q.getVirusPPM();
                            LineChart.Data<LocalDateTime, Double> dataPoint = new LineChart.Data<>(qD, qV);
                            dataV.add(dataPoint);
                            dataPoint.getNode().setUserData(q);
                            if (qV > maxY) {
                                maxY = qV;
                            }
                            if (qV < minY) {
                                minY = qV;
                            }
                        }
                        if (cppm) {
                            double qC = q.getContaminantPPM();
                            LineChart.Data<LocalDateTime, Double> dataPoint = new LineChart.Data<>(qD, qC);
                            dataC.add(dataPoint);
                            dataPoint.getNode().setUserData(q);
                            if (qC > maxY) {
                                maxY = qC;
                            }
                            if (qC < minY) {
                                minY = qC;
                            }
                        }
                    }
                }
                if (fromDate.isEqual(LocalDateTime.MIN)) {
                    fromDateBox.setValue(minD);
                }
                if (toDate.isEqual(LocalDateTime.MAX)) {
                    toDateBox.setValue(maxD);
                }
                xAxis.setAutoRanging(false);
                long minuteFudge = ((long) ((maxD.toEpochSecond(ZoneOffset.UTC) - minD.toEpochSecond(ZoneOffset.UTC)) * 0.0005));
                xAxis.setLowerBound(minD.minusMinutes(minuteFudge)); //add a small margin to both sides of the axis
                xAxis.setUpperBound(maxD.plusMinutes(minuteFudge));

                yAxis.setAutoRanging(false);
                yAxis.setForceZeroInRange(false);
                double yFudge = ((maxY - minY) * 0.05);
                yAxis.setLowerBound(minY - yFudge);
                yAxis.setUpperBound(maxY + yFudge);
                yAxis.setTickUnit((maxY - minY) * 0.1);
            } else {
                historyGraphVbox.setDisable(true);
            }
        }
    }

    private void applyDataPointMouseEvents(LineChart.Series<LocalDateTime, Double> series, String yName) {
        Platform.runLater(() -> {
            StackPane popupPane = new StackPane(hoverLabel);
            popupPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            popup = new Popup();
            popup.getContent().add(popupPane);
            
            for (LineChart.Data<LocalDateTime, Double> dataPoint : series.getData()) {
                final Node node = dataPoint.getNode();
                
                node.setOnMouseEntered(mouseEvent -> {
                    hoverLabel.setText(String.format("Date: %s\n%s: %s", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dataPoint.getXValue()), yName, dataPoint.getYValue()));
                    popup.setX(mouseEvent.getScreenX() - 35);
                    popup.setY(mouseEvent.getScreenY() - 40);
                    popup.show(series.getNode().getScene().getWindow());
                });
                
                node.setOnMouseExited(mouseEvent -> {
                    popup.hide();
                });

                node.setOnMouseClicked(mouseEvent -> {
                    TreeItem<DisplayableReport> tI = itemMap.get(node.getUserData());
                    if (tI != null) {
                        tI.getParent().setExpanded(true);
                        TreeTableViewSelectionModel<DisplayableReport> rTTselectionModel = reportTreeTable.getSelectionModel();
                        rTTselectionModel.select(tI);
                        reportTreeTable.scrollTo(rTTselectionModel.getSelectedIndex());
                    } else {
                        Debug.warn("Unable to find a TreeItem to map this report to! %s", node.getUserData());
                    }
                });
            }
        });
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

        reportTreeTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        reportSelected(newValue.getValue());
                    }
                });
        historyGraphVbox.setDisable(true);
        ObservableList<String> dItems = dataType.getItems();
        dItems.add(DATA_TYPE_VPPM);
        dItems.add(DATA_TYPE_CPPM);
        dItems.add(DATA_TYPE_BOTH);
        dataType.setValue(DATA_TYPE_BOTH);
        xAxis.setTickLabelFormatter(new StringConverter<LocalDateTime>() {
            @Override public String toString(LocalDateTime localDateTime) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY/dd/MM\nHH:mm:ss");
                return dtf.format(localDateTime);
            }
            @Override public LocalDateTime fromString(String s) {
                return LocalDateTime.parse(s);
            }
        });
        yAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override public String toString(Number d) {
                return (String.format("%.2f", d));
            }
            @Override public Number fromString(String d) {
                return (Double.valueOf(d));
            }
        });
    }    
}
