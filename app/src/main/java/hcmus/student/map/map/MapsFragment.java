package hcmus.student.map.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;
import hcmus.student.map.database.Database;
import hcmus.student.map.database.Place;
import hcmus.student.map.map.direction.DirectionFragment;
import hcmus.student.map.map.utilities.LocationChangeCallback;
import hcmus.student.map.map.utilities.MarkerAnimator;
import hcmus.student.map.map.utilities.OrientationSensor;
import hcmus.student.map.map.utilities.direction.Direction;
import hcmus.student.map.map.utilities.direction.DirectionResponse;
import hcmus.student.map.map.utilities.direction.DirectionTask;

public class MapsFragment extends Fragment implements OnMapReadyCallback, DirectionResponse, MapsFragmentCallbacks, LocationChangeCallback {

    private static final int DEFAULT_ZOOM = 15;
    private static final int EPSILON = 5;
    private static final int NORMAL_ROUTE_WIDTH = 8;
    private static final int SELECTED_ROUTE_WIDTH = 12;

    private GoogleMap mMap;
    private Location mCurrentLocation;
    private Marker mLocationIndicator;
    private Marker marker;
    private List<Marker> mContactMarkers;
    private ArrayList<Polyline> mRoutes;
    private Marker mRouteStartMarker, mRouteEndMarker;
    private MapView mMapView;
    private OrientationSensor mSensor;
    private Database mDatabase;
    private MarkerAnimator animator;
    private MainActivity main;
    private Context context;
    private DirectionFragment directionFragment;

    private boolean isContactShown = false;

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
            mContactMarkers = new ArrayList<>();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
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
        main.registerLocationChange(this);
        mMapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ImageButton btnLocation = getView().findViewById(R.id.btnLocation);
        final FloatingActionButton btnContact = getView().findViewById(R.id.btnContact);

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),
                        mMap.getCameraPosition().zoom >= DEFAULT_ZOOM ? mMap.getCameraPosition().zoom : DEFAULT_ZOOM
                ));
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContactShown) {
                    hideAllAddress();
                    btnContact.setImageDrawable(ResourcesCompat.getDrawable(getResources() ,R.drawable.ic_btn_show_contact, null));
                    isContactShown = false;
                } else {
                    showAllAddress();
                    btnContact.setImageDrawable(ResourcesCompat.getDrawable(getResources() ,R.drawable.ic_btn_hide_contact, null));
                    isContactShown = true;
                }
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (marker != null) marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getPosition().equals(mLocationIndicator.getPosition()))
                    return true;
                main.openMarkerInfo(marker);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                return true;
            }
        });

        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPolylineClick(Polyline polyline) {
                for (Polyline route : mRoutes) {
                    route.setZIndex(0);
                    route.setWidth(NORMAL_ROUTE_WIDTH);
                }
                polyline.setWidth(SELECTED_ROUTE_WIDTH);
                polyline.setZIndex(1);
                main.openRouteInfo(polyline.getTag().toString(), polyline.getColor());
            }
        });
    }

    @Override
    public void onLocationChange(Location location) {
        if (mMap == null)
            return;
        //Display location indicator
        if (mCurrentLocation == null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.location_indicator,
                    context.getTheme());
            Bitmap bitmap = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), 72, 72, false);
            bitmapDrawable.setAntiAlias(true);
            mLocationIndicator = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).flat(true)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)).anchor(0.5f, 0.5f));
            animator = new MarkerAnimator(mLocationIndicator, mMap);        //Move camera to user location with default zoom
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                    location.getLongitude()), DEFAULT_ZOOM));
        }
        mCurrentLocation = location;
        animator.animate(location);
    }

    @Override
    public void closeDirection() {
        getChildFragmentManager().popBackStack();
        if (mRoutes != null) {
            for (int i = 0; i < mRoutes.size(); i++) {
                mRoutes.get(i).remove();
            }
        }

        if (mRouteStartMarker != null)
            mRouteStartMarker.remove();
        if (mRouteEndMarker != null)
            mRouteEndMarker.remove();

        directionFragment = null;
    }

    public void drawRoute(LatLng start, LatLng end) {
        LatLng startPos = start == null ? new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()) : start;
        LatLng endPos = end == null ? new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()) : end;
        if (directionFragment == null)
            showDirectionFragment(null, endPos);
        else {
            try {
                directionFragment.onRouteChange(start, end);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String url = Direction.getDirectionUrl(startPos, endPos, main);
        new DirectionTask(this).execute(url);
    }

    private void showAllAddress() {
        List<Place> places = mDatabase.getAllPlaces();
        for (Place place : places) {
            createAvatarMarker(place.getLocation(), place.getAvatar());
        }
    }

    private void hideAllAddress() {
        for (Marker marker : mContactMarkers) {
            marker.remove();
        }
    }

    public void showDirectionFragment(LatLng origin, LatLng dest) {
        directionFragment = DirectionFragment.newInstance(origin, dest);

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameTop, directionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void openSearchResultMarker(LatLng coordinate) {
        if (marker != null)
            marker.remove();
        marker = mMap.addMarker(new MarkerOptions().position(coordinate));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(coordinate));
    }

    @Override
    public void createAvatarMarker(LatLng coordinate, byte[] avt) {
        if (marker != null) {
            marker.remove();
            marker = null;
        }
        Bitmap bmpMarker = BitmapFactory.decodeResource(getResources(), R.drawable.marker_frame).copy(Bitmap.Config.ARGB_8888, true);
        bmpMarker = Bitmap.createScaledBitmap(bmpMarker, 100, 115, false);
        if (avt != null) {
            Bitmap bmpAvatar = BitmapFactory.decodeByteArray(avt, 0, avt.length);
            bmpAvatar = Bitmap.createScaledBitmap(bmpAvatar, 90, 90, false);
            Canvas canvas = new Canvas(bmpMarker);
            canvas.drawBitmap(bmpAvatar, 5, 5, null);
        }

        mContactMarkers.add(mMap.addMarker(new MarkerOptions().position(coordinate)
                .icon(BitmapDescriptorFactory.fromBitmap(bmpMarker))));
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
    public void onRouteRespond(List<PolylineOptions> polylineOptions, List<String> durations) {
        if (mRoutes != null) {
            for (int i = 0; i < mRoutes.size(); i++) {
                mRoutes.get(i).remove();
            }
        }

        if (polylineOptions == null || polylineOptions.size() == 0) {
            Toast.makeText(context, "Cannot find direction to this location", Toast.LENGTH_SHORT).show();
            return;
        }

        mRoutes = new ArrayList<>();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < polylineOptions.size(); i++) {
            PolylineOptions route = polylineOptions.get(i);
            Polyline polyline = mMap.addPolyline((route));
            polyline.setClickable(true);
            polyline.setTag(durations.get(i));
            polyline.setZIndex(0);
            mRoutes.add(polyline);
            List<LatLng> points = route.getPoints();
            for (LatLng point : points) {
                builder.include(point);
            }
        }

        LatLngBounds bounds = builder.build();

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200), 1000, null);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.marker_point,
                context.getTheme());
        Bitmap bmp = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), 36, 36, false);
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