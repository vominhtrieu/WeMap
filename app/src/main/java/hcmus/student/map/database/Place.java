package hcmus.student.map.database;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    private String name;
    private LatLng location;
    private byte[] avatar;

    public Place(String name, LatLng location, byte[] avatar) {
        this.name = name;
        this.location = location;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }

    public byte[] getAvatar() {
        return avatar;
    }
}
