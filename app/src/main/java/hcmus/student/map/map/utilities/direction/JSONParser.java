package hcmus.student.map.map.utilities.direction;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
}