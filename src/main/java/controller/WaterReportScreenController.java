package controller;

import eu.hansolo.fx.DateAxis310;
import java.net.URL;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.application.Platform;
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
    private TreeItem<DisplayableReport> root;
    private Popup popup;
    private WaterReport currentReport = null;
    private final Map<DisplayableReport, TreeItem<DisplayableReport>> itemMap = new HashMap<>();
    private boolean graphUpdating = false;

    private final Label hoverLabel = new Label("");

    private static final String DATA_TYPE_BOTH = "Both";
    private static final String DATA_TYPE_VPPM = "V PPM";
    private static final String DATA_TYPE_CPPM = "C PPM";

    /**
     * Set the stage for the Main Screen Controller
     * @param stage The stage being set
     */
    public void setStage(Stage stage) {
        Stage stage1 = stage;
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
    public synchronized void updateReports(Iterable<model.WaterReport> reportList) {
        //ObservableList<TreeTableColumn<DisplayableReport,
        // ?>> sortColumns = new SimpleListProperty<>(FXCollections.observableArrayList());
        //if (reportTreeTable.getSortOrder().isEmpty()) {
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
                List<QualityReport> q = rr.getQualityReportList();
                for (QualityReport qq : q) {
                    TreeItem<DisplayableReport> qT = new TreeItem<>(qq);
                    rChildren.add(qT);
                    itemMap.put(qq, qT);
                }
            }
        }
        reportNumberColumn.setSortType(SortType.DESCENDING);
        //sortColumns.add(reportNumberColumn);
        Debug.debug("sort order: %s", reportTreeTable.getSortOrder());
        if (reportTreeTable.getSortOrder().isEmpty()) {
            // reportTreeTable.getSortOrder().clear();
            reportTreeTable.getSortOrder().add(reportNumberColumn);
        }
        boolean authed = UserManager.isUserHistoryReportAuthorized(activeUser);
        historyGraphVbox.setVisible(authed);
        historyGraphVbox.setManaged(authed);
        redrawHistoryGraph();
    }

    /**
     * Handle the action when the "fromDateBox" selector changes
     * @param event The event triggering this function
     */
    @FXML
    public void handleFromDateAction(ActionEvent event) {
    }

    /**
     * Handle the action when the "toDateBox" selector changes
     * @param event The event triggering this function
     */
    @FXML
    public void handleToDateAction(ActionEvent event) {
    }

    /**
     * Handle the action when the "dataType" selector changes
     * @param event The event triggering this function
     */
    @FXML
    public void handleDataTypeAction(ActionEvent event) {
        redrawHistoryGraph();
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
            wr = ((QualityReport) r).getParentReport();
        } else if (r instanceof WaterReport) {
            wr = ((WaterReport) r);
        } else {
            throw (new InvalidParameterException(String.format("Unknown report type \"%s\"!", r.getClass().getName())));
        }
        return (currentReport == null ? wr : (currentReport.equals(wr) ? null : wr));
    }

    /**
     * Event handler for when a report is clicked 
     * @param r the report that was clicked
     */
    private void reportSelected(DisplayableReport r) {
        WaterReport newReport = getNewReport(r);
        if (newReport != null) {
            currentReport = newReport;
            resetDateRange();
            redrawHistoryGraph();
        }
    }

    /**
     * Resets the date range selectors
     */
    private void resetDateRange() {
        ObservableList<LocalDateTime> fDItems = fromDateBox.getItems();
        ObservableList<LocalDateTime> tDItems = toDateBox.getItems();
        boolean oldGU = graphUpdating;
        graphUpdating = true;
        fromDateBox.setValue(null);
        toDateBox.setValue(null);
        fDItems.clear();
        tDItems.clear();
        graphUpdating = oldGU;
    }

    /**
     * Draws the series (lines) on the history graph
     * @param vppm Should we draw the vppm
     * @param cppm Should we draw the cppm
     * @param qList The list of events to draw
     * @param fromDate The starting date to filter the events
     * @param toDate The ending date to filter the events
     */
    private void drawSeries(boolean vppm, boolean cppm, Iterable<model.QualityReport> qList,
                            LocalDateTime fromDate, LocalDateTime toDate) {
        ObservableList<LineChart.Series<LocalDateTime, Double>> graphData = historyGraph.getData();
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
        double maxY = 0;
        double minY = Double.MAX_VALUE;
        for (QualityReport q : qList) {
            LocalDateTime qD = q.getDateTime();
            if ((qD.isAfter(fromDate) || qD.isEqual(fromDate)) && (qD.isBefore(toDate) || qD.isEqual(toDate))) {
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
        xAxis.setAutoRanging(false);
        final double minuteFudgeMult = 0.0005;
        long minuteFudge = ((long) ((toDate.toEpochSecond(ZoneOffset.UTC)
                - fromDate.toEpochSecond(ZoneOffset.UTC)) * minuteFudgeMult));
        if (minuteFudge == 0) {
            minuteFudge = 1;
        }
        xAxis.setLowerBound(fromDate.minusMinutes(minuteFudge)); //add a small margin to both sides of the axis
        xAxis.setUpperBound(toDate.plusMinutes(minuteFudge));
        
        yAxis.setAutoRanging(false);
        yAxis.setForceZeroInRange(false);
        final double yFudgeMult = 0.05;
        double yFudge = ((maxY - minY) * yFudgeMult);
        if (yFudge == 0) {
            yFudge = (maxY != 0 ? maxY : minY) * yFudgeMult;
        }
        yAxis.setLowerBound(minY - yFudge);
        yAxis.setUpperBound(maxY + yFudge);
        final double tickMult = 0.1;
        yAxis.setTickUnit((maxY - minY) * tickMult);
        
    }

    /**
     * Redraws the history graph
     */
    public void redrawHistoryGraph() {
        if (currentReport == null || graphUpdating) {
            return;
        }
        graphUpdating = true;
        ObservableList<LineChart.Series<LocalDateTime, Double>> graphData = historyGraph.getData();
        graphData.clear();
        if (UserManager.isUserHistoryReportAuthorized(activeUser)) {
            List<QualityReport> qList = currentReport.getQualityReportList();


            LocalDateTime fromDate = fromDateBox.getValue();
            LocalDateTime toDate = toDateBox.getValue();
            //if either of these are null, we want to set it to the min/max of the reports
                
            ObservableList<LocalDateTime> fDItems = fromDateBox.getItems();
            ObservableList<LocalDateTime> tDItems = toDateBox.getItems();
            fDItems.clear();
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

            if (!qList.isEmpty()) {

                LocalDateTime minD = LocalDateTime.MAX;
                LocalDateTime maxD = LocalDateTime.MIN;
                for (QualityReport q : qList) {
                    LocalDateTime qD = q.getDateTime();
                    if (toDate == null || !qD.isAfter(toDate)) {
                        fDItems.add(qD);
                    }
                    
                    if (fromDate == null || !qD.isBefore(fromDate)) {
                        tDItems.add(qD);
                    }
                    
                    if (qD.compareTo(maxD) > 0) {
                        maxD = qD;
                    }
                    if (qD.compareTo(minD) < 0) {
                        minD = qD;
                    }
                }
                if (fromDate == null) {
                    fromDate = minD;
                }
                if (toDate == null) {
                    toDate = maxD;
                }
                fromDateBox.setValue(fromDate);
                toDateBox.setValue(toDate);

                drawSeries(vppm, cppm, qList, fromDate, toDate);
            } else {
                historyGraphVbox.setDisable(true);
            }
        }
        graphUpdating = false;
    }

    /**
     * Creates the mouse events on a Series so that the tooltip pops up and the click fires the selector
     * @param series The series you want the events placed on
     * @param yName The name of the series to display in the tooltip
     */
    private void applyDataPointMouseEvents(LineChart.Series<LocalDateTime, Double> series, String yName) {
        Platform.runLater(() -> {
            StackPane popupPane = new StackPane(hoverLabel);
            popupPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
                    CornerRadii.EMPTY, Insets.EMPTY)));
            popup = new Popup();
            popup.getContent().add(popupPane);
            
            for (LineChart.Data<LocalDateTime, Double> dataPoint : series.getData()) {
                final Node node = dataPoint.getNode();
                
                node.setOnMouseEntered(mouseEvent -> {
                    hoverLabel.setText(String.format("Date: %s\n%s: %s",
                            DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dataPoint.getXValue()),
                            yName, dataPoint.getYValue()));
                    final int xSubtract = 35;
                    final int ySubtract = 40;
                    popup.setX(mouseEvent.getScreenX() - xSubtract);
                    popup.setY(mouseEvent.getScreenY() - ySubtract);
                    popup.show(series.getNode().getScene().getWindow());
                });
                
                node.setOnMouseExited(mouseEvent -> popup.hide());

                node.setOnMouseClicked(mouseEvent -> {
                    TreeItem<DisplayableReport> tI = itemMap.get(node.getUserData());
                    if (tI != null) {
                        tI.getParent().setExpanded(true);
                        TreeTableViewSelectionModel<DisplayableReport> rTTselectionModel =
                                reportTreeTable.getSelectionModel();
                        rTTselectionModel.select(tI);
                        rTTselectionModel.select(tI); //selects the wrong one the first time. needs to be called twice
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
        fromDateBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            redrawHistoryGraph();
        });
        toDateBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            redrawHistoryGraph();
        });
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
