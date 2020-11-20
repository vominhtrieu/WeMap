package hcmus.student.map;

import com.google.android.gms.maps.model.LatLng;

public interface MainCallbacks {
    void openAddContact(LatLng latLng);
    void updateOnscreenMarker(LatLng coordinate, byte[] avt);
}
