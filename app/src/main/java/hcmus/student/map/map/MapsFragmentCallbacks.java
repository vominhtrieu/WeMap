package hcmus.student.map.map;

import com.google.android.gms.maps.model.LatLng;

public interface MapsFragmentCallbacks {
    void openSearchResultMarker(LatLng coordinate);
    void createAvatarMarker(LatLng coordinate, byte[] avt);
    void closeDirection();
    void moveCamera(LatLng location);
}
