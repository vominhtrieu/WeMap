package hcmus.student.map.weather.utilities;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailWeatherParser {
    DetailWeather parse(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        JSONObject main = jsonObject.getJSONObject("main");

        JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);

        String icon = weather.getString("icon");
        String description = weather.getString("description");

        double temp = main.getDouble("temp") - 273.15;
        double humidity = main.getDouble("humidity");

        double rain;
        try {
            rain = jsonObject.getJSONObject("rain").getDouble("1h");
        }
        catch (Exception e) {
            rain = 0.0;
        }
        double wind = jsonObject.getJSONObject("wind").getDouble("speed");

        return new DetailWeather(icon, description, temp, rain, wind, humidity);
    }
}
