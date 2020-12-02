package hcmus.student.map.map.utilities;


import android.content.Context;
import android.location.Location;



public class SpeedMonitor {
    double oldTime = 0;
    Location oldLocation;
    Context context;



    public SpeedMonitor(Context context) {
        oldLocation = null;
        this.context=context;

    }

    public double getSpeed(Location currentLocation) {
        double newTime = System.currentTimeMillis();

        if (oldLocation == null ) {
            oldTime = newTime;
            oldLocation = currentLocation;

            return 0;

        } else {
            float[] result = new float[1];
            Location.distanceBetween(oldLocation.getLatitude(), oldLocation.getLongitude(),
                    currentLocation.getLatitude(), currentLocation.getLongitude(), result);
            double distance = result[0] * 1000;

            double timeDifferent = newTime - oldTime;
            //Get speed in km per hour
            double speed = distance * 3.6 / timeDifferent;
            oldTime = newTime;
            oldLocation = currentLocation;


            return Math.round(speed);

        }

    }
}
