package softwaredesign.entities;

import org.alternativevision.gpx.beans.Track;
import softwaredesign.helperclasses.Calc;


public class Activity {
    private RouteData routeData;
    private String name;
    private int activityID;

    public Activity(Track track) {
        routeData = new RouteData(track);

        // Testing distances calculation; Remove this
        System.out.printf("\n\nTOTAL DISTANCE TRAVELLED:\n%.2f m\n\n", Calc.findSum(routeData.getDistances()));
    }

    public RouteData getRouteData() {
        return routeData;
    }






}
