package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.User;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.InfoWindow;
import com.lynden.gmapsfx.javascript.object.InfoWindowOptions;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import lib.Debug;
import model.ReportManager;
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

    private GoogleMap map;

    private User activeUser;
    private Stage stage;
    private List<Marker> markerList = null;

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
    }    

    /**
     * Creates the map with default options
     */
    public void createMap() {
        //Set the initial properties of the map
        MapOptions options = new MapOptions();

        //set up the center location for the map
        LatLong center = new LatLong(33.7756, -84.3963);

        options.center(center)
                .zoom(16)
                .overviewMapControl(false)
                .panControl(true)
                .rotateControl(true)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(true)
                .mapType(MapTypeIdEnum.HYBRID);

        map = mapView.createMap(options);
        /*
        map.addUIEventHandler(UIEventType.click, (JSObject e) -> {
            Debug.debug("you clicked");
            JSObject clicked = (JSObject) e.getMember("latLng");
            Double clickedLat = (Double) clicked.call("lat");
            Double clickedLng = (Double) clicked.call("lng");
            Debug.debug("here: %f,%f", clickedLat, clickedLng);

            MarkerOptions markerOptions = new MarkerOptions();
            LatLong loc = new LatLong(clickedLat, clickedLng);

            markerOptions.position(loc)
                    .visible(Boolean.TRUE)
                    .title(String.format("%f,%f", clickedLat, clickedLng));

            Marker marker = new Marker(markerOptions);

            map.addUIEventHandler(marker,
                UIEventType.click,
                (JSObject obj) -> {
                    InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                    infoWindowOptions.content("this is the description<br /><h1>test header</h1><br /><a href='#test'>test link</a><br /><a href='http://google.com/'>test external link</a>");

                    InfoWindow window = new InfoWindow(infoWindowOptions);
                    window.open(map, marker);
                }
            );

            map.addMarker(marker);


        });
        */

    }

    @Override
    public void mapInitialized() {
        createMap();
        updateMap();
    }

    /**
     * Updates the map markers
     */
    public void updateMap() {
        if (markerList != null) {
            for (Marker m: markerList) {
                map.removeMarker(m);
            }
        }
        List<WaterReport> reportList = ReportManager.getWaterReportlist();
        markerList = new ArrayList<>(reportList.size());
        markReports(reportList);
    }

    /**
     * Draws all the given reports
     * @param reportList the list of reports to draw 
     */
    public void markReports(List<WaterReport> reportList) {
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
                    infoWindowOptions.content(String.format("<h1>%.5f, %.5f</h1>"
                            + "<h3>Water type: %s<br />"
                            + "Water condition: %s</h3>"
                            + "Report number: %d<br />"
                            + "Reported by: %s<br />"
                            + "Reported at: %s<br />",
                            lat, lng,
                            rr.getWaterType().toString(),
                            rr.getWaterCondition().toString(),
                            rr.getReportNum(),
                            rr.getAuthor().getUsername(),
                            rr.getDateTime().format(DateTimeFormatter.ISO_DATE_TIME)
                    ));

                    InfoWindow window = new InfoWindow(infoWindowOptions);
                    window.open(map, marker);
                }
            );
            map.addMarker(marker);
            markerList.add(marker);
        }

    }



}
