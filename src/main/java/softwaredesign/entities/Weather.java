package softwaredesign.entities;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.net.http.HttpClient;
import java.io.*;
import java.util.Date;
import org.json.JSONObject;

public class Weather {

    private String conditions;
    private Double temperature;
    private Double windSpeed;
    private Double humidity;

    public Weather(RouteData routeData){
        Date timeStamp = routeData.getTimeStamps()[0];

        String year = new SimpleDateFormat("yyyy").format(timeStamp);
        String month = new SimpleDateFormat("MM").format(timeStamp);
        String date = new SimpleDateFormat("dd").format(timeStamp);
        String hours = new SimpleDateFormat("HH").format(timeStamp);
        String minutes = new SimpleDateFormat("mm").format(timeStamp);

        Double latitude = routeData.getLatitudes()[0];
        Double longitude = routeData.getLongitudes()[0];

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://visual-crossing-weather.p.rapidapi.com/history?startDateTime="+year+"-"+month+"-"+date+"T"+hours+"%3A"+minutes+"%3A00&aggregateHours=1&location="+latitude+"%2C"+longitude+"&endDateTime="+year+"-"+month+"-"+date+"T"+hours+"%3A"+minutes+"%3A00&unitGroup=metric&contentType=json&shortColumnNames=true"))
                .header("x-rapidapi-host", "visual-crossing-weather.p.rapidapi.com")
                .header("x-rapidapi-key", "0c4cb4457emsh59a9f18425466a7p1045c9jsn08640ab722bc")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject responseJson = new JSONObject(response.body()).getJSONObject("locations").getJSONObject(latitude + "," + longitude).getJSONArray("values").getJSONObject(0);

            conditions = responseJson.getString("conditions");
            temperature = responseJson.getDouble("temp");
            windSpeed = responseJson.getDouble("wspd");
            humidity = responseJson.getDouble("humidity");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getConditions() {return conditions;}
    public Double getTemperature() {return  temperature;}
    public Double getWindSpeed() {return  windSpeed;}
    public Double getHumidity() {return  humidity;}

    public String getImagePath() {
        String imagePath;

        switch (conditions){
            case	"Drizzle":
            case	"Heavy Drizzle":
            case	"Light Drizzle":
            case	"Heavy Drizzle/Rain":
            case	"Light Drizzle/Rain":
            case	"Freezing Drizzle/Freezing Rain":
            case	"Heavy Freezing Drizzle/Freezing Rain":
            case	"Light Freezing Drizzle/Freezing Rain":
            case	"Heavy Freezing Rain":
            case	"Light Freezing Rain":
            case	"Hail":
            case	"Heavy Rain And Snow":
            case	"Light Rain And Snow":
            case	"Rain Showers":
            case	"Heavy Rain":
            case	"Light Rain":
            case	"Precipitation In Vicinity":
            case	"Rain":
            case	"Thunderstorm":
                imagePath = "src/main/resources/Media/umbrella.png";
            break;

            case	"Duststorm":
            case	"Fog":
            case	"Freezing Fog":
            case	"Mist":
            case	"Sky Coverage Increasing":
            case	"Sky Unchanged":
            case	"Smoke Or Haze":
            case	"Diamond Dust":
                imagePath = "src/main/resources/Media/cloudy.png";
            break;

            case	"Funnel Cloud/Tornado":
            case	"Hail Showers":
            case	"Lightning Without Thunder":
            case	"Overcast":
            case	"Partially cloudy":
            case	"Thunderstorm Without Precipitation":
            case	"Squalls":
                imagePath = "src/main/resources/Media/cloudy.png";
            break;

            case	"Heavy Snow":
            case	"Light Snow":
            case	"Snow":
            case	"Snow And Rain Showers":
            case	"Snow Showers":
            case	"Ice":
            case	"Blowing Or Drifting Snow":
                imagePath = "src/main/resources/Media/snow.png";
            break;

            case	"Sky Coverage Decreasing":
            case	"Clear":
                imagePath = "src/main/resources/Media/sunny.png";
            break;

            default:
                imagePath = "src/main/resources/Media/VU-logo.png";

        }
        return imagePath;
    }
}
