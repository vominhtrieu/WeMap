package hcmus.student.map.utitlies;

import hcmus.student.map.model.Place;

public interface AddressChangeCallback {
    void onAddressInsert(Place place);
    void onAddressUpdate(Place place);
    void onAddressDelete(int placeId);
}