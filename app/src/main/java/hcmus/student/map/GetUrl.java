package hcmus.student.map;


import android.app.Activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GetUrl {

    // KEY!

//    public static String FindPlaceFromText(String input) {
//        String data="";
//        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/findplacefromtext/json");
//        try {
//            sb.append("?input="+input);
//            sb.append("&inputtype=" + URLEncoder.encode(input, "utf8"));
//            sb.append("&key=" + API_KEY);
//            } catch (UnsupportedEncodingException ex) {
//            ex.printStackTrace();
//        }
//        data=sb.toString();
//        return data;
//    }
//
//    public static String NearbySearch(String keyword, double lat, double lng, int radius) {
//        String data="";
//        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json");
//        sb.append("?location=" + lat + "," + lng);
//        sb.append("&radius=" + radius);
//        sb.append("&keyword=" + keyword);
//        sb.append("&sensor=true");
//        sb.append("&key="+ API_KEY);
//        data=sb.toString();
//
//        return data;
//    }

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