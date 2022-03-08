package softwaredesign.entities;

import com.sothawo.mapjfx.Coordinate;
import org.alternativevision.gpx.beans.Track;
import org.alternativevision.gpx.beans.Waypoint;

import java.util.ArrayList;
import java.util.Date;

public class Metrics {
    private final Date[] timeStamps;
    private final Double[] latitudes;
    private final Double[] longitudes;
    private final Double[] elevations;

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
            if (elevations[i] == null) {
                elevations[i] = elevations[i-1];
            }
        }
    }

    public Double[] getVelocities() {
        int nrWayPoints = timeStamps.length;
        Double[] velocities = new Double[nrWayPoints-1];
        Double[] distances = getDistances();

        // Calculating velocities between every wayPoint seems to yield very volatile values (from 0.1 to 40.2 km/h)
        // Solution: apply a 3-waypoint moving average; velocity[i] = avg velocity from i-1 to i+1
        for (int i = 0; i < nrWayPoints - 1; i++) {
            int from = Math.max(i - 1, 0);
            int to = Math.min(i + 1, nrWayPoints - 1);

            long timeDifferenceMilliSec = timeStamps[to].getTime() - timeStamps[from].getTime();
            Double timeDifferenceSec = timeDifferenceMilliSec / 1000.0;

            Double threePointDistance = 0.0;
            for (int j = from; j < to; j++) {
                threePointDistance += distances[j];
            }
            double velocityMpS = threePointDistance / timeDifferenceSec;  // in Meters per Second
            velocities[i] = velocityMpS * 3.6;                            // Convert m/s to km/h
        }

        return velocities;
    }

    public Double[] getDistances() {
        int nrWayPoints = timeStamps.length;
        Double[] distances = new Double[nrWayPoints-1];
        for (int i = 0; i < nrWayPoints - 1; i++) {
            distances[i] = calculateDistance(i, i+1);
        }
        // distances[i] stores the distance from wayPoint[i] to wayPoint[i+1]
        return distances;
    }

    private Double calculateDistance(int from, int to) {
        final int RADIUS_EARTH = 6371; // in KM

        double latDistance = Math.toRadians(latitudes[to] - latitudes[from]);
        double lonDistance = Math.toRadians(longitudes[from] - longitudes[to]);

        // Haversine formula for calculating distance between geopoints
        double a = Math.pow(Math.sin(latDistance / 2), 2)
                 + Math.pow(Math.sin(lonDistance / 2), 2)
                 * Math.cos(Math.toRadians(latitudes[from])) * Math.cos(Math.toRadians(latitudes[to]));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double horDistanceKM = RADIUS_EARTH * c;
        double horDistanceM = horDistanceKM * 1000; // Convert KM to Meters

        double verDistanceM = elevations[from] - elevations[to];

        // Pythagoras with vertical distance and horizontal distance
        return Math.sqrt( Math.pow(horDistanceM, 2) + Math.pow(verDistanceM, 2) );
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
}
