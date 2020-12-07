package hcmus.student.map.utitlies;

import com.google.android.gms.maps.model.LatLng;

import hcmus.student.map.map.utilities.LocationChangeCallback;
import hcmus.student.map.model.Place;

public interface MainCallbacks {
    void registerLocationChange(LocationChangeCallback delegate);

    void registerAddressChange(AddressChangeCallback delegate);

    void openSearchResultMarker(LatLng latLng);

    void openAddContact(LatLng latLng);

    void openRouteInfo(String routeDuration, int routeColor);

    void backToPreviousFragment();

    void locatePlace(LatLng location);

    void editPlaces(Place place);

    AddressProvider getAddressProvider();
}
