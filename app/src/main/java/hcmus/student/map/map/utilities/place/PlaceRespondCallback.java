package hcmus.student.map.map.utilities.place;

import java.util.List;

import hcmus.student.map.model.Place;

public interface PlaceRespondCallback {
    void onRespond(String url, List<Place> placeList);
}