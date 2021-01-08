package hcmus.student.wemap.map.utilities.place;

import java.util.List;

import hcmus.student.wemap.model.Place;

public interface PlaceRespondCallback {
    void onRespond(String url, List<Place> placeList);
}