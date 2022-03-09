package softwaredesign.entities;

import com.squere

public class Weather {
    OkHttpClient client = new OkHttpClient();
    // test with lat="52.086967" lon="4.3763127"
    Request request = new Request.Builder()
            .url("https://visual-crossing-weather.p.rapidapi.com/history?startDateTime=2019-01-01T00%3A00%3A00&aggregateHours=24&location=52.086967%2C4.3763127&endDateTime=2019-01-03T00%3A00%3A00&unitGroup=metric&dayStartTime=8%3A00%3A00&contentType=json&dayEndTime=17%3A00%3A00&shortColumnNames=0")
            .get()
            .addHeader("x-rapidapi-host", "visual-crossing-weather.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "0c4cb4457emsh59a9f18425466a7p1045c9jsn08640ab722bc")
            .build();

    Response response = client.newCall(request).execute();
}
