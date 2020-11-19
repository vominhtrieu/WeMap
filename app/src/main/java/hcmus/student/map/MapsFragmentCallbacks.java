package hcmus.student.map;

import com.google.android.gms.maps.model.LatLng;

public interface MapsFragmentCallbacks {
    void createAvatarMarker(LatLng coordinate, byte[] avt);
}
