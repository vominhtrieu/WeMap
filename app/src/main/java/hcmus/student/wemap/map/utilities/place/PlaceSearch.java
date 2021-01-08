package hcmus.student.wemap.map.utilities.place;


import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.google.android.gms.maps.model.LatLng;

import java.net.URLEncoder;
import java.util.Locale;

import hcmus.student.wemap.R;

public class PlaceSearch {
    public static String getUrl(String reference, Activity activity) {
        String API_KEY = activity.getResources().getString(R.string.google_maps_key);
        String data = "";
        try {
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = ((Context) activity).getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = ((Context) activity).getResources().getConfiguration().locale;
            }
            String lang = "en";
            if (locale.getCountry().equals("VN")) {
                lang = "vi";
            }
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json");
            sb.append("?query=").append(URLEncoder.encode(reference, "UTF-8"));
            sb.append("&language=").append(lang);
            sb.append("&key=").append(API_KEY);
            data = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String getUrl(LatLng location, String placeType, Activity activity) {
        String API_KEY = activity.getResources().getString(R.string.google_maps_key);
        String data = "";
        try {
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = ((Context) activity).getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = ((Context) activity).getResources().getConfiguration().locale;
            }
            String lang = "en";
            if (locale.getCountry().equals("VN")) {
                lang = "vi";
            }
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json");
            sb.append("?location=").append(String.valueOf(location.latitude).concat(",").concat(String.valueOf(location.longitude)));
            sb.append("&radius=" + 5000);
            sb.append("&language=").append(lang);
            sb.append("&type=").append(placeType);
            sb.append("&key=").append(API_KEY);
            data = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}