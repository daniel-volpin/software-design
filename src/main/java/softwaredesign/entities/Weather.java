package softwaredesign.entities;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.util.Date;
import java.time.*;

public class Weather {

    private String conditions;
    private double tempreture;
    private double windSpeed;
    private double humidity;

    public Weather(Metrics metrics) throws IOException {

        Date timeStamp = metrics.getTimeStamps()[0];
        LocalDate a;

        String date,month,year,hours,minutes;
        double latitude,longitude;

//        date = integerToString(timeStamp.getDate());
//        month = integerToString(timeStamp.getMonth());
//        year = integerToString(timeStamp.getYear());
//        hours = integerToString(timeStamp.getHours());
//        minutes = integerToString(timeStamp.getMinutes());

        latitude = metrics.getLatitudes()[0];
        longitude = metrics.getLongitudes()[0];

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://visual-crossing-weather.p.rapidapi.com/history?startDateTime="+year+"-"+month+"-"+date+"T"+hours+"%3A"+minutes+"%3A00&aggregateHours=1&location="+latitude+"%2C"+longitude+"&endDateTime="+year+"-"+month+"-"+date+"T"+hours+"%3A"+minutes+"%3A00&unitGroup=metric&contentType=json&shortColumnNames=true")
                .get()
                .addHeader("x-rapidapi-host", "visual-crossing-weather.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "0c4cb4457emsh59a9f18425466a7p1045c9jsn08640ab722bc")
                .build();

        Response response = client.newCall(request).execute();



    }

    //converts a number to a 2 digit String format 1->"01" 5->"05" 24->"24" etc..
    String integerToString(Integer number){
        if(number < 0){
            return null;
        }
        if(number < 10) {
            return "0" + number;
        }
        return number.toString();
    }
}
