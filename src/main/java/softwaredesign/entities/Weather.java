package softwaredesign.entities;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.net.http.HttpClient;
import java.io.*;
import java.util.Date;

public class Weather {

    private String conditions;
    private double tempreture;
    private double windSpeed;
    private double humidity;

    public Weather(Metrics metrics){

        Date timeStamp = metrics.getTimeStamps()[0];

        String date,month,hours,minutes, year;

        double latitude,longitude;

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

        year = yearFormat.format(timeStamp);
        month = monthFormat.format(timeStamp);
        date = dayFormat.format(timeStamp);
        hours = hourFormat.format(timeStamp);
        minutes = minuteFormat.format(timeStamp);

        latitude = metrics.getLatitudes()[0];
        longitude = metrics.getLongitudes()[0];

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("https://visual-crossing-weather.p.rapidapi.com/history?startDateTime="+year+"-"+month+"-"+date+"T"+hours+"%3A"+minutes+"%3A00&aggregateHours=1&location="+latitude+"%2C"+longitude+"&endDateTime="+year+"-"+month+"-"+date+"T"+hours+"%3A"+minutes+"%3A00&unitGroup=metric&contentType=json&shortColumnNames=true"))
                        .header("x-rapidapi-host", "visual-crossing-weather.p.rapidapi.com")
                        .header("x-rapidapi-key", "0c4cb4457emsh59a9f18425466a7p1045c9jsn08640ab722bc")
                        .build();

        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
