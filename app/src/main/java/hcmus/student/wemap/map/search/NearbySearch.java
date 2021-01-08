package hcmus.student.wemap.map.search;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import hcmus.student.wemap.map.MapsFragmentCallbacks;
import hcmus.student.wemap.map.utilities.place.GetPlaces;
import hcmus.student.wemap.map.utilities.place.PlaceRespondCallback;
import hcmus.student.wemap.map.utilities.place.PlaceSearch;
import hcmus.student.wemap.model.Place;

public class NearbySearch implements PlaceRespondCallback {
    final static int DELAY_TYPING = 200;

    Context context;
    MapsFragmentCallbacks delegate;
    List<Place> places;
    String currentRequest;
    String type;

    public NearbySearch(Context context, MapsFragmentCallbacks delegate) {
        this.context = context;
        this.delegate = delegate;
        this.places = new ArrayList<>();
        currentRequest = null;
    }

    public void executeSearch(LatLng location, String type) {
        this.type = type;
        places = new ArrayList<>();
        if (type.length() > 0) {
            GetPlaces getPlaces = new GetPlaces(this);
            currentRequest = PlaceSearch.getUrl(location, type, (Activity) context);
            getPlaces.execute(currentRequest);
        } else {
            currentRequest = null;
        }
    }

    @Override
    public void onRespond(String url, List<Place> placeList) {
        if (currentRequest != null && !currentRequest.equals(url))
            return;
        if (placeList == null)
            return;
        places.addAll(placeList);
        delegate.onNearbySearchRespond(type, places);
    }
}
