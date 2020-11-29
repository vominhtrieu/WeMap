package hcmus.student.map.weather.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

public class DailyWeather {
    private String day;
    private Bitmap icon;
    private double minTemp, maxTemp;

    public DailyWeather(int timestamp, String icon, double minTemp, double maxTemp) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(timestamp * 1000);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));

        this.day = new DateFormatSymbols().getShortWeekdays()[cal.get(java.util.Calendar.DAY_OF_WEEK)];
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
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }
}
