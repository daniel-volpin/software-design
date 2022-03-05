package softwaredesign;

import com.gluonhq.attach.position.Position;
import com.gluonhq.attach.position.PositionService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.attach.util.Services;

import com.gluonhq.*;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MobileSample extends Application {

    private static final Logger LOGGER = Logger.getLogger(MobileSample.class.getName());

    static {
        try {
            LogManager.getLogManager().readConfiguration(MobileSample.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading logging properties file", e);
        }
    }

    private MapPoint mapPoint;

    @Override
    public void start(Stage stage) {
        MapView view = new MapView();
        view.addLayer(positionLayer());
        view.setZoom(3);
        Scene scene;
        final Label headerLabel = headerLabel();
        final Group copyright = createCopyright();
        StackPane bp = new StackPane() {
            @Override
            protected void layoutChildren() {
                super.layoutChildren();
                headerLabel.setLayoutY(0.0);
                copyright.setLayoutX(getWidth() - copyright.prefWidth(-1));
                copyright.setLayoutY(getHeight() - copyright.prefHeight(-1));
            }
        };
        bp.getChildren().addAll(view, headerLabel, copyright);
        if (Platform.isDesktop()) {
            headerLabel.setManaged(false);
            headerLabel.setVisible(false);
            scene = new Scene(bp, 600, 700);
            stage.setTitle("Gluon Maps Demo");
        } else {
            headerLabel.setManaged(true);
            headerLabel.setVisible(true);
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            scene = new Scene(bp, bounds.getWidth(), bounds.getHeight());
        }
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        view.flyTo(1., mapPoint, 2.);
    }

    private Label headerLabel() {
        final Label header = new Label("Gluon Maps Demo");
        header.getStyleClass().add("header");
        return header;
    }

    private Group createCopyright() {
        final Label copyright = new Label(
                "Map data © OpenStreetMap contributors, CC-BY-SA.\n" +
                        "Imagery  © OpenStreetMap, for demo only."
        );
        copyright.getStyleClass().add("copyright");
        copyright.setAlignment(Pos.CENTER);
        copyright.setMaxWidth(Double.MAX_VALUE);
        return new Group(copyright);
    }

    private MapLayer positionLayer() {
        return Services.get(PositionService.class)
                .map(positionService -> {
                    positionService.start();

                    ReadOnlyObjectProperty<Position> positionProperty = positionService.positionProperty();
                    Position position = positionProperty.get();
                    if (position == null) {
                        position = new Position(50., 4.);
                    }
                    mapPoint = new MapPoint(position.getLatitude(), position.getLongitude());
                    LOGGER.log(Level.INFO, "Initial Position: " + position.getLatitude() + ", " + position.getLongitude());

                    PoiLayer answer = new PoiLayer();
                    answer.addPoint(mapPoint, new Circle(7, Color.RED));

                    positionProperty.addListener(e -> {
                        Position pos = positionProperty.get();
                        LOGGER.log(Level.INFO, "New Position: " + pos.getLatitude() + ", " + pos.getLongitude());
                        mapPoint.update(pos.getLatitude(), pos.getLongitude());
                    });
                    return answer;
                })
                .orElseGet(() -> {
                    LOGGER.log(Level.WARNING, "Position Service not available");
                    PoiLayer answer = new PoiLayer();
                    mapPoint = new MapPoint(50., 4.);
                    answer.addPoint(mapPoint, new Circle(7, Color.RED));
                    return answer;
                });
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}