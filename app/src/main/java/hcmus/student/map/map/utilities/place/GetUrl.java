package hcmus.student.map.map.utilities.place;


import android.app.Activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import hcmus.student.map.R;

public class GetUrl {
    public static String TextSearch(String reference, Activity activity) {
        String API_KEY = activity.getResources().getString(R.string.google_maps_key);
        String data="";
        try {
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json");
            sb.append("?query=" + URLEncoder.encode(reference, "UTF-8"));
            sb.append("&key=" + API_KEY);
            data = sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}