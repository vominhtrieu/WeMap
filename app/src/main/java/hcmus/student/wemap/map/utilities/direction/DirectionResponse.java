package hcmus.student.wemap.map.utilities.direction;

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public interface DirectionResponse {
    void onRouteRespond(List<PolylineOptions> polygonOptions, List<String> duration);
}
