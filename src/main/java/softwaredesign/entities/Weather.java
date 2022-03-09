package softwaredesign.entities;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

public class Weather {
    private String timeZone;
    private String conditions;
    private double tempreture;
    private double windSpeed;
    private double humidity;
    public Weather(Metrics metrics) throws IOException {
        Date date = metrics.getTimeStamps()[0];

    }






    String day,month,year,latitude,longitude,Hours,Minutes;
    int b = 6;
    String a = integerToString(b);
    OkHttpClient client = new OkHttpClient();
    // test with lat="52.086967" lon="4.3763127"
    Request request = new Request.Builder()
            .url("https://visual-crossing-weather.p.rapidapi.com/history?startDateTime=2019-01-01T00%3A00%3A00&aggregateHours=24&location=52.086967%2C4.3763127&endDateTime=2019-01-03T00%3A00%3A00&unitGroup=metric&dayStartTime=8%3A00%3A00&contentType=json&dayEndTime=17%3A00%3A00&shortColumnNames=0")
            .get()
            .addHeader("x-rapidapi-host", "visual-crossing-weather.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "0c4cb4457emsh59a9f18425466a7p1045c9jsn08640ab722bc")
            .build();

    private Response response = client.newCall(request).execute();


    //converts a number to a 2 digit format 1->01 5->05 24->24 etc..
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
