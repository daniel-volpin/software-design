package softwaredesign.entities;

import org.alternativevision.gpx.beans.Track;
import softwaredesign.helperclasses.Calc;


public class Activity {
    private final RouteData routeData;
    private final Weather weather;
    // TODO: private ActivityType type;

    public Activity(Track track) {
        routeData = new RouteData(track);
        weather = new Weather(routeData);
    }

    public RouteData getRouteData() {
        return routeData;
    }

    public String getWeatherImagePath() {return weather.getImagePath(); }

    public Double getWeatherHumidity() {
        return weather.getHumidity();
    }

    public String getWeatherConditions() {
        return weather.getConditions();
    }

    public Double getWeatherTemp() {
        return weather.getTemperature();
    }

    public Double getWeatherWindSpeed() {
        return weather.getWindSpeed();
    }

    public Double getTotalDistance() {
        return Calc.findSum(routeData.getDistances());
    }

}
