package hcmus.student.map.map.utilities;


import android.content.Context;
import android.location.Location;


public class SpeedMonitor {
    long oldTime = 0;
    Location oldLocation;
    Context context;


    public SpeedMonitor(Context context) {
        oldLocation = null;
        this.context = context;

    }

    public double getSpeed(Location currentLocation) {
        long newTime = System.currentTimeMillis();

        if (oldLocation == null) {
            oldTime = newTime;
            oldLocation = currentLocation;

            return 0;

        } else {
            float distance = currentLocation.distanceTo(oldLocation);

            float timeDifferent = (float) ((newTime - oldTime) / 1000);
            //Get speed in km per hour
            double speed = distance * 3.6 / timeDifferent;
            oldTime = newTime;
            oldLocation = currentLocation;

            return speed;
        }

    }
}
