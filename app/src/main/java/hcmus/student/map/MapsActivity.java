package hcmus.student.map;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Collections;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_STATUS_CODE = 1;
    private static final int DEFAULT_ZOOM = 15;
    private static final long UPDATE_INTERVAL = 1000;
    private static final long FASTEST_UPDATE_INTERVAL = 1000;
    private static final long ANIMATION_DURATION = 500;
    private static final double FOLLOWING_THRESHOLD = 0.00000001;

    private GoogleMap mMap;
    private OrientationSensor sensor;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mClient;
    private Marker mLocationIndicator;
    private LocationCallback mLocationCallBack;
    private Marker marker;
    private DataBase mDataBase;
    private MarkerInfoAdapter mMarkerInfoAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SensorManager sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        sensor = new OrientationSensor(sensorService) {
            @Override
            public void onSensorChanged(float rotation) {
                //Implement Rotation change here
                //Log.d("Azimuth", Float.toString(rotation));
            }

        };

        mClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallBack = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableLocation();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showAlert();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_STATUS_CODE);
        }

//        mDataBase=new DataBase(this,"AddressBook.sql",null,1);
//        mDataBase.QueryData("CREATE TABLE IF NOT EXISTS PlaceTable(Name NVARCHAR(200),Longitude DOUBLE PRIMARY KEY, Latitude DOUBLE PRIMARY KEY, Avatar BLOG");



    }

    protected void onResume() {
        super.onResume();
        listenToLocationChange();
        sensor.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationCallBack != null) {
            mClient.removeLocationUpdates(mLocationCallBack);
            sensor.unregister();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        ImageButton btnLocation = findViewById(R.id.btnLocation);

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),
                        mMap.getCameraPosition().zoom >= DEFAULT_ZOOM ? mMap.getCameraPosition().zoom : DEFAULT_ZOOM
                ));
            }
        });

        //Display location indicator
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.location_indicator,
                getTheme());
        Bitmap bitmap = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), 72, 72, false);
        bitmapDrawable.setAntiAlias(true);
        mLocationIndicator = mMap.addMarker(new MarkerOptions().position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (marker != null) marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                mMarkerInfoAdapter = new MarkerInfoAdapter(marker);
                fragmentTransaction.add(R.id.frameMarkerInfo, mMarkerInfoAdapter);

                fragmentTransaction.commit();
                return false;
            }
        });


        //Move camera to user location with default zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(),
                mCurrentLocation.getLongitude()), DEFAULT_ZOOM));

        listenToLocationChange();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_STATUS_CODE:
                if (grantResults.length > 0)
                    enableLocation();
                break;
        }
    }

    private void animateLocationIndicator() {
        final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

        final LatLng startLatLng = mLocationIndicator.getPosition();
        final Location tempLocation = mCurrentLocation;
        final LatLng endLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mCurrentLocation != tempLocation) {
                    animator.cancel();
                    return;
                }
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

    private void enableLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = mClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mCurrentLocation = location;
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(MapsActivity.this);
                    }
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            builder.setTitle("Warning!");
            builder.setMessage("You have denied the app to access your permission, this app won't work");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    //Should move to inteface later
    public void closeMarkerInfo() {
        if (mMarkerInfoAdapter != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(mMarkerInfoAdapter);
            fragmentTransaction.commit();
        }
    }

    public void backToFragmentBefore(int id){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(id);
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }
    }


    private void listenToLocationChange() {
        //Request change location setting
        final LocationRequest request = LocationRequest.create();
        request.setInterval(UPDATE_INTERVAL);
        request.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addAllLocationRequests(Collections.singleton(request));
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        //Listen for location change event
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    mLocationCallBack = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            mCurrentLocation = locationResult.getLastLocation();

                            if (mCurrentLocation != null) {
                                animateLocationIndicator();
                            }
                        }
                    };

                    mClient.requestLocationUpdates(request, mLocationCallBack, Looper.myLooper());
                }
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("Warning!");
        builder.setMessage("App must have permission to access you location");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes, I know", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_STATUS_CODE);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}