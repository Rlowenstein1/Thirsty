package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.User;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventHandler;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.InfoWindow;
import com.lynden.gmapsfx.javascript.object.InfoWindowOptions;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.LatLongBounds;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import lib.Debug;
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

    private ReadOnlyObjectProperty<LatLongBounds> mapBoundsProperty;
    
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

    @Override
    public void mapInitialized() {

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
        mapBoundsProperty = map.boundsProperty();
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
    }


}
