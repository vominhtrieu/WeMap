package hcmus.student.map.map.utilities.direction;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PolylineString {
    String mEncodedString;

    public PolylineString(String encodedString) {
        mEncodedString = encodedString;
    }

    public List<LatLng> decode() {
        List<ArrayList<Integer>> numbers = new ArrayList<>();

        int numberIndex = 0;
        numbers.add(new ArrayList<Integer>());

        for (int i = 0; i < mEncodedString.length(); i++) {
            int temp = mEncodedString.charAt(i) - 63;
            numbers.get(numberIndex).add(temp & 0x1F);

            //If 5th bit is 1, there is a chunk follow
            if ((temp & 0x20) == 0 && i != mEncodedString.length() - 1) {
                numbers.add(new ArrayList<Integer>());
                numberIndex++;
            }
        }

        ArrayList<Double> coordinates = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            int number = 0;

            ArrayList<Integer> elem = numbers.get(i);
            for (int j = 0; j < elem.size(); j++) {
                number |= elem.get(j) << (j * 5);
            }

            //Check if original number is negative
            if ((number & 1) == 1) {
                number = ~number;
            }

            coordinates.add(((double) (number >> 1)) / 100000.0);
        }

        List<LatLng> poly = new ArrayList<>();

        double lat = 0, lng = 0;

        for (int i = 0; i < coordinates.size(); i += 2) {
            lat += coordinates.get(i);
            lng += coordinates.get(i + 1);
            poly.add(new LatLng(lat, lng));
        }

        return poly;
    }
}
