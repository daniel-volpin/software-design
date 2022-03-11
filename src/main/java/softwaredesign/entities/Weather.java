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
    private double temperature;
    private double windSpeed;
    private double humidity;

    public Weather(RouteData routeData){
        Date timeStamp = routeData.getTimeStamps()[0];

        String year = new SimpleDateFormat("yyyy").format(timeStamp);
        String month = new SimpleDateFormat("MM").format(timeStamp);
        String date = new SimpleDateFormat("dd").format(timeStamp);
        String hours = new SimpleDateFormat("HH").format(timeStamp);
        String minutes = new SimpleDateFormat("mm").format(timeStamp);

        double latitude = routeData.getLatitudes()[0];
        double longitude = routeData.getLongitudes()[0];

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
    public double getTemperature() {return  temperature;}
    public double getWindSpeed() {return  windSpeed;}
    public double getHumidity() {return  humidity;}
}
