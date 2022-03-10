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


    public Weather(Metrics metrics){

        Date timeStamp = metrics.getTimeStamps()[0];

        String date,month,hours,minutes, year;

        double latitude,longitude;

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

        SimpleDateFormat dateFormat = new SimpleDateFormat();

        year = yearFormat.format(timeStamp);
        month = monthFormat.format(timeStamp);
        date = dayFormat.format(timeStamp);
        hours = hourFormat.format(timeStamp);
        minutes = minuteFormat.format(timeStamp);

        latitude = metrics.getLatitudes()[0];
        longitude = metrics.getLongitudes()[0];

        HttpClient client = HttpClient.newHttpClient();
//
//        String res = "{\"columns\":{\"wdir\":{\"id\":\"wdir\",\"name\":\"Wind Direction\",\"type\":2,\"unit\":null},\"latitude\":{\"id\":\"latitude\",\"name\":\"Latitude\",\"type\":2,\"unit\":null},\"cloudcover\":{\"id\":\"cloudcover\",\"name\":\"Cloud Cover\",\"type\":2,\"unit\":\"%\"},\"mint\":{\"id\":\"mint\",\"name\":\"Minimum Temperature\",\"type\":2,\"unit\":\"degC\"},\"datetime\":{\"id\":\"datetime\",\"name\":\"Date time\",\"type\":3,\"unit\":null},\"precip\":{\"id\":\"precip\",\"name\":\"Precipitation\",\"type\":2,\"unit\":\"mm\"},\"solarradiation\":{\"id\":\"solarradiation\",\"name\":\"Solar Radiation\",\"type\":2,\"unit\":\"W/m^2\"},\"dew\":{\"id\":\"dew\",\"name\":\"Dew Point\",\"type\":2,\"unit\":\"degC\"},\"humidity\":{\"id\":\"humidity\",\"name\":\"Relative Humidity\",\"type\":2,\"unit\":\"%\"},\"precipcover\":{\"id\":\"precipcover\",\"name\":\"Precipitation Cover\",\"type\":2,\"unit\":\"%\"},\"longitude\":{\"id\":\"longitude\",\"name\":\"Longitude\",\"type\":2,\"unit\":null},\"info\":{\"id\":\"info\",\"name\":\"Info\",\"type\":1,\"unit\":null},\"temp\":{\"id\":\"temp\",\"name\":\"Temperature\",\"type\":2,\"unit\":\"degC\"},\"address\":{\"id\":\"address\",\"name\":\"Address\",\"type\":1,\"unit\":null},\"maxt\":{\"id\":\"maxt\",\"name\":\"Maximum Temperature\",\"type\":2,\"unit\":\"degC\"},\"visibility\":{\"id\":\"visibility\",\"name\":\"Visibility\",\"type\":2,\"unit\":\"km\"},\"wspd\":{\"id\":\"wspd\",\"name\":\"Wind Speed\",\"type\":2,\"unit\":\"kph\"},\"solarenergy\":{\"id\":\"solarenergy\",\"name\":\"Solar Energy\",\"type\":2,\"unit\":\"J/m^2\"},\"resolvedAddress\":{\"id\":\"resolvedAddress\",\"name\":\"Resolved Address\",\"type\":1,\"unit\":null},\"heatindex\":{\"id\":\"heatindex\",\"name\":\"Heat Index\",\"type\":2,\"unit\":\"degC\"},\"weathertype\":{\"id\":\"weathertype\",\"name\":\"Weather Type\",\"type\":1,\"unit\":null},\"snowdepth\":{\"id\":\"snowdepth\",\"name\":\"Snow Depth\",\"type\":2,\"unit\":\"cm\"},\"sealevelpressure\":{\"id\":\"sealevelpressure\",\"name\":\"Sea Level Pressure\",\"type\":2,\"unit\":\"mb\"},\"snow\":{\"id\":\"snow\",\"name\":\"Snow\",\"type\":2,\"unit\":\"cm\"},\"name\":{\"id\":\"name\",\"name\":\"Name\",\"type\":1,\"unit\":null},\"wgust\":{\"id\":\"wgust\",\"name\":\"Wind Gust\",\"type\":2,\"unit\":\"kph\"},\"conditions\":{\"id\":\"conditions\",\"name\":\"Conditions\",\"type\":1,\"unit\":null},\"windchill\":{\"id\":\"windchill\",\"name\":\"Wind Chill\",\"type\":2,\"unit\":\"degC\"}},\"remainingCost\":0,\"queryCost\":1,\"messages\":null,\"locations\":{\"52.083176,5.173492\":{\"stationContributions\":null,\"values\":[{\"wdir\":310.0,\"temp\":8.6,\"maxt\":8.6,\"visibility\":30.0,\"wspd\":10.9,\"datetimeStr\":\"2016-03-20T11:17:00+01:00\",\"solarenergy\":null,\"heatindex\":null,\"cloudcover\":99.1,\"mint\":8.6,\"datetime\":1458472620000,\"precip\":0.0,\"solarradiation\":null,\"weathertype\":\"Drizzle, Sky Coverage Increasing, Sky Unchanged\",\"snowdepth\":null,\"sealevelpressure\":1021.9,\"snow\":null,\"dew\":1.7,\"humidity\":61.64,\"precipcover\":null,\"wgust\":21.7,\"conditions\":\"Overcast\",\"windchill\":6.9,\"info\":null}],\"id\":\"52.083176,5.173492\",\"address\":\"52.083176,5.173492\",\"name\":\"52.083176,5.173492\",\"index\":0,\"latitude\":52.083176,\"longitude\":5.173492,\"distance\":0.0,\"time\":0.0,\"tz\":\"Europe/Amsterdam\",\"currentConditions\":null,\"alerts\":null}}}\n";
//        JSONObject obj = new JSONObject(res).getJSONObject("locations").getJSONObject("52.083176,5.173492").getJSONArray("values").getJSONObject(0);
//        System.out.println(obj.getString("conditions"));


        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("https://visual-crossing-weather.p.rapidapi.com/history?startDateTime="+year+"-"+month+"-"+date+"T"+hours+"%3A"+minutes+"%3A00&aggregateHours=1&location="+latitude+"%2C"+longitude+"&endDateTime="+year+"-"+month+"-"+date+"T"+hours+"%3A"+minutes+"%3A00&unitGroup=metric&contentType=json&shortColumnNames=true"))
                        .header("x-rapidapi-host", "visual-crossing-weather.p.rapidapi.com")
                        .header("x-rapidapi-key", "0c4cb4457emsh59a9f18425466a7p1045c9jsn08640ab722bc")
                        .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            JSONObject responseJson = new JSONObject(response).getJSONObject("locations").getJSONObject(latitude + "," + longitude).getJSONArray("values").getJSONObject(0);
//            conditions = responseJson.getString("conditions");
//            temperature = responseJson.getDouble("temp");
//            windSpeed = responseJson.getDouble("wspd");
//            humidity = responseJson.getDouble("humidity");
//
//            System.out.println("cond " + conditions + ", temp " + temperature+ ", windspeed " + windSpeed + ", humidity " + humidity);

            System.out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
