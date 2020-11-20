package hcmus.student.map.map.utilities.direction;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONParser {
    public List<List<List<List<LatLng>>>> parseRoutes(JSONObject jsonObject) {
        List<List<List<List<LatLng>>>> routes = null;
        try {
            JSONArray jsonRoutes = jsonObject.getJSONArray("routes");

            routes = new ArrayList<>();
            for (int i = 0; i < jsonRoutes.length(); i++) {
                JSONArray jsonLegs = jsonRoutes.getJSONObject(i).getJSONArray("legs");

                List<List<List<LatLng>>> legs = new ArrayList<>();

                for (int j = 0; j < jsonRoutes.length(); j++) {
                    JSONArray jsonSteps = jsonLegs.getJSONObject(i).getJSONArray("steps");

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

    public List<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";


        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");
            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}