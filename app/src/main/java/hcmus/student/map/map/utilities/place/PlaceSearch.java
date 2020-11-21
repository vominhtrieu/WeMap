package hcmus.student.map.map.utilities.place;


import android.app.Activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import hcmus.student.map.R;

public class GetUrl {
    public static String TextSearch(String reference, Activity activity) {
        String API_KEY = activity.getResources().getString(R.string.google_maps_key);
        String data="";
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json");
        sb.append("?query="+reference);
        sb.append("&key="+ API_KEY);
        data=sb.toString();

        return data;
    }
}