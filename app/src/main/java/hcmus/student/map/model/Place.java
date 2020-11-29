package hcmus.student.map.model;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    private String name;
    private LatLng location;
    private byte[] avatar;
    private String favorite;

    public Place(String name, LatLng location, byte[] avatar) {
        this.name = name;
        this.location = location;
        this.avatar = avatar;
        this.favorite = "0";
    }

    public Place(String name, LatLng location, byte[] avatar, String favorite) {
        this.name = name;
        this.location = location;
        this.avatar = avatar;
        this.favorite = favorite;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }
}
