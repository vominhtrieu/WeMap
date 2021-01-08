package hcmus.student.wemap.model;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    private int id;
    private String name;
    private LatLng location;
    private String avatar;
    private String favorite;

    public Place(int id, String name, LatLng location, String avatar) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.avatar = avatar;
        this.favorite = "0";
    }

    public Place(int id, String name, LatLng location, String avatar, String favorite) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.avatar = avatar;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public Double getLatitude() {
        if (location != null)
            return location.latitude;
        return null;
    }

}
