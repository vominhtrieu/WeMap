package hcmus.student.map;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hcmus.student.map.map.AddContactFragment;
import hcmus.student.map.map.MapsFragment;
import hcmus.student.map.map.MarkerInfoFragment;
import hcmus.student.map.map.utilities.LocationChangeCallback;


public class MainActivity extends FragmentActivity implements MainCallbacks {
    private static final long UPDATE_INTERVAL = 1000;
    private static final long FASTEST_UPDATE_INTERVAL = 1000;
    private static final int LOCATION_STATUS_CODE = 1;
    private FusedLocationProviderClient mClient;
    private ViewPager2 mViewPager;
    private ViewPagerAdapter adapter;
    private Location mCurrentLocation;
    private MapsFragment mMapFragment;
    private List<LocationChangeCallback> delegates;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout mTabs = findViewById(R.id.tabs);

        mViewPager = findViewById(R.id.pager);
        mViewPager.setUserInputEnabled(false);

        adapter = new ViewPagerAdapter(this);
        mViewPager.setAdapter(new ViewPagerAdapter(this));
        mViewPager.setAdapter(adapter);
        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                mMapFragment = (MapsFragment) adapter.getFragment(0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mClient = LocationServices.getFusedLocationProviderClient(this);
        delegates = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableLocation();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showAlert();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_STATUS_CODE);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_STATUS_CODE) {
            if (grantResults.length > 0)
                enableLocation();
        }
    }

    private void listenToLocationChange() {
        //Request change location setting
        final LocationRequest request = LocationRequest.create();
        request.setInterval(UPDATE_INTERVAL);
        request.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addAllLocationRequests(Collections.singleton(request));
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        final Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        //Listen for location change event
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationCallback mLocationCallBack = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            Location temp = locationResult.getLastLocation();
                            if (temp == null) return;

                            mCurrentLocation = temp;
                            notifyLocationChange();
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

    private void notifyLocationChange() {
        for (LocationChangeCallback delegate : delegates) {
            delegate.onLocationChange(mCurrentLocation);
        }
    }

    private void enableLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            listenToLocationChange();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public Location getLocation() {
        return mCurrentLocation;
    }

    public void backToPreviousFragment() {
        getSupportFragmentManager().popBackStack();
    }

    public void openMarkerInfo(Marker marker) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frameBottom, MarkerInfoFragment.newInstance(marker));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    public void drawRoute(LatLng start, LatLng end) {
        MapsFragment fragment = (MapsFragment) adapter.getFragment(0);
        fragment.drawRoute(start, end);
    }

    @Override
    public void locatePlace(LatLng location) {


        mViewPager.setCurrentItem(0);
        MapsFragment fragment = (MapsFragment) adapter.getFragment(0);
        fragment.moveCamera(location);
        TabLayout mTabs = findViewById(R.id.tabs);
        TabLayout.Tab tab = mTabs.getTabAt(0);
        tab.select();
    }

    @Override
    public void registerLocationChange(LocationChangeCallback delegate) {
        delegates.add(delegate);
    }

    @Override
    public void openSearchResultMarker(LatLng latLng) {
        MapsFragment fragment = (MapsFragment) adapter.getFragment(0);
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        fragment.openSearchResultMarker(latLng);
    }

    @Override
    public void openAddContact(LatLng latLng) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.frameBottom, AddContactFragment.newInstance(latLng));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0 && adapter.getFragment(0).getChildFragmentManager().getBackStackEntryCount() > 0) {
            ((MapsFragment) adapter.getFragment(0)).closeDirection();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void updateOnscreenMarker(LatLng coordinate, byte[] avt) {
        mMapFragment.createAvatarMarker(coordinate, avt);
    }
}