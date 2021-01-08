package hcmus.student.wemap.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import hcmus.student.wemap.model.Place;

public interface MapsFragmentCallbacks {
    void openSearchResultMarker(LatLng coordinate);
    void closeDirection();
    void moveCamera(LatLng location);
    void onNearbySearchRespond(String type, List<Place> places);
    //void removeMarker();
}
