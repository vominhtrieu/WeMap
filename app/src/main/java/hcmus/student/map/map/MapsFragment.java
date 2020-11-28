package hcmus.student.map.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hcmus.student.map.MainActivity;
import hcmus.student.map.map.utilities.SpeedMonitor;
import hcmus.student.map.map.utilities.place.PlaceSearch;
import hcmus.student.map.map.utilities.MapsFragmentCallbacks;
import hcmus.student.map.R;
import hcmus.student.map.database.Database;
import hcmus.student.map.database.Place;
import hcmus.student.map.map.utilities.MarkerAnimator;
import hcmus.student.map.map.utilities.OrientationSensor;
import hcmus.student.map.map.utilities.direction.Direction;
import hcmus.student.map.map.utilities.direction.DirectionResponse;
import hcmus.student.map.map.utilities.direction.DirectionTask;
import hcmus.student.map.map.utilities.place.GetPlaces;

public class MapsFragment extends Fragment implements OnMapReadyCallback, DirectionResponse, MapsFragmentCallbacks {

    private static final int LOCATION_STATUS_CODE = 1;
    private static final int DEFAULT_ZOOM = 15;
    private static final long UPDATE_INTERVAL = 1000;
    private static final long FASTEST_UPDATE_INTERVAL = 1000;
    private static final int EPSILON = 5;

    private GoogleMap mMap;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mClient;
    private Marker mLocationIndicator;
    private LocationCallback mLocationCallBack;
    private Marker marker;
    private ArrayList<Polyline> mRoutes;
    private Marker mRouteStartMarker, mRouteEndMarker;
    private MapView mMapView;
    private OrientationSensor mSensor;
    private Database mDatabase;

    private MainActivity main;
    private Context context;
    private SearchView searchView;
    private ListView listPlace;
    SpeedMonitor speedMonitor;
    TextView txtSpeed;

    @RequiresApi(api = Build.VERSION_CODES.M)

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
            main = (MainActivity) getActivity();
            mRouteStartMarker = mRouteEndMarker = null;
            mDatabase = new Database(context);
            speedMonitor = new SpeedMonitor(context);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }

        String url= PlaceSearch.TextSearch("HCMUS", main);
        GetPlaces getNearby=new GetPlaces();
        getNearby.execute(url);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        SensorManager sensorService = (SensorManager) main.getSystemService(Context.SENSOR_SERVICE);
        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        listPlace = view.findViewById(R.id.listPlace);
        searchView = view.findViewById(R.id.svSearch);
        txtSpeed =view.findViewById(R.id.txtSpeed);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();

                Database db = new Database(context);
                List<Place> places = db.getAllPlaces();
                for (int i = 0; i < places.size(); i++) {
                    if (location.equals(places.get(i).getName())) {
                        Toast.makeText((MainActivity) getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText((MainActivity) getActivity(), "Not succsess", Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                Database db = new Database(context);
                List<Place> place = db.getAllPlaces();

                ArrayList<String> placesList = new ArrayList<>();
                for (int i = 0; i < place.size(); i++) {
                    String name = place.get(i).getName().toLowerCase();
                    if (name.contains(newText)) {
                        placesList.add(place.get(i).getName());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>((MainActivity) getActivity(), android.R.layout.simple_list_item_1, placesList);
                listPlace.setAdapter(adapter);

                if (searchView.getQuery().toString().equals("")) {
                    placesList = new ArrayList<>();
                    adapter = new ArrayAdapter<String>((MainActivity) getActivity(), android.R.layout.simple_list_item_1, placesList);
                    listPlace.setAdapter(adapter);
                }

                return true;
            }
        });


        //Implement Rotation change here
        mSensor = new OrientationSensor(sensorService) {
            float previousRotation = 0;

            @Override
            public void onSensorChanged(float rotation) {
                //Implement Rotation change here
                if (mLocationIndicator != null && Math.abs(previousRotation - rotation) >= EPSILON) {
                    mLocationIndicator.setRotation(rotation);
                    previousRotation = rotation;
                }
            }
        };

        mClient = LocationServices.getFusedLocationProviderClient(context);
        mLocationCallBack = null;
        mRoutes = null;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableLocation();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showAlert();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_STATUS_CODE);
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ImageButton btnLocation = getView().findViewById(R.id.btnLocation);

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
                context.getTheme());
        Bitmap bitmap = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), 72, 72, false);
        bitmapDrawable.setAntiAlias(true);
        mLocationIndicator = mMap.addMarker(new MarkerOptions().position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())).flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)).anchor(0.5f, 0.5f));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (marker != null) marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getPosition().equals(mLocationIndicator.getPosition()))
                    return true;
                main.openMarkerInfo(marker);
                return true;
            }
        });

        //Move camera to user location with default zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(),
                mCurrentLocation.getLongitude()), DEFAULT_ZOOM));

        showAllAddress();
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

    private void enableLocation() {
        if (ActivityCompat.checkSelfPermission(main, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = mClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {

                        mCurrentLocation = location;
                        mMapView.getMapAsync(MapsFragment.this);
                    }
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

    private void listenToLocationChange() {
        //Request change location setting
        final LocationRequest request = LocationRequest.create();
        request.setInterval(UPDATE_INTERVAL);
        request.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addAllLocationRequests(Collections.singleton(request));
        SettingsClient settingsClient = LocationServices.getSettingsClient(main);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        final MarkerAnimator animator = new MarkerAnimator(mLocationIndicator, mMap);

        //Listen for location change event
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (ActivityCompat.checkSelfPermission(main,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLocationCallBack = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            mCurrentLocation = locationResult.getLastLocation();
                            if (mCurrentLocation != null){

                                txtSpeed.setText(Double.toString(speedMonitor.getSpeed(mCurrentLocation))+"km/h");
                                animator.animate(mCurrentLocation);
                            }
                        }
                    };

                    mClient.requestLocationUpdates(request, mLocationCallBack, Looper.myLooper());
                }
            }
        });
    }

    public void drawRoute(LatLng start, LatLng end) {
        LatLng startPos = start == null ? new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()) : start;
        LatLng endPos = end == null ? new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()) : end;

        String url = Direction.getDirectionUrl(startPos, endPos, main);
        new DirectionTask(this).execute(url);
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

    private void showAllAddress() {
        List<Place> places = mDatabase.getAllPlaces();
        for (Place place : places) {
            if(place.getLatitude() != null) {
                createAvatarMarker(new LatLng(place.getLatitude(), place.getLongitude()), place.getAvatar());
            }
        }
    }

    @Override
    public void createAvatarMarker(LatLng coordinate, byte[] avt) {
        if (marker != null) {
            marker.remove();
            marker = null;
        }
        Bitmap bmpMarker = BitmapFactory.decodeResource(getResources(), R.drawable.marker_frame).copy(Bitmap.Config.ARGB_8888, true);
        bmpMarker = Bitmap.createScaledBitmap(bmpMarker, 100, 130, false);
        if (avt != null) {
            Bitmap bmpAvatar = BitmapFactory.decodeByteArray(avt, 0, avt.length);
            bmpAvatar = Bitmap.createScaledBitmap(bmpAvatar, 72, 72, false);
            Canvas canvas = new Canvas(bmpMarker);
            canvas.drawBitmap(bmpAvatar, 14, 14, null);
        }

        mMap.addMarker(new MarkerOptions().position(coordinate)
                .icon(BitmapDescriptorFactory.fromBitmap(bmpMarker)));
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mSensor.register();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensor.unregister();
    }

    @Override
    public void onRespond(List<PolylineOptions> polylineOptions) {
        if (mRoutes != null) {
            for (int i = 0; i < mRoutes.size(); i++) {
                mRoutes.get(i).remove();
            }
        }

        mRoutes = new ArrayList<>();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (PolylineOptions route : polylineOptions) {
            mRoutes.add(mMap.addPolyline(route));
            List<LatLng> points = route.getPoints();
            for (LatLng point : points) {
                builder.include(point);
            }
        }
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100), 1000, null);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.marker_point,
                context.getTheme());
        Bitmap bmp = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), 48, 48, false);
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(bmp);
        if (mRouteStartMarker != null)
            mRouteStartMarker.remove();
        if (mRouteEndMarker != null)
            mRouteEndMarker.remove();

        mRouteStartMarker = mMap.addMarker(new MarkerOptions()
                .position(polylineOptions.get(0).getPoints().get(0))
                .icon(descriptor).anchor(0.5f, 0.5f));
        mRouteEndMarker = mMap.addMarker(new MarkerOptions()
                .position(polylineOptions.get(0).getPoints().get(polylineOptions.get(0).getPoints().size() - 1))
                .icon(descriptor).anchor(0.5f, 0.5f));
    }
}

