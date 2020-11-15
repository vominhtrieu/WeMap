package hcmus.student.map;

public class Place  {
    private String name;
    private Double longitude;
    private Double latitude;
    private byte[] avatar;

    public Place(String name, Double longitude, Double latitude, byte[] avatar) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public byte[] getAvatar() {
        return avatar;
    }
}
