package hcmus.student.map.weather.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class DetailWeather {
    private String icon;
    private String description;
    private double temperature, rain, windSpeed, humidity;
    private List<DailyWeather> dailyWeathers;

    public DetailWeather(String icon, String description, double temperature, double rain, double windSpeed, double humidity, List<DailyWeather> dailyWeathers) {
        this.icon = icon;
        this.description = description;
        this.temperature = temperature;
        this.rain = rain;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.dailyWeathers = dailyWeathers;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getRain() {
        return rain;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getHumidity() {
        return humidity;
    }

    public List<DailyWeather> getDailyWeathers() {
        return dailyWeathers;
    }
}
