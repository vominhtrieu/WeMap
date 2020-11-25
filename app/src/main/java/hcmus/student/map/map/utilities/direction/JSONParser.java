package hcmus.student.map.map.utilities.direction;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hcmus.student.map.database.Place;

public class JSONParser {
    public List<List<List<List<LatLng>>>> parseRoutes(JSONObject jsonObject) {
        List<List<List<List<LatLng>>>> routes = null;
        try {
            JSONArray jsonRoutes = jsonObject.getJSONArray("routes");

            routes = new ArrayList<>();
            for (int i = 0; i < jsonRoutes.length(); i++) {
                JSONArray jsonLegs = jsonRoutes.getJSONObject(i).getJSONArray("legs");

                List<List<List<LatLng>>> legs = new ArrayList<>();

                for (int j = 0; j < jsonLegs.length(); j++) {
                    JSONArray jsonSteps = jsonLegs.getJSONObject(j).getJSONArray("steps");

                    List<List<LatLng>> steps = new ArrayList<>();
                    for (int k = 0; k < jsonSteps.length(); k++) {
                        JSONObject jsonStep = jsonSteps.getJSONObject(k);
                        String points = jsonStep.getJSONObject("polyline").getString("points");
                        PolylineString polyline = new PolylineString(points);
                        steps.add(polyline.decode());
                    }
                    legs.add(steps);
                }
                routes.add(legs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    public List<Place> parsePlace(String jsonData) {
        JSONArray result;
        List<Place> placeList = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            result = jsonObject.getJSONArray("results");

            int placesCount = result.length();
            placeList = new ArrayList<>();

            for (int i = 0; i < placesCount; i++) {
                try {
                    JSONObject place = result.getJSONObject(i);
                    String name = place.getString("name");
                    JSONObject location = place.getJSONObject("geometry").getJSONObject("location");
                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");

                    placeList.add(new Place(name, new LatLng(lat, lng), null));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return placeList;
    }

}