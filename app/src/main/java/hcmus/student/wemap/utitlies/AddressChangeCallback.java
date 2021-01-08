package hcmus.student.wemap.utitlies;

import hcmus.student.wemap.model.Place;

public interface AddressChangeCallback {
    void onAddressInsert(Place place);
    void onAddressUpdate(Place place);
    void onAddressDelete(int placeId);
}