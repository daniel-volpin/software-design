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
    private Double[] elevations;

    public Metrics(Track track) {
        ArrayList<Waypoint> wayPoints = track.getTrackPoints();
        int nrWayPoints = wayPoints.size();

        timeStamps = new Date[nrWayPoints];
        latitudes = new Double[nrWayPoints];
        longitudes = new Double[nrWayPoints];
        elevations = new Double[nrWayPoints];

        for (int i = 0; i < nrWayPoints; i++) {
            Waypoint waypoint = wayPoints.get(i);
            timeStamps[i] = waypoint.getTime();
            latitudes[i] = waypoint.getLatitude();
            longitudes[i] = waypoint.getLongitude();
            elevations[i] = waypoint.getElevation();
        }
    }

    public Double[] getVelocities() {
        int nrWayPoints = timeStamps.length;
        Double[] velocities = new Double[nrWayPoints-1];
        Double[] distances = getDistances();

        for (int i = 0; i < nrWayPoints - 1; i++) {
            long timeDifference_ms = timeStamps[i].getTime() - timeStamps[i+1].getTime();
            // TODO: Convert to km/h
            velocities[i] = distances[i] / timeDifference_ms;
        }
        return velocities;
    }

    public Double[] getDistances() {
        int nrWayPoints = timeStamps.length;
        Double[] distances = new Double[nrWayPoints-1];
        for (int i = 0; i < nrWayPoints - 1; i++) {
            distances[i] = calculateDistance(i, i+1);
        }
        return distances;
    }

    // TODO: calculateDistance
    private Double calculateDistance(int from, int to) {
        // TODO: Haversine formula for calculating distance between geopoints
//        latitudes[from];
//        latitudes[to];
//        longitudes[from];
//        longitudes[to];
        return 0.0;
    }

    public Coordinate[] getCoordinates() {
        Coordinate[] coordinates = new Coordinate[latitudes.length];
        for (int i = 0; i < latitudes.length; i++) {
            coordinates[i] = new Coordinate(latitudes[i], longitudes[i]);
        }
        return coordinates;
    }

    public Date[] getTimeStamps() {
        return timeStamps;
    }

    public Double[] getElevations() {
        return elevations;
    }

    public Double[] getLatitudes() {
        return latitudes;
    }

    public Double[] getLongitudes() {
        return longitudes;
    }

    public static Double findMax(Double[] values) {
        Double maxValue = values[0];
        for (int i = 1; i < values.length; i++){
            if (values[i] > maxValue) {
                maxValue = values[i];
            }
        }
        return maxValue;
    }

    public static Double findMin(Double[] values) {
        Double minValue = values[0];
        for (int i = 1; i < values.length; i++){
            if (values[i] < minValue) {
                minValue = values[i];
            }
        }
        return minValue;
    }
}
