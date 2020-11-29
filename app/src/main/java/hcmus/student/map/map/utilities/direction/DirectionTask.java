package hcmus.student.map.map.utilities.direction;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hcmus.student.map.utitlies.FetchUrlTask;

public class DirectionTask extends AsyncTask<String, Integer, List<Route>> {
    static final int[] ROUTE_COLORS = {Color.BLUE, Color.RED, Color.BLACK, Color.YELLOW, Color.GRAY};

    DirectionResponse delegate;

    public DirectionTask(DirectionResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected List<Route> doInBackground(String... strings) {
        FetchUrlTask task = new FetchUrlTask();
        String respond = task.fetch(strings[0]);

        List<Route> result = null;
        try {
            JSONObject jsonObject = new JSONObject(respond);
            JSONParser parser = new JSONParser();
            result = parser.parseRoutes(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<Route> lists) {
        if (lists == null) {
            return;
        }

        ArrayList<PolylineOptions> respondPolylines = new ArrayList<>();
        ArrayList<String> durations = new ArrayList<>();

        int routeCount = Math.min(lists.size(), 5);

        for (int i = routeCount - 1; i >= 0; i--) {
            PolylineOptions polylineOptions = new PolylineOptions();
            ArrayList<LatLng> points = new ArrayList<>();

            List<List<List<LatLng>>> legs = lists.get(i).getRoute();
            for (int j = 0; j < legs.size(); j++) {
                List<List<LatLng>> steps = legs.get(j);
                for (int k = 0; k < steps.size(); k++) {
                    points.addAll(steps.get(k));
                }
            }
            polylineOptions.addAll(points);
            polylineOptions.width(8);
            polylineOptions.color(ROUTE_COLORS[i]);
            respondPolylines.add(polylineOptions);
            durations.add(lists.get(i).getDuration());
        }

        delegate.onRouteRespond(respondPolylines, durations);
    }
}
