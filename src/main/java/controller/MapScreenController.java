package controller;

import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import model.User;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.MapStateEventType;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.InfoWindow;
import com.lynden.gmapsfx.javascript.object.InfoWindowOptions;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import lib.Debug;
import model.QualityReport;
import model.ReportManager;
import model.UserManager;
import model.WaterReport;
import netscape.javascript.JSObject;


/**
 * FXML Controller class
 *
 * @author tybrown
 */
public class MapScreenController implements Initializable, MapComponentInitializedListener {

    @FXML
    private GoogleMapView mapView;

    @FXML
    private ToggleButton addAReportButton;
    @FXML
    private ToggleButton addQReportButton;

    @FXML
    private JFXTextArea tutTextArea;

    private boolean addingAReport;
    private boolean addingQReport;

    private GoogleMap map;

    private User activeUser;
    private List<Marker> markerList = null;
    private boolean mapInitialized = false;


    private final String defaultText = "Welcome! Thirsty?\nThirsty is the next big thing"
            + " in the water reporting application world! To get started, click one of the buttons below!";


    /**
     * Sets the Quality Report button based on user's level
     */
    private void setQButtonVisibility() {
        boolean authed = UserManager.isUserQualityReportAuthorized(activeUser);
        addQReportButton.setVisible(authed);
        addQReportButton.setManaged(authed);
    }

    /**
     * Greets the user with a welcome message
     * @param activeUser The user who is being greeted
     */
    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
        setQButtonVisibility();
    }

    /**
     * Returns the text to put on the tab containing this screen
     * @return The String text to put on the tab
     */
    public String getTabText() {
        return ("Water Availability Map");
    }

    /**
     * Initializes the controller class.
     * @param url the url
     * @param rb the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            mapView.addMapInializedListener(this);
        } catch (Exception e) {
            Debug.debug("Exception: %s %s", e.toString(), mapView);
        }
        tutTextArea.setText(defaultText);
    }    

    /**
     * Creates the map with default options
     */
    private void createMap() {
        //Set the initial properties of the map
        MapOptions options = new MapOptions();

        //set up the center location for the map
        LatLong center = new LatLong(activeUser.getLastCoordsLat(), activeUser.getLastCoordsLng());

        //noinspection ChainedMethodCall
        final int zoomVal = 16;
        options.center(center)
                .zoom(zoomVal)
                .overviewMapControl(false)
                .panControl(true)
                .rotateControl(true)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(true)
                .mapType(MapTypeIdEnum.HYBRID);

        map = mapView.createMap(options);
        map.addStateEventHandler(MapStateEventType.center_changed, () -> activeUser.setLastCoords(map.getCenter()));
        map.addUIEventHandler(UIEventType.click, (JSObject e) -> {
            if (addingAReport) {
                JSObject clicked = (JSObject) e.getMember("latLng");
                Double clickedLat = (Double) clicked.call("lat");
                Double clickedLng = (Double) clicked.call("lng");

                MarkerOptions markerOptions = new MarkerOptions();
                LatLong loc = new LatLong(clickedLat, clickedLng);

                markerOptions.position(loc)
                        .visible(Boolean.TRUE)
                        .title(String.format("%f,%f", clickedLat, clickedLng));

                Marker marker = new Marker(markerOptions);

                map.addMarker(marker);
                markerList.add(marker);

                InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                infoWindowOptions.content(String.format(
                        "<h1>Create new water availability report at<br />%.5f, %.5f?</h1>",
                        clickedLat, clickedLng
                ));
                
                InfoWindow window = new InfoWindow(infoWindowOptions);
                window.open(map, marker);

                map.panTo(new LatLong(clickedLat, clickedLng));

                Alert alert = new Alert(AlertType.CONFIRMATION,
                        String.format("Create new water availability report at\n%.5f, %.5f?", clickedLat, clickedLng),
                        ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                Bounds screenBounds = mapView.localToScreen(mapView.getBoundsInLocal());
                final double divide = 2.0;
                alert.setY(screenBounds.getMinY() + (screenBounds.getHeight() / divide));
                alert.setX(screenBounds.getMinX() + (screenBounds.getWidth() / divide));
                alert.showAndWait();
                
                if (alert.getResult() == ButtonType.YES) {
                    MasterSingleton.populateNewAReport(clickedLat, clickedLng);
                } else {
                    updateMap();
                }

            }
        });
    }

    /**
     * Updates the map
     */
    @Override
    public void mapInitialized() {
        createMap();
        updateMap();
        mapInitialized = true;
    }

    /**
     * Returns true if the map is initialized, false otherwise
     * @return true if the map is initialized
     */
    public boolean isMapInitialized() {
        return (mapInitialized);
    }

    /**
     * Updates the map markers
     */
    public void updateMap() {
        setQButtonVisibility();
        if (markerList != null) {
            for (Marker m: markerList) {
                map.removeMarker(m);
            }
        }
        List<WaterReport> reportList = ReportManager.getWaterReportList();
        markerList = new ArrayList<>(reportList.size());
        markReports(reportList);
    }

    /**
     * Draws all the given reports
     * @param reportList the list of reports to draw 
     */
    private void markReports(Iterable<WaterReport> reportList) {
        for (WaterReport rr : reportList) {
            MarkerOptions markerOptions = new MarkerOptions();
            double lat = rr.getLatitude();
            double lng = rr.getLongitude();
            LatLong loc = new LatLong(lat, lng);

            markerOptions.position(loc)
                    .visible(Boolean.TRUE)
                    .title(String.format("%f,%f", lat, lng));

            Marker marker = new Marker(markerOptions);

            map.addUIEventHandler(marker,
                UIEventType.click,
                (JSObject obj) -> {
                    InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                    String content = String.format("<h1>%.5f, %.5f</h1>"
                            + "<h3>Water type: %s<br />"
                            + "Water condition: %s</h3>"
                            + "Report number: %d<br />"
                            + "Reported by: %s<br />"
                            + "Reported at: %s<br />"
                            + "<br />"
                            + "",
                            lat, lng,
                            rr.getWaterType(),
                            rr.getWaterCondition(),
                            rr.getReportNum(),
                            rr.getAuthor().getUsername(),
                            rr.getDateTime()
                    );

                    QualityReport qr = rr.getMostRecentQualityReport();
                    if (qr != null) {
                        content += String.format("<br />"
                            + "<h3>Latest quality report:</h3>"
                            + "Water safety: %s<br/>"
                            + "Virus PPM: %f</br>"
                            + "Contaminant PPM: %f</br>"
                            + "Report number: %d<br />"
                            + "Reported by: %s<br />"
                            + "Reported at: %s<br />",
                            qr.getWaterSafety(),
                            qr.getVirusPPM(),
                            qr.getContaminantPPM(),
                            qr.getReportNum(),
                            qr.getAuthor().getUsername(),
                            qr.getDateTime()
                        );
                    }

                    infoWindowOptions.content(content);

                    InfoWindow window = new InfoWindow(infoWindowOptions);
                    window.open(map, marker);

                    if (addingQReport) {
                        map.panTo(new LatLong(lat, lng));
                        
                        Alert alert = new Alert(AlertType.CONFIRMATION,
                                String.format("Create new water Quality report at\n%.5f, %.5f?", lat, lng),
                                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                        Bounds screenBounds = mapView.localToScreen(mapView.getBoundsInLocal());
                        final double divide = 2.0;
                        alert.setY(screenBounds.getMinY() + (screenBounds.getHeight() / divide));
                        alert.setX(screenBounds.getMinX() + (screenBounds.getWidth() / divide));
                        alert.showAndWait();
                        
                        if (alert.getResult() == ButtonType.YES) {
                            MasterSingleton.populateNewQReport(rr.getReportNum());
                        } else {
                            updateMap();
                        }
                    }
                }
            );
            map.addMarker(marker);
            markerList.add(marker);
        }

    }

    /**
     * Toggles adding availability report functionality when clicking on map
     * @param event the ActionEvent of the click
     */
    @FXML
    private void handleAddAReportButtonAction(ActionEvent event) {
        addingAReport = addAReportButton.isSelected();
        addingQReport = false;
        if (addQReportButton.isSelected()) {
            addQReportButton.setSelected(false);
        }
        if (addingAReport) {
            String availText = "Now, click anywhere on the map to add a water report at that coordinate!";
            tutTextArea.setText(String.format("%s\n\n%s", defaultText, availText));
        } else {
            tutTextArea.setText(defaultText);
        }
    }

    /**
     * Toggles adding quality report functionality when clicking on map
     * @param event the ActionEvent of the click
     */
    @FXML
    private void handleAddQReportButtonAction(ActionEvent event) {
        addingAReport = false;
        addingQReport = addQReportButton.isSelected();
        if (addAReportButton.isSelected()) {
            addAReportButton.setSelected(false);
        }
        if (addingQReport) {
            String qualityText = "Now, click on any existing water report to add a quality report to it!";
            tutTextArea.setText(String.format("%s\n\n%s", defaultText, qualityText));
        } else {
            tutTextArea.setText(defaultText);
        }
    }
}
