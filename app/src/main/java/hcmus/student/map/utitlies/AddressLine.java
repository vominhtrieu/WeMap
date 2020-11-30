package hcmus.student.map.utitlies;

import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

public class AddressLine extends AsyncTask<LatLng, Void, String> {
    Geocoder geocoder;
    OnAddressLineResponse delegate;

    public AddressLine(Geocoder geocoder, OnAddressLineResponse delegate) {
        this.geocoder = geocoder;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(LatLng... locations) {
        LatLng location = locations[0];
        try {
            return geocoder.getFromLocation(location.latitude, location.longitude, 1).get(0).getAddressLine(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        delegate.onAddressLineResponse(s);
    }
}
