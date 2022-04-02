package softwaredesign.controllers;

import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapViewEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Route;
import org.alternativevision.gpx.beans.Track;
import org.alternativevision.gpx.beans.Waypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import softwaredesign.entities.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MainSceneController {

    /** logger for the class. */
    private static final Logger logger = LoggerFactory.getLogger(MainSceneController.class);

    /** Coordinate of Amsterdam city centre. */
    private static final Coordinate centerPoint = new Coordinate(52.3717204, 4.9020727);

    /** default zoom value. */
    private static final int ZOOM_DEFAULT = 14;

    /** For removing the trackLine if a new file is uploaded */
    private CoordinateLine shownTrackLine;

    private ArrayList<Activity> activityHistory;
    private Activity currentActivity = null;
    private final ArrayList<TitledPane> titledPaneActivities = new ArrayList<>();
    Profile profile = null;

    /** boolean to update the routeData information */
    private boolean metricOn = true;

    /** button to set the map's zoom. */
    @FXML private Button buttonZoom;

    /** the MapView containing the map */
    @FXML private MapView mapView;

    /** the box containing the top controls, must be enabled when mapView is initialized */
    @FXML private HBox topControls;

    /** Slider to change the zoom value */
    @FXML private Slider sliderZoom;

    /** Menu elements*/
    @FXML private Button okBtn;
    @FXML private Button activityBtn;
    @FXML private Button GPXBtn;
    @FXML private VBox activityTypeSelection;
    @FXML private ChoiceBox<String> activityChoiceBox;

    /** dynamic right side pane that is loaded when an activity file is uploaded*/
    @FXML private VBox rightSideVBox;
    @FXML private BorderPane borderPane;
    @FXML private Button retractBtn;

    /** check boxes to change route data from metric to imperial and vise versa. */
    @FXML private CheckBox imperialCheckBox;
    @FXML private CheckBox metricCheckBox;

    /** labels for metric and imperial unit change*/
    @FXML private Label metricLabel;
    @FXML private Label imperialLabel;

    /** anchor pane to display the activity history*/
    @FXML private AnchorPane activityAnchorPane;

    /** Profile  elements*/
    @FXML private VBox profileElements;
    @FXML private TextField heightTextField;
    @FXML private TextField weigthTextField;
    @FXML private TextField ageTextField;
    @FXML private Button confirmProfileBtn;

    public MainSceneController() { }

    /**
     * called after the fxml is loaded and all objects are created. This is not called initialize anymore,
     * because we need to pass in the projection before initializing.
     *
     * @param projection
     *     the projection to use in the map.
     */
    public void initMapAndControls(Projection projection) {
        logger.trace("begin initialize");

        // set the controls to disabled, this will be changed when the MapView is initialized
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
        // add an event handler for MapViewEvent#MAP_EXTENT and set the extent in the map
        mapView.addEventHandler(MapViewEvent.MAP_EXTENT, event -> {
            event.consume();
            mapView.setExtent(event.getExtent());
        });
        logger.trace("map handlers initialized");
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
     * finishes setup after the mpa is initialized
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

    private ArrayList<Label> makeRouteDataLabels(Activity activity) {
        ArrayList<Label> routeDataLabels = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");

        double totalDistanceM = Math.round(activity.getTotalDistanceM() * 100) / 100.0;

        Label totalDistanceLabel = new Label();
        Label avgSpeedLabel = new Label();
        Label totalTimeLabel = new Label();

        try {
            // If the GPX file does not have time-data, these function calls will fail:
            double totalTimeM = Math.round(activity.getTotalTimeS() / 60.0 * 100) / 100.0;
            double avgSpeedKMpS = Math.round(activity.getAverageSpeedMpS() * 3.6 * 100) / 100.0;

            if (!metricOn) {
                totalDistanceM = totalDistanceM * 0.0006214;
                totalDistanceM = Double.parseDouble(df.format(totalDistanceM));
                totalDistanceLabel.setText("Total Distance: " + totalDistanceM + "mi");

                avgSpeedKMpS = avgSpeedKMpS * 0.6214;
                avgSpeedKMpS = Double.parseDouble(df.format(avgSpeedKMpS));
                avgSpeedLabel.setText("Average Speed: " + avgSpeedKMpS + " miles/hr");
            } else {
                totalTimeM = Double.parseDouble(df.format(totalTimeM));
                totalDistanceLabel.setText("Total Distance: " + totalDistanceM + "m");
                avgSpeedKMpS = Double.parseDouble(df.format(avgSpeedKMpS));
                avgSpeedLabel.setText("Average Speed: " + avgSpeedKMpS + " km/hr");
            }

            totalTimeLabel.setText("Total Duration: " + totalTimeM + " min");

            routeDataLabels.add(totalTimeLabel);
            routeDataLabels.add(avgSpeedLabel);

        } catch (Exception e) {
            logger.trace(e.toString());
        }

        routeDataLabels.add(totalDistanceLabel);

        return routeDataLabels;
    }

    private ImageView getWeatherImage(Weather weather) {
        if (weather == null) {
            return null;
        }
        ImageView weatherImage = null;

        try {
            String weatherImagePath = weather.getImagePath();
            Image image = new Image(new FileInputStream(weatherImagePath));
            weatherImage = new ImageView(image);
            weatherImage.setFitWidth(100);
            weatherImage.setPreserveRatio(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return weatherImage;
    }

    private ArrayList<Label> makeWeatherLabels(Weather weather) {
        if (weather == null) {
            return null;
        }

        ArrayList<Label> weatherLabels = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");

        Double temp = weather.getTemperature();
        Double humidity = weather.getHumidity();
        Double windSpeed = weather.getWindSpeed();
        String conditions = weather.getConditions();

        Label tempLabel = new Label();
        Label humidityLabel = new Label();
        Label windSpeedLabel = new Label();
        Label conditionsLabel = new Label();

        if (!metricOn) {
            temp = ((9.0/5.0) * temp + 32);
            temp = Double.valueOf(df.format(temp));
            tempLabel.setText("Temperature: " + temp + "\u00B0F");

            windSpeed = windSpeed * 0.6214;
            windSpeed = Double.valueOf(df.format(windSpeed));
            windSpeedLabel.setText("Wind Speed: " + windSpeed + " miles/hr");
        } else {
            tempLabel.setText("Temperature: " + temp + "\u00B0C");
            windSpeedLabel.setText("Wind Speed: " + windSpeed + " km/hr");
        }

        humidityLabel.setText("Humidity: " + humidity + "%");
        conditionsLabel.setText("Weather Condition: " + conditions);

        weatherLabels.add(tempLabel);
        weatherLabels.add(humidityLabel);
        weatherLabels.add(windSpeedLabel);

        conditionsLabel.setWrapText(true);
        weatherLabels.add(conditionsLabel);

        return weatherLabels;
    }

    private void initializeActivity(ArrayList<Waypoint> wayPoints) {
        Activity newActivity = new Activity(wayPoints);
        currentActivity = newActivity;
        addActivity(newActivity);
        changeShownActivity(newActivity);
        makeRightPane(newActivity);
        enableActivityTypeSelection();
        enableProfileDataInput();
    }

    private void enableActivityTypeSelection(){
        List<String> activityNames = ActivityTypeFactory.enumValuesToString();//{"Walking", "Running", "Cycling", "Roller Skating"};
        if(activityChoiceBox.isDisabled()){
            activityChoiceBox.getItems().addAll(activityNames);
        }
        activityTypeSelection.setDisable(false);
    }

    private void enableProfileDataInput(){
        profileElements.setDisable(false);
    }

    private void makeRightPane(Activity newActivity) {
        ArrayList<Label> routeDataLabels = makeRouteDataLabels(newActivity);
        ArrayList<Label> weatherLabels = makeWeatherLabels(newActivity.getWeather());
        ImageView weatherImage = getWeatherImage(newActivity.getWeather());

        Label separatorLabel = new Label();
        separatorLabel.setText("\n\n");

        FXMLLoader rightSideLoader = new FXMLLoader(getClass().getResource("/Scenes/rightPane.fxml"));
        try {
            rightSideVBox = rightSideLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Label rightPaneLabel = new Label("Route Information");
        rightPaneLabel.setStyle("-fx-color-label-visible: false; -fx-font-size: large; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 0 0 10 0");

        HBox titleBox = new HBox();
        titleBox.setStyle("-fx-alignment: center");
        titleBox.getChildren().add(rightPaneLabel);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 0, 0, 10));

        vBox.getChildren().addAll(routeDataLabels);
        vBox.getChildren().add(separatorLabel);

        if (weatherLabels != null) {
            vBox.getChildren().addAll(weatherLabels);
        }
        if (weatherImage != null) {
            vBox.getChildren().add(weatherImage);
        }

        if (profile == null || newActivity.getActivityType() == null) {
            Button calculateCaloriesBtn = makeCaloriesButton();
            vBox.getChildren().add(calculateCaloriesBtn);
        } else {
            Double calories = newActivity.calculateCalories(profile);
            if (calories != null) {
                Label caloriesLabel = new Label();
                caloriesLabel.setText("Total calories burned: " + Math.round(calories));
                caloriesLabel.setWrapText(true);
                vBox.getChildren().add(caloriesLabel);
            }            
        }

        rightSideVBox.getChildren().add(titleBox);
        rightSideVBox.getChildren().add(vBox);

        borderPane.setRight(rightSideVBox);
    }

    private Button makeCaloriesButton() {
        Button calculateCaloriesBtn = new Button("Calculate Calories");

        calculateCaloriesBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            calculateCalories();
                });

        calculateCaloriesBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            calculateCaloriesBtn.setStyle("-fx-background-color: white; -fx-font-weight: bold; -fx-font-size: larger");
                });

        calculateCaloriesBtn.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            calculateCaloriesBtn.setStyle("-fx-background-color: #C1BBDD;");
                });
        calculateCaloriesBtn.setStyle("-fx-background-color: #C1BBDD; -fx-end-margin: 20");
        return calculateCaloriesBtn;
    }


    private void addActivityPane() {
        TitledPane titledPane = new TitledPane();
        VBox vBox = new VBox();

        if (!activityHistory.isEmpty()) {
            Label totDistanceLabel = new Label("Distance: " + activityHistory.get(activityHistory.size()-1).getTotalDistanceM() + " m");
            vBox.getChildren().add(totDistanceLabel);

            try {
                Label avgSpeedLabel = new Label("Avg Speed: " + activityHistory.get(activityHistory.size()-1).getAverageSpeedMpS() + " km/hr");
                Label totTimeLabel = new Label("Time: " + activityHistory.get(activityHistory.size()-1).getTotalTimeS() + " min");


                vBox.getChildren().add(avgSpeedLabel);
                vBox.getChildren().add(totTimeLabel);
            } catch (Exception e) {
                logger.trace(e.toString());
            }

            int index = activityHistory.size()-1;
            Button button = new Button("Open");

            button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                    button.setStyle("-fx-background-color: white; -fx-font-weight: bold; -fx-font-size: larger");
                }
            );

            button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                        button.setStyle("-fx-background-color: #C1BBDD;");
                    }
            );

            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    changeShownActivity(activityHistory.get(index));
                    makeRightPane(activityHistory.get(index));
                }
            );

            button.setStyle("-fx-background-color: #C1BBDD; -fx-end-margin: 20");
            vBox.getChildren().add(button);
        }

        vBox.setStyle("-fx-background-color: #d3bbdd");

        titledPane.setText("Activity " + activityHistory.size());
        titledPane.setContent(vBox);
        titledPane.setStyle("-fx-background-color: #d3bbdd");
        titledPaneActivities.add(titledPane);
    }

    private void addActivity(Activity newActivity) {
        if (activityHistory == null) {
            activityHistory = new ArrayList<>();
        }
        activityHistory.add(newActivity);
        addActivityPane();
    }

    private void changeShownActivity(Activity newActivity) {
        RouteData routeData = newActivity.getRouteData();

        /** Make a CoordinateLine for plotting */
        CoordinateLine trackLine = new CoordinateLine(routeData.getCoordinates());
        trackLine.setColor(Color.ORANGERED).setVisible(true).setWidth(4);

        if (shownTrackLine != null) {
            mapView.removeCoordinateLine(shownTrackLine);
        }
        mapView.addCoordinateLine(trackLine);
        shownTrackLine = trackLine;

        /** Set the extent of the map. Assumes the window is still its original size of when the window first opened */
        Coordinate[] routeExtent = routeData.getRouteExtent();
        mapView.setExtent(Extent.forCoordinates(routeExtent));
    }

    private ArrayList<Waypoint> getWayPointsFromFile(FileInputStream gpxFile) throws ParserConfigurationException, IOException, SAXException {
        GPXParser p = new GPXParser();
        GPX gpx = p.parseGPX(gpxFile);
        HashSet<Route> routes = gpx.getRoutes();
        HashSet<Track> tracks = gpx.getTracks();

        if (routes != null && routes.size() == 1) {
            Route[] routeArray = routes.toArray(new Route[0]);
            return routeArray[0].getRoutePoints();
        } else if (tracks != null && tracks.size() == 1) {
            Track[] trackArray = tracks.toArray(new Track[0]);
            return trackArray[0].getTrackPoints();
        } else {
            throw new IOException("Please provide a GPX File with exactly one Track or Route");
        }
    }

    private void showActivityHistory() {
        Accordion accordion = new Accordion();
        accordion.setPrefWidth(200);
        accordion.getPanes().addAll(titledPaneActivities);

        activityAnchorPane.getChildren().addAll(accordion);
    }

    private void calculateCalories(){
        if (profile == null) {
            profileDataPrompt();
            return;
        }
        if (currentActivity.getActivityType() == null) {
            activityTypePrompt();
            return;
        }
        makeRightPane(currentActivity);
    }

    @FXML private void openGPXFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("GPX Files", "*.gpx"));

        File file = fileChooser.showOpenDialog(null);
        try {
            FileInputStream gpxFile = new FileInputStream(file);
            ArrayList<Waypoint> wayPoints = getWayPointsFromFile(gpxFile);
            initializeActivity(wayPoints);
        } catch (java.io.FileNotFoundException e) {
            // TODO: Write the error to the screen such that the user knows something went wrong
            logger.info("ERROR: GPX file not found");
        } catch (Exception e) {
            // TODO: Write the error to the screen such that the user knows something went wrong
            logger.info("ERROR: " + e.getMessage());
        }

    }
    @FXML private void openActivityHistoryPane() {
        FXMLLoader activityLoader = new FXMLLoader(getClass().getResource("/Scenes/activityHistoryScene.fxml"));

        try {
            activityAnchorPane = activityLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        showActivityHistory();
        borderPane.setRight(activityAnchorPane);
    }

    @FXML private void activityTypePrompt(){
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Scenes/activityTypePrompt.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Provide Activity Type");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void profileDataPrompt(){
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Scenes/profilePrompt.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Provide Profile");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void profileConfirmed(){
        double height = Double.parseDouble(heightTextField.getText());
        double weight = Double.parseDouble(weigthTextField.getText());
        int age = Integer.parseInt(ageTextField.getText());

        profile = new Profile(height, weight, age);
        System.out.println(profile.getAge());
        System.out.println(profile.getWeight());
        System.out.println(profile.getHeight());
    }


    @FXML private void retractBtnClicked() {
        rightSideVBox.setPrefSize(0,0);
    }
    @FXML private void retractBtnEntered() { retractBtn.setStyle("-fx-background-color: #d3bbdd; -fx-font-size: x-large"); }
    @FXML private void GPXBtnEntered() {GPXBtn.setStyle("-fx-background-color: #d3bbdd; -fx-font-size: x-large");}
    @FXML private void profileBtnEntered() {okBtn.setStyle("-fx-background-color: #d3bbdd; -fx-font-size: x-large");}
    @FXML private void activityBtnEntered() { activityBtn.setStyle("-fx-background-color: #d3bbdd; -fx-font-size: x-large");}

    @FXML private void retractBtnExited() { retractBtn.setStyle("-fx-background-color: #C1BBDD"); }
    @FXML private void GPXBtnExited() { GPXBtn.setStyle("-fx-background-color: #C1BBDD");}
    @FXML private void profileBtnExited() { okBtn.setStyle("-fx-background-color: #C1BBDD");}
    @FXML private void activityBtnExited() {activityBtn.setStyle("-fx-background-color: #C1BBDD");}

    @FXML private void metricCheckBoxTicked() {
        metricLabel.setStyle("-fx-text-fill: white; -fx-padding: 5; -fx-font-size: large; -fx-font-weight: bold");
        metricCheckBox.setDisable(true);

        imperialLabel.setStyle("-fx-text-fill: white; -fx-padding: 5; -fx-font-size: large; -fx-font-weight: normal");
        imperialCheckBox.setDisable(false);
        imperialCheckBox.setSelected(false);

        metricOn = true;
        if (currentActivity != null) makeRightPane(currentActivity);
    }
    @FXML private void imperialCheckBoxTicked() {
        imperialLabel.setStyle("-fx-text-fill: white; -fx-padding: 5; -fx-font-size: large; -fx-font-weight: bold");
        imperialCheckBox.setDisable(true);

        metricLabel.setStyle("-fx-text-fill: white; -fx-padding: 5; -fx-font-size: large; -fx-font-weight: normal");
        metricCheckBox.setDisable(false);
        metricCheckBox.setSelected(false);

        metricOn = false;
        if (currentActivity != null) makeRightPane(currentActivity);
    }

    @FXML private void promptOK() {
        Stage stage = (Stage) okBtn.getScene().getWindow();
        stage.close();
    }
    
    @FXML private void activityTypeSelected() {
        currentActivity.setActivityType(activityChoiceBox.getValue());
        System.out.println(currentActivity.getActivityType().getName());
    }

}