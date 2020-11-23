package hcmus.student.map.map.utilities.place;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hcmus.student.map.map.utilities.DataParser;
import hcmus.student.map.map.utilities.DownloadUrl;
import hcmus.student.map.database.Place;

public class GetPlaces extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {

        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
        ArrayList<Place> list= PlacesList(nearbyPlacesList);
        for (int i=0; i<list.size();i++){
            Log.d("name: ", list.get(i).getName());
            Log.d("Latitude: ", String.valueOf(list.get(i).getLatitude()));
            Log.d("Longtitude: ", String.valueOf(list.get(i).getLongitude()));
        }
    }

    private ArrayList<Place> PlacesList (List<HashMap<String, String>> nearbyPlacesList) {
        ArrayList<Place> places= new ArrayList<>();

        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
//          LatLng latLng = new LatLng(lat, lng);
            Place place =new Place(placeName,lat,lng,null);
            places.add(place);
        }
        return places;
    }
}