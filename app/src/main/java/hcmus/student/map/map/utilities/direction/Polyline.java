package hcmus.student.map.map.utilities.direction;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Polyline {
    String mEncodedString;

    public Polyline(String encodedString) {
        mEncodedString = encodedString;
    }

    public List<LatLng> decode() {
        int index = 0, len = mEncodedString.length();

        List<Integer> fiveBitArray = new ArrayList<>();

        while (index < len) {
           int temp = mEncodedString.charAt(index) - 63;
            // Turn Off the 5th bit
            temp = temp | (1 << 5);
            fiveBitArray.add(temp);
        }

        List<LatLng> poly = new ArrayList<>();
        int lat = 0, lng = 0, num = 0;

        for (int i = fiveBitArray.size() - 1; i >= 0; i--) {

        }
        return poly;
    }
}
