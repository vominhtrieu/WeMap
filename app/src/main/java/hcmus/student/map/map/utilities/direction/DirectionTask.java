package hcmus.student.map.map.utilities.direction;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hcmus.student.map.map.utilities.FetchUrlTask;

public class DirectionTask extends AsyncTask<String, Integer, List<List<List<List<LatLng>>>>> {
    static final int[] ROUTE_COLORS = {Color.BLUE, Color.RED, Color.BLACK, Color.YELLOW, Color.GRAY};

    DirectionResponse delegate;

    public DirectionTask(DirectionResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected List<List<List<List<LatLng>>>> doInBackground(String... strings) {
        FetchUrlTask task = new FetchUrlTask();
        String respond = task.fetch(strings[0]);

        List<List<List<List<LatLng>>>> result = null;
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
    protected void onPostExecute(List<List<List<List<LatLng>>>> lists) {
        if (lists == null) {
            return;
        }

        ArrayList<PolylineOptions> respond = new ArrayList<>();
        int routeCount = Math.min(lists.size(), 5);

        for (int i = routeCount - 1; i >= 0; i--) {
            PolylineOptions polylineOptions = new PolylineOptions();
            ArrayList<LatLng> points = new ArrayList<>();

            List<List<List<LatLng>>> legs = lists.get(i);
            for (int j = 0; j < legs.size(); j++) {
                List<List<LatLng>> steps = legs.get(j);
                for (int k = 0; k < steps.size(); k++) {
                    points.addAll(steps.get(k));
                }
            }
            polylineOptions.addAll(points);
            polylineOptions.width(8);
            polylineOptions.color(ROUTE_COLORS[i]);
            respond.add(polylineOptions);
        }

        delegate.onRespond(respond);
    }
}
