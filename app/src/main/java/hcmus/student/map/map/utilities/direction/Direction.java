package hcmus.student.map.map.utilities.direction;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import hcmus.student.map.R;

public class Direction {

    public static String getDirectionUrl(LatLng start, LatLng end, String mode, Activity activity) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://maps.googleapis.com/maps/api/directions/json?");
        sb.append("origin=").append(start.latitude).append(",").append(start.longitude).append("&");
        sb.append("destination=").append(end.latitude).append(",").append(end.longitude).append("&");
        sb.append("alternatives=true&");
        sb.append("mode=").append(mode).append("&");
        if (mode.equals("transit"))
            sb.append("transit_mode=bus&");
        sb.append("key=").append(activity.getResources().getString(R.string.google_maps_key));
        Log.d("d", sb.toString());
        return sb.toString();
    }
}
