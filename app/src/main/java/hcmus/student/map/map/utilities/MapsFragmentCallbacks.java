package hcmus.student.map.map.utilities;

import com.google.android.gms.maps.model.LatLng;

public interface MapsFragmentCallbacks {
    void createAvatarMarker(LatLng coordinate, byte[] avt);
}
