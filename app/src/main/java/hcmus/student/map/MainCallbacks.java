package hcmus.student.map;

import com.google.android.gms.maps.model.LatLng;

import hcmus.student.map.map.utilities.LocationChangeCallback;
import hcmus.student.map.utitlies.AddressChangeCallback;

public interface MainCallbacks {
    void registerLocationChange(LocationChangeCallback delegate);

    void registerAddressChange(AddressChangeCallback delegate);

    void openSearchResultMarker(LatLng latLng);

    void openAddContact(LatLng latLng);

    void openRouteInfo(String routeDuration, int routeColor);

    void backToPreviousFragment();

    void locatePlace(LatLng location);
}
