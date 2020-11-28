package hcmus.student.map.weather.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DetailWeather {
    private Bitmap icon;
    private String description;
    private double temperature, rain, wind, humidity;

    public DetailWeather(String icon, String description, double temperature, double rain, double wind, double humidity) {
        try {
            URL url = new URL("https://openweathermap.org/img/wn/" + icon + "@2x.png");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();
            this.icon = BitmapFactory.decodeStream(stream);
        } catch (Exception e) {
            this.icon = null;
        }
        this.description = description;
        this.temperature = temperature;
        this.rain = rain;
        this.wind = wind;
        this.humidity = humidity;
    }

    public Bitmap getIcon() {
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

    public double getWind() {
        return wind;
    }

    public double getHumidity() {
        return humidity;
    }
}
