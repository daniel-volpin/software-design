package softwaredesign.entities;

import org.alternativevision.gpx.beans.Track;
import softwaredesign.helperclasses.Calc;


public class Activity {
    private Metrics routeData;
    private String name;
    private int activityID;

    public Activity(Track track) {
        routeData = new Metrics(track);

        // Testing distances calculation; Remove this
        Double[] distances = routeData.getDistances();
        System.out.println("\n\nTOTAL DISTANCE TRAVELLED:");
        System.out.println(Calc.findSum(distances));
        System.out.println("\n\n");
    }

    public Metrics getRouteData() {
        return routeData;
    }






}
