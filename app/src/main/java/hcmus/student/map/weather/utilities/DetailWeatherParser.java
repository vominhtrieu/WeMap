package hcmus.student.map.weather.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailWeatherParser {
    final static int MAX_NUMBER_OF_DAY = 7;
    DetailWeather parse(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        JSONObject current = jsonObject.getJSONObject("current");

        JSONObject weather = current.getJSONArray("weather").getJSONObject(0);

        String icon = weather.getString("icon");
        String description = weather.getString("description");

        double temp = current.getDouble("temp") - 273.15;
        double humidity = current.getDouble("humidity");

        double rain;
        try {
            rain = current.getJSONObject("rain").getDouble("1h");
        } catch (Exception e) {
            rain = 0.0;
        }
        double wind = current.getDouble("wind_speed");

        JSONArray daily = jsonObject.getJSONArray("daily");
        List<DailyWeather> dailyWeathers = new ArrayList<>();
        for (int i = 0; i < MAX_NUMBER_OF_DAY; i++) {
            JSONObject dailyWeather = daily.getJSONObject(i);
            icon = dailyWeather.getJSONArray("weather").getJSONObject(0).getString("icon");
            int timestamp = dailyWeather.getInt("dt");
            double minTemp = dailyWeather.getJSONObject("temp").getDouble("min") - 273.15;
            double maxTemp = dailyWeather.getJSONObject("temp").getDouble("max") - 273.15;
            dailyWeathers.add(new DailyWeather(timestamp, icon, minTemp, maxTemp));
        }

        return new DetailWeather(icon, description, temp, rain, wind, humidity, dailyWeathers);
    }
}
