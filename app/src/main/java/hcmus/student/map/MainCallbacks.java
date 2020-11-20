package hcmus.student.map;

import com.google.android.gms.maps.model.LatLng;

public interface MainCallbacks {
    void updateOnscreenMarker(LatLng coordinate, byte[] avt);
}
