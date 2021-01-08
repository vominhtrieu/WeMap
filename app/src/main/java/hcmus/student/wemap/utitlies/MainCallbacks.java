package hcmus.student.wemap.utitlies;

import com.google.android.gms.maps.model.LatLng;

import hcmus.student.wemap.map.utilities.LocationChangeCallback;
import hcmus.student.wemap.model.Place;

public interface MainCallbacks {
    void registerLocationChange(LocationChangeCallback delegate);

    void registerAddressChange(AddressChangeCallback delegate);

    void openSearchResultMarker(LatLng latLng);

    void openAddContact(LatLng latLng);

    void backToPreviousFragment();

    void locatePlace(LatLng location);

    void editPlaces(Place place);

    AddressProvider getAddressProvider();
}
