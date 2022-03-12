package softwaredesign.entities;

import org.alternativevision.gpx.beans.Track;
import softwaredesign.helperclasses.Calc;
import java.util.Date;


public class Activity {
    private final RouteData routeData;
    private final Weather weather;
    // TODO: private ActivityType type;

    public Activity(Track track) {
        routeData = new RouteData(track);
        weather = new Weather(routeData);
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
