package hcmus.student.map.weather;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import hcmus.student.map.utitlies.FetchUrlTask;
import hcmus.student.map.weather.utilities.DetailWeather;
import hcmus.student.map.weather.utilities.DetailWeatherParser;
import hcmus.student.map.weather.utilities.OnWeatherResponse;

public class WeatherAsynTask extends AsyncTask<String, Void, DetailWeather> {
    OnWeatherResponse delegate;
    public WeatherAsynTask(OnWeatherResponse delegate) {
        this.delegate =  delegate;
    }

    @Override
    protected DetailWeather doInBackground(String... strings) {
        FetchUrlTask fetchUrlTask = new FetchUrlTask();
        String data = fetchUrlTask.fetch(strings[0]);
        DetailWeatherParser parser = new DetailWeatherParser();
        try {
            return parser.parse(data);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(DetailWeather detailWeather) {
        delegate.onWeatherResponse(detailWeather);
    }
}
