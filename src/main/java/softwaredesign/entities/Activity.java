package softwaredesign.entities;

import org.alternativevision.gpx.beans.Waypoint;
import softwaredesign.helperclasses.Calc;

import java.util.ArrayList;
import java.util.Date;


public class Activity {

    private final RouteData routeData;
    private final Weather weather;
    private ActivityType type;

    public Activity(ArrayList<Waypoint> wayPoints) {

        routeData = new RouteData(wayPoints);
        type = null;

        if (routeData.getTimeStamps()[0] == null) {
            weather = null;
        } else {
            weather = new Weather(routeData);
        }
    }


    public Double calculateCalories(Profile profile){
        if(type == null) {
            return null;
        }
        return (type.getMET() * profile.getWeight()) * (this.getTotalTimeS() / 3600);
    }


    public void setActivityType(String activityName){
        type = new ActivityTypeFactory().getActivityType(activityName);
    }

    public ActivityType getActivityType(){
        return type;
    }

    public RouteData getRouteData() { return routeData;}

    public Weather getWeather() { return weather; }

    public Double getTotalDistanceM() {
        return Calc.findSum(routeData.getDistances());
    }

    public Double getTotalTimeS() {
        Date[] timeStamps = routeData.getTimeStamps();

        long timeDifferenceMilliSec = timeStamps[timeStamps.length-1].getTime() - timeStamps[0].getTime();
        return timeDifferenceMilliSec / 1000.0;
    }

    public Double getAverageSpeedMpS() { return getTotalDistanceM() / getTotalTimeS(); }

}
