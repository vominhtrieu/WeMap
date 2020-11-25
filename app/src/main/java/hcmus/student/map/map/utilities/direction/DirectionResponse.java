package hcmus.student.map.map.utilities.direction;

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public interface DirectionResponse {
    void onRespond(List<PolylineOptions> polygonOptions, List<String> duration);
}
