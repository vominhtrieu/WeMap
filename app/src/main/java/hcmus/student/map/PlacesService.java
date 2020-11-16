package hcmus.student.map;

import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class PlacesService {
    private static final String LOG_TAG="Map";
    private static final String PLACES_API_BASE="https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE="/autocomplete";
    private static final String TYPE_DETAILS="/detagitils";
    private static final String TYPE_SEARCH="/search";
    private static final String OUT_JSON="/json";

    private static final String API_KEY="AIzaSyBFmKqnMzYN036kYwrb9AIdf-kIoD4JF9I";

    public static String autocomplete(String input){
        String data="";
        HttpURLConnection conn=null;
        StringBuilder jsonResults=new StringBuilder();
        try{
            StringBuilder sb=new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_AUTOCOMPLETE);
            sb.append(OUT_JSON);
            sb.append("?sensor=false");
            sb.append("&key=" + API_KEY);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url=new URL(sb.toString());
            conn=(HttpURLConnection) url.openConnection();
            InputStreamReader in=new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff=new char[1024];
            while ((read=in.read(buff))!=-1){
                jsonResults.append(buff,0,read);
            }
            data=jsonResults.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Error processing Places API URL", e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG,"Error connecting to Places API URL", e);
            e.printStackTrace();
        }finally {
            if (conn!=null){
                conn.disconnect();
            }
        }

        return data;
    }

    public static String search(String keyword, double lat, double lng, int radius){
        String data="";
        HttpURLConnection conn=null;
        StringBuilder jsonResults=new StringBuilder();
        try{
            StringBuilder sb=new StringBuilder();
            sb.append(TYPE_SEARCH);
            sb.append(OUT_JSON);
            sb.append("?sensor=false");
            sb.append("&key=" + API_KEY);
            sb.append("&keyword=" + URLEncoder.encode(keyword, "utf8"));
            sb.append("&location=" + String.valueOf(lat) + "," +String.valueOf(lng));
            sb.append("&radius=" + String.valueOf(radius));

            URL url=new URL(sb.toString());
            conn=(HttpURLConnection) url.openConnection();
            InputStreamReader in=new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff=new char[1024];
            while ((read=in.read(buff))!=-1){
                jsonResults.append(buff,0,read);
            }
            data=jsonResults.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Error processing Places API URL", e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG,"Error connecting to Places API URL", e);
            e.printStackTrace();
        }finally {
            if (conn!=null){
                conn.disconnect();
            }
        }
        return data;
    }
    public static String details(String reference) {
        String data="";
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_DETAILS);
            sb.append(OUT_JSON);
            sb.append("?sensor=false");
            sb.append("&key=" + API_KEY);
            sb.append("&reference=" + URLEncoder.encode(reference, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            data=jsonResults.toString();
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return data;
    }
}

