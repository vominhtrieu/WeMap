package hcmus.student.map.utitlies;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import hcmus.student.map.model.Database;
import hcmus.student.map.model.Place;

public class AddressProvider {
    private Database mDatabase;
    private OnAddressChange delegate;
    private List<Place> places;

    public AddressProvider(Context context, OnAddressChange delegate) {
        this.delegate = delegate;
        mDatabase = new Database(context);
        places = mDatabase.getAllPlaces();
    }


    public List<Place> getPlaces() {
        return places;
    }

    public void insertPlace(String name, LatLng location, String avatar) {
        mDatabase.insertPlace(name, location, avatar);
        Place newPlace = mDatabase.searchForPlaces(name).get(0);
        places = mDatabase.getAllPlaces();
        delegate.onAddressInsert(newPlace);
    }

    public void updatePlace(Place place) {
        mDatabase.editPlace(place);
        places = mDatabase.getAllPlaces();
        delegate.onAddressUpdate(place);
    }

    public void deletePlace(int placeId) {
        mDatabase.deletePlace(placeId);
        places = mDatabase.getAllPlaces();
        delegate.onAddressDelete(placeId);
    }

    public void addFavorite(int placeId) {
        mDatabase.addFavorite(placeId);
        places = mDatabase.getAllPlaces();
    }

    public void removeFavorite(int placeId) {
        mDatabase.removeFavorite(placeId);
        places = mDatabase.getAllPlaces();
    }
}
