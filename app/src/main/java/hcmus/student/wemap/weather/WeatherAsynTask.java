package hcmus.student.wemap.weather;

import android.os.AsyncTask;

import org.json.JSONException;

import hcmus.student.wemap.utitlies.FetchUrlTask;
import hcmus.student.wemap.weather.utilities.DetailWeather;
import hcmus.student.wemap.weather.utilities.DetailWeatherParser;
import hcmus.student.wemap.weather.utilities.OnWeatherResponse;

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
