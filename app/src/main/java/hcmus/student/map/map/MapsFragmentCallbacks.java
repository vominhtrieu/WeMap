package hcmus.student.map.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import hcmus.student.map.model.Place;

public interface MapsFragmentCallbacks {
    void openSearchResultMarker(LatLng coordinate);
    void closeDirection();
    void moveCamera(LatLng location);
    void onNearbySearchRespond(String type, List<Place> places);
    //void removeMarker();
}
