package hcmus.student.map.map.utilities.direction;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Route {
    public Route() {
        route = new ArrayList<>();
    }

    List<List<List<LatLng>>> route;
    String duration;

    public void setRoute(List<List<List<LatLng>>> route) {
        this.route = route;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<List<List<LatLng>>> getRoute() {
        return route;
    }

    public String getDuration() {
        return duration;
    }
}
