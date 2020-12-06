package hcmus.student.map.utitlies;

import com.google.android.gms.maps.model.LatLng;

import hcmus.student.map.map.utilities.LocationChangeCallback;

public interface MainCallbacks {
    void registerLocationChange(LocationChangeCallback delegate);

    void openSearchResultMarker(LatLng latLng);

    void openAddContact(LatLng latLng);

    void openRouteInfo(String routeDuration, int routeColor);

    void backToPreviousFragment();

    void updateOnscreenMarker(LatLng coordinate, byte[] avt);

    void locatePlace(LatLng location);
}
