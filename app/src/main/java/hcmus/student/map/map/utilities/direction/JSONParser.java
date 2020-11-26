package hcmus.student.map.map.utilities.direction;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hcmus.student.map.database.Place;

public class JSONParser {
    public List<Route> parseRoutes(JSONObject jsonObject) {
        List<Route> routes = null;
        int duration;
        try {
            JSONArray jsonRoutes = jsonObject.getJSONArray("routes");

            routes = new ArrayList<>();
            for (int i = 0; i < jsonRoutes.length(); i++) {
                Route route = new Route();
                duration = 0;
                JSONArray jsonLegs = jsonRoutes.getJSONObject(i).getJSONArray("legs");

                List<List<List<LatLng>>> legs = new ArrayList<>();

                for (int j = 0; j < jsonRoutes.length(); j++) {
                    JSONArray jsonSteps = jsonLegs.getJSONObject(i).getJSONArray("steps");
                    JSONObject jsonDuration = jsonLegs.getJSONObject(i).getJSONObject("duration");
                    duration += jsonDuration.getInt("value");

                    List<List<LatLng>> steps = new ArrayList<>();
                    for (int k = 0; k < jsonSteps.length(); k++) {
                        JSONObject jsonStep = jsonSteps.getJSONObject(k);
                        String points = jsonStep.getJSONObject("polyline").getString("points");
                        PolylineString polyline = new PolylineString(points);
                        steps.add(polyline.decode());
                    }
                    legs.add(steps);
                }
                route.setRoute(legs);
                route.setDuration(convertSecondsToTimeString(duration));
                routes.add(route);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    String convertSecondsToTimeString(int seconds) {
        int days = seconds / 86400;
        int hours = (seconds % 86400) / 3600;
        int minutes = (int) Math.ceil(seconds % 3600 / 60.0);

        String strMinute = minutes > 1 ? (minutes + " minutes ") : (minutes > 0 ? (minutes + " minute ") : "");
        String strHour = hours > 1 ? (hours + " hours ") : (hours > 0 ? (hours + " hour ") : "");
        String strDay = days > 1 ? (days + " days ") : (days > 0 ? (days + " day ") : "");

        return strDay + strHour + strMinute;
    }

    public List<Place> parse(String jsonData) {
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
                    Double lat = location.getDouble("lat");
                    Double lng = location.getDouble("lng");

                    placeList.add(new Place(name, lat, lng, null));

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