package hcmus.student.wemap.map.utilities.direction;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Route {
    List<List<List<LatLng>>> route;
    String duration;

    public Route() {
        route = new ArrayList<>();
    }

    public Route(List<List<List<LatLng>>> route, String duration) {
        this.route = route;
        this.duration = duration;
    }


    public List<List<List<LatLng>>> getRoute() {
        return route;
    }

    public String getDuration() {
        return duration;
    }
}
