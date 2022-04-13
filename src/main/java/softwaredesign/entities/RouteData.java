package softwaredesign.entities;

import com.sothawo.mapjfx.Coordinate;
import org.alternativevision.gpx.beans.Waypoint;
import softwaredesign.helperclasses.Calc;

import java.util.ArrayList;
import java.util.Date;

public class RouteData {
    private final Date[] timeStamps;
    private final Double[] latitudes;
    private final Double[] longitudes;
    private final Double[] elevations;

    public RouteData(ArrayList<Waypoint> wayPoints) {
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
            if (elevations[i] == null && i>0) {
                elevations[i] = elevations[i-1];
            }
        }
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

        if (elevations[from] == null || elevations[to] == null) {
            return horDistanceM;
        } else {
            // Do Pythagoras with vertical distance and horizontal distance
            double verDistanceM = elevations[from] - elevations[to];
            return Math.sqrt(Math.pow(horDistanceM, 2) + Math.pow(verDistanceM, 2));
        }
    }

    public Coordinate[] getCoordinates() {
        Coordinate[] coordinates = new Coordinate[latitudes.length];
        for (int i = 0; i < latitudes.length; i++) {
            coordinates[i] = new Coordinate(latitudes[i], longitudes[i]);
        }
        return coordinates;
    }

    public Coordinate[] getRouteExtent() {
        /** Find minimum and maximum latitude and longitude coordinates*/
        Double maxLat = Calc.findMax(latitudes);
        Double minLat = Calc.findMin(latitudes);
        Double maxLon = Calc.findMax(longitudes);
        Double minLon = Calc.findMin(longitudes);

        Double marginPercentage = 0.4;
        Double latMargin = (maxLat - minLat) * marginPercentage;
        Double lonMargin = (maxLat - minLat) * marginPercentage;

        Double[] extremeLats = {minLat - latMargin, maxLat + latMargin};
        Double[] extremeLons = {minLon - lonMargin, maxLon + lonMargin};

        Coordinate[] extentCoordinates = new Coordinate[4];
        for (int i = 0; i < extremeLats.length; i++) {
            for (int j = 0; j < extremeLons.length; j++) {
                extentCoordinates[i*extremeLons.length + j] = new Coordinate(extremeLats[i], extremeLons[j]);
            }
        }
        return extentCoordinates;
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
