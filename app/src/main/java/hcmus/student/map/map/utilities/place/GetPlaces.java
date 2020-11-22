package hcmus.student.map.map.utilities.place;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hcmus.student.map.database.Place;
import hcmus.student.map.map.utilities.FetchUrlTask;
import hcmus.student.map.map.utilities.direction.JSONParser;

public class GetPlaces extends AsyncTask<String, String,  List<Place>> {

    @Override
    protected List<Place> doInBackground(String... params) {
        try {
            String url = params[0];
            Log.d("Url", url);
            FetchUrlTask downloadUrl = new FetchUrlTask();
            String googlePlacesData = downloadUrl.fetch(url);
            JSONParser parser = new JSONParser();
            return parser.parse(googlePlacesData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Place> result) {
        for (Place place : result) {
            Log.d("DEBUG", place.getName());
        }
    }
}