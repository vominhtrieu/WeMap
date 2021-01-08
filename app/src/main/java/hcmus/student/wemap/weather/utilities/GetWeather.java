package hcmus.student.wemap.weather.utilities;

import android.content.Context;
import android.os.Build;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import hcmus.student.wemap.R;

public class GetWeather {
    public static String getUrl(Context context, LatLng location) {
        String key = context.getResources().getString(R.string.open_weather_map_api);
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        String lang = "en";
        if (locale.getCountry().equals("VN")) {
            lang = "vi";
        }

        return String.format(Locale.US, "https://api.openweathermap.org/data/2.5/onecall?lat=%f&lon=%f&exclude=minutely,hourly,alerts" +
                        "&lang=%s&appid=%s",
                location.latitude, location.longitude, lang, key);
    }
}
