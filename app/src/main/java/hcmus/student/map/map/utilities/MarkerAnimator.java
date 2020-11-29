package hcmus.student.map.map.utilities;

import android.animation.ValueAnimator;
import android.location.Location;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MarkerAnimator {
    private static final long ANIMATION_DURATION = 500;
    private static final double FOLLOWING_THRESHOLD = 0.00000001;

    public Marker mLocationIndicator;
    public GoogleMap mMap;

    public MarkerAnimator(Marker locationIndicator, GoogleMap map) {
        this.mLocationIndicator = locationIndicator;
        this.mMap = map;
    }

    public void animate(final Location currentLocation) {
        final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

        final LatLng startLatLng = mLocationIndicator.getPosition();
        final LatLng endLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                double newLat = startLatLng.latitude + (endLatLng.latitude - startLatLng.latitude) * fraction;
                double newLng = startLatLng.longitude + (endLatLng.longitude - startLatLng.longitude) * fraction;
                LatLng newPosition = new LatLng(newLat, newLng);

                //Check if user moved camera, if yes, camera won't follow user's location
                LatLng cameraTargetPosition = mMap.getCameraPosition().target;
                LatLng indicatorPosition = mLocationIndicator.getPosition();

                double latitudeDiff = cameraTargetPosition.latitude - indicatorPosition.latitude;
                double longitudeDiff = cameraTargetPosition.longitude - indicatorPosition.longitude;

                //We don't compute square root because this result is enough for checking whether user move the camera or not
                double squareDistance = latitudeDiff * latitudeDiff + longitudeDiff * longitudeDiff;

                if (squareDistance < FOLLOWING_THRESHOLD)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newPosition));
                mLocationIndicator.setPosition(newPosition);
            }
        });
        animator.start();
    }
}