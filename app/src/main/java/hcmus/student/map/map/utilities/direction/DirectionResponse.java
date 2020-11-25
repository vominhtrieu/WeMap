package hcmus.student.map.map.utilities.direction;

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public interface DirectionResponse {
    void onRouteRespond(List<PolylineOptions> polygonOptions);
}
