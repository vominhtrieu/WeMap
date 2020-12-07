package hcmus.student.map.utitlies;

import hcmus.student.map.model.Place;

public interface OnAddressChange {
    void onAddressInsert(Place place);
    void onAddressUpdate(Place place);
    void onAddressDelete(int placeId);
}