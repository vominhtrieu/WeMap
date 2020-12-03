package hcmus.student.map.map.utilities;

import android.animation.ValueAnimator;
import android.location.Location;
import android.util.Log;
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

    public void animate(final Location currentLocation, final boolean isCameraFollowing) {
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

                if (isCameraFollowing) {
                    LatLng cameraPosition = mMap.getCameraPosition().target;

                    double newCameraLat = newLat - cameraPosition.latitude;
                    double newCameraLng = newLng - cameraPosition.longitude;
                    newCameraLat = cameraPosition.latitude + newCameraLat * 0.1;
                    newCameraLng = cameraPosition.longitude + newCameraLng * 0.1;

                    LatLng newCameraPosition = new LatLng(newCameraLat, newCameraLng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newCameraPosition));
                }
                mLocationIndicator.setPosition(newPosition);
            }
        });
        animator.start();
    }
}