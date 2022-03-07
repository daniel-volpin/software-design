package softwaredesign.entities;

import com.sothawo.mapjfx.Coordinate;
import org.alternativevision.gpx.beans.Track;
import org.alternativevision.gpx.beans.Waypoint;

import java.util.ArrayList;
import java.util.Date;

public class Metrics {
    private Date[] timeStamps;
    private Double[] latitudes;
    private Double[] longitudes;
    private Double[] velocities;
    private Double[] elevations;

    public Metrics(Track track) {
        ArrayList<Waypoint> wayPoints = track.getTrackPoints();
        int nrWayPoints = wayPoints.size();

        timeStamps = new Date[nrWayPoints];
        latitudes  = new Double[nrWayPoints];
        longitudes = new Double[nrWayPoints];
        elevations = new Double[nrWayPoints];

        for (int i = 0; i < nrWayPoints; i++) {
            Waypoint waypoint = wayPoints.get(i);
            timeStamps[i] = waypoint.getTime();
            latitudes[i] = waypoint.getLatitude();
            longitudes[i] = waypoint.getLongitude();
            elevations[i] = waypoint.getElevation();
        }

        velocities = new Double[nrWayPoints-1];
        for (int i = 0; i < nrWayPoints - 1; i++) {
            velocities[i] = calculateSpeed();
        }
    }

    public Double[] getLatitudes() {
        return this.latitudes;
    }
    public Double[] getLongitudes() {
        return this.longitudes;
    }


    public Coordinate[] getCoordinates() {
        Coordinate[] coordinates = new Coordinate[latitudes.length];
        for (int i = 0; i < latitudes.length; i++) {
            coordinates[i] = new Coordinate(latitudes[i], longitudes[i]);
        }
        return coordinates;
    }

    // TODO: calculateSpeed
    private Double calculateSpeed() {
        return 0.0;
    }

}
