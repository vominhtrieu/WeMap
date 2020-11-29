package hcmus.student.map.weather.utilities;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailWeatherParser {
    DetailWeather parse(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        JSONObject main = jsonObject.getJSONObject("main");

        JSONObject weather = jsonObject.getJSONObject("weather");

        String icon = weather.getString("icon");
        String description = weather.getString("description");

        Double temp = main.getDouble("temp") - 273.15;
        Double humidity = main.getDouble("humidity");

        JSONObject rainObject = jsonObject.getJSONObject("rain");
        Double rain;
        if (rainObject != null)
            rain = jsonObject.getJSONObject("rain").getDouble("1h");
        else
            rain = 0.0;
        Double wind = jsonObject.getJSONObject("wind").getDouble("speed");

        return new DetailWeather(icon, description, temp, rain, wind, humidity);
    }
}
