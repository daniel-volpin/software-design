package softwaredesign.controllers;

import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapViewEvent;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import softwaredesign.entities.Activity;
import softwaredesign.entities.RouteData;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;


public class MainSceneController {

    /** logger for the class. */
    private static final Logger logger = LoggerFactory.getLogger(MainSceneController.class);

    /** Coordinate of Amsterdam city centre. */
    private static Coordinate centerPoint = new Coordinate(52.3717204, 4.9020727);

    /** default zoom value. */
    private static final int ZOOM_DEFAULT = 14;

    /** the markers. */
    private final Marker markerClick;

    /** For removing the trackLine if a new file is uploaded */
    private CoordinateLine shownTrackLine;

    // TODO: Profile
    private ArrayList<Activity> activityHistory;

    /** Menu buttons*/
    @FXML private Button profileBtn;
    @FXML private Button settingsBtn;
    @FXML private Button GPXBtn;

    /** button to set the map's zoom. */
    @FXML private Button buttonZoom;

    /** the MapView containing the map */
    @FXML private MapView mapView;

    /** the box containing the top controls, must be enabled when mapView is initialized */
    @FXML private HBox topControls;

    /** Slider to change the zoom value */
    @FXML private Slider sliderZoom;


    @FXML public VBox rightSideVBox;
    @FXML public BorderPane borderPane;


    public MainSceneController() {
        markerClick = Marker.createProvided(Marker.Provided.ORANGE).setVisible(false);
    }

    /**
     * called after the fxml is loaded and all objects are created. This is not called initialize any more,
     * because we need to pass in the projection before initializing.
     *
     * @param projection
     *     the projection to use in the map.
     */
    public void initMapAndControls(Projection projection) {
        logger.trace("begin initialize");

        // set the controls to disabled, this will be changed when the MapView is intialized
        setControlsDisable(true);

        // wire the zoom button and connect the slider to the map's zoom
        buttonZoom.setOnAction(event -> mapView.setZoom(ZOOM_DEFAULT));
        sliderZoom.valueProperty().bindBidirectional(mapView.zoomProperty());

        // watch the MapView's initialized property to finish initialization
        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                afterMapIsInitialized();
            }
        });

        setupEventHandlers();

        logger.trace("start map initialization");
        mapView.initialize(Configuration.builder()
                .projection(projection)
                .showZoomControls(false)
                .build());
        logger.debug("initialization finished");
    }

    /**
     * initializes the event handlers.
     */
    private void setupEventHandlers() {
        // add an event handler for singleclicks, set the click marker to the new position when it's visible
        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            event.consume();
            final Coordinate newPosition = event.getCoordinate().normalize();

            if (markerClick.getVisible()) {
                final Coordinate oldPosition = markerClick.getPosition();
                if (oldPosition != null) {
                    animateClickMarker(oldPosition, newPosition);
                } else {
                    markerClick.setPosition(newPosition);
                    // adding can only be done after coordinate is set
                    mapView.addMarker(markerClick);
                }
            }
        });

        // add an event handler for MapViewEvent#MAP_EXTENT and set the extent in the map
        mapView.addEventHandler(MapViewEvent.MAP_EXTENT, event -> {
            event.consume();
            mapView.setExtent(event.getExtent());
        });

//        mapView.addEventHandler(MapViewEvent.MAP_POINTER_MOVED, event -> logger.debug("pointer moved to " + event.getCoordinate()));
        logger.trace("map handlers initialized");
    }

    private void animateClickMarker(Coordinate oldPosition, Coordinate newPosition) {
        // animate the marker to the new position
        final Transition transition = new Transition() {
            private final Double oldPositionLongitude = oldPosition.getLongitude();
            private final Double oldPositionLatitude = oldPosition.getLatitude();
            private final double deltaLatitude = newPosition.getLatitude() - oldPositionLatitude;
            private final double deltaLongitude = newPosition.getLongitude() - oldPositionLongitude;

            {
                setCycleDuration(Duration.seconds(1.0));
                setOnFinished(evt -> markerClick.setPosition(newPosition));
            }

            @Override
            protected void interpolate(double v) {
                final double latitude = oldPosition.getLatitude() + v * deltaLatitude;
                final double longitude = oldPosition.getLongitude() + v * deltaLongitude;
                markerClick.setPosition(new Coordinate(latitude, longitude));
            }
        };
        transition.play();
    }

    /**
     * enables / disables the different controls
     *
     * @param flag
     *     if true the controls are disabled
     */
    private void setControlsDisable(boolean flag) {
        topControls.setDisable(flag);
    }

    /**
     * finishes setup after the mpa is initialzed
     */
    private void afterMapIsInitialized() {
        logger.trace("map initialized");
        logger.debug("setting center and enabling controls...");

        // start at the harbour with default zoom
        mapView.setZoom(ZOOM_DEFAULT);
        mapView.setCenter(centerPoint);

        // now enable the controls
        setControlsDisable(false);
    }

    private void initializeActivity(Track track) {
        Activity newActivity = new Activity(track);
        addActivity(newActivity);
        changeShownActivity(newActivity);

        Double totalDistance = newActivity.getTotalDistance();
        totalDistance = (double) (Math.round(totalDistance * 100) / 100);
        Label totalDistanceLabel = new Label();
        totalDistanceLabel.setText("Total Distance: " + totalDistance + "m");

        Double temp = newActivity.getWeatherTemp();
        Label tempLabel = new Label();
        tempLabel.setText("Temperature: " + temp);

        Double humidity = newActivity.getWeatherHumidity();
        Label humidityLabel = new Label();
        humidityLabel.setText("Humidity: " + humidity + "%");

        Double windSpeed = newActivity.getWeatherHumidity();
        Label windSpeedLabel = new Label();
        windSpeedLabel.setText("Wind Speed: " + windSpeed + "km/h");

        String conditions = newActivity.getWeatherConditions();
        Label conditionsLabel = new Label();
        conditionsLabel.setText("Weather Condition: " + conditions);

        String weatherImagePath = newActivity.getWeatherImagePath();
        ImageView imageView = null;
        try {
            Image image = new Image(new FileInputStream(weatherImagePath));
            imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        FXMLLoader rightSideLoader = new FXMLLoader(getClass().getResource("/Scenes/rightSide.fxml"));
        try {
            rightSideVBox = rightSideLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        conditionsLabel.setWrapText(true);

        rightSideVBox.getChildren().addAll(totalDistanceLabel, tempLabel, windSpeedLabel, humidityLabel, conditionsLabel, imageView);
        borderPane.setRight(rightSideVBox);

    }

    private void addActivity(Activity newActivity) {
        if (activityHistory == null) {
            activityHistory = new ArrayList<>();
        }
        activityHistory.add(newActivity);
    }

    private void changeShownActivity(Activity newActivity) {
        RouteData routeData = newActivity.getRouteData();

        /** Make a CoordinateLine for plotting */
        CoordinateLine trackLine = new CoordinateLine(routeData.getCoordinates());
        trackLine.setColor(Color.ORANGERED).setVisible(true);

        if (shownTrackLine != null) {
            mapView.removeCoordinateLine(shownTrackLine);
        }
        mapView.addCoordinateLine(trackLine);
        shownTrackLine = trackLine;

        /** Set the extent of the map. Assumes the window is still its original size of when the window first opened */
        Coordinate[] routeExtent = routeData.getRouteExtent();
        mapView.setExtent(Extent.forCoordinates(routeExtent));
    }

    private Track getTrackFromFile(FileInputStream gpxFile) throws ParserConfigurationException, IOException, SAXException {
        GPXParser p = new GPXParser();
        GPX gpx = p.parseGPX(gpxFile);
        HashSet<Track> tracks = gpx.getTracks();

        // TODO: Multiple tracks --> trackHistory?
        if (tracks.size() == 1) {
            Track[] trackArray = tracks.toArray(new Track[0]);
            return trackArray[0];
        } else {
            throw new IOException("Please provide a GPX File with exactly one Track");
        }
    }

    @FXML
    private void openGPXFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("GPX Files", "*.gpx"));

        GPXBtn.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(null);
            try {
                FileInputStream gpxFile = new FileInputStream(file);
                Track track = getTrackFromFile(gpxFile);
                initializeActivity(track);
            } catch (java.io.FileNotFoundException e) {
                // TODO: Write the error to the screen such that the user knows something went wrong
                logger.info("ERROR: GPX file not found");
            } catch (Exception e) {
                // TODO: Write the error to the screen such that the user knows something went wrong
                logger.info("ERROR: " + e.getMessage());
            }
        });

    }

    public void GPXBtnEntered(MouseEvent mouseEvent) {
        GPXBtn.setStyle("-fx-background-color: #d3bbdd");
    }

    public void profileBtnEntered(MouseEvent mouseEvent) {
        profileBtn.setStyle("-fx-background-color: #d3bbdd");
    }

    public void settingsBtnEntered(MouseEvent mouseEvent) {
        settingsBtn.setStyle("-fx-background-color: #d3bbdd");
    }

    public void GPXBtnExited(MouseEvent mouseEvent) {
        GPXBtn.setStyle("-fx-background-color: #C1BBDD");
    }

    public void profileBtnExited(MouseEvent mouseEvent) {
        profileBtn.setStyle("-fx-background-color: #C1BBDD");
    }
    public void settingsBtnExited(MouseEvent mouseEvent) {
        settingsBtn.setStyle("-fx-background-color: #C1BBDD");
    }


}