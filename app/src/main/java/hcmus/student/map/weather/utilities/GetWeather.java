package hcmus.student.map.weather.utilities;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import hcmus.student.map.R;

public class GetWeather {
    public static String getUrl(Context context, LatLng location) {
        String key = context.getResources().getString(R.string.open_weather_map_api);
        return String.format(Locale.US, "https://api.openweathermap.org/data/2.5/onecall?lat=%f&lon=%f&exclude=minutely,hourly,alerts&appid=%s",
                location.latitude, location.longitude, key);
    }
}
