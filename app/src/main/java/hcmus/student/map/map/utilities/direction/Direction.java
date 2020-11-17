package hcmus.student.map.map.utilities.direction;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import hcmus.student.map.R;

public class Direction {

    public static String getDirectionUrl(LatLng start, LatLng end, Activity activity) {
        String url = String.format(Locale.US,
                "https://maps.googleapis.com/maps/api/directions/json?origin=%f,%f&destination=%f,%f&key=%s",
                start.latitude, start.longitude, end.latitude, end.longitude, activity.getResources().getString(R.string.google_maps_key));
        return url;
    }
}
