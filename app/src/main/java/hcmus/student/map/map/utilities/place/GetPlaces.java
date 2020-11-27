package hcmus.student.map.map.utilities.place;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import hcmus.student.map.database.Place;
import hcmus.student.map.map.utilities.FetchUrlTask;
import hcmus.student.map.map.utilities.direction.JSONParser;


public class GetPlaces extends AsyncTask<String, String, List<Place>> {
    PlaceRespondCallback delegate;
    String url;

    public GetPlaces(PlaceRespondCallback delegate) {
        this.delegate = delegate;
    }

    @Override
    protected List<Place> doInBackground(String... params) {
        try {
            url = params[0];
            FetchUrlTask downloadUrl = new FetchUrlTask();
            String googlePlacesData = downloadUrl.fetch(url);
            JSONParser parser = new JSONParser();
            return parser.parsePlace(googlePlacesData);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Place> result) {
        delegate.onRespond(url, result);
    }
}