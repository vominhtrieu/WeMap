package hcmus.student.map.utitlies;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Collections;

public class LocationService {
    private static final long UPDATE_INTERVAL = 1000;
    private static final long FASTEST_UPDATE_INTERVAL = 1000;
    private static final long MIN_DISTANCE = 1;

    private Context context;
    private FusedLocationProviderClient mClient;
    private OnLocationChange delegate;

    public LocationService(Context context, OnLocationChange delegate) {
        this.context = context;
        this.mClient = LocationServices.getFusedLocationProviderClient(context);
        this.delegate = delegate;
    }

    public void start() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<LocationAvailability> task = mClient.getLocationAvailability();
        task.addOnSuccessListener(new OnSuccessListener<LocationAvailability>() {
            @Override
            public void onSuccess(LocationAvailability locationAvailability) {
                listenToLocationChange();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                start();
            }
        });
    }

    private void listenToLocationChange() {
        //Request change location setting
        final LocationRequest request = LocationRequest.create();
        request.setInterval(UPDATE_INTERVAL);
        request.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setSmallestDisplacement(MIN_DISTANCE);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addAllLocationRequests(Collections.singleton(request));
        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        final Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        //Listen for location change event
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationCallback mLocationCallBack = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            Location temp = locationResult.getLastLocation();
                            if (temp == null) return;
                            delegate.onLocationChange(temp);
                        }
                    };

                    mClient.requestLocationUpdates(request, mLocationCallBack, Looper.myLooper());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listenToLocationChange();
            }
        });
    }
}
