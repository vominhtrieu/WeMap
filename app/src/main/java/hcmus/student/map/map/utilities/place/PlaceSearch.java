package hcmus.student.map.map.utilities.place;


import android.app.Activity;
import android.content.Context;
import android.os.Build;

import java.net.URLEncoder;
import java.util.Locale;

import hcmus.student.map.R;

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
            sb.append("?query=" + URLEncoder.encode(reference, "UTF-8"));
            sb.append("&language=" + lang);
            sb.append("&key=" + API_KEY);
            data = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}