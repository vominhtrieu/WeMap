package hcmus.student.map.map.direction;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;
import hcmus.student.map.model.Place;
import hcmus.student.map.map.search.SearchClickCallback;
import hcmus.student.map.map.search.SearchResultAdapter;

public class DirectionFragment extends Fragment implements DirectionFragmentCallback {
    MainActivity activity;
    Context context;
    int notUserTypingChecker;
    Place origin;
    Place dest;
    EditText edtOrigin, edtDest;

    public static DirectionFragment newInstance(LatLng origin, LatLng dest) {
        Bundle args = new Bundle();
        args.putParcelable("origin", origin);
        args.putParcelable("dest", dest);
        DirectionFragment fragment = new DirectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        context = getContext();
        notUserTypingChecker = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_direction, null, false);
        edtOrigin = view.findViewById(R.id.edtOrigin);
        edtDest = view.findViewById(R.id.edtDest);
        RecyclerView lvFirstSearchResult = view.findViewById(R.id.lvFirstSearchResult);
        RecyclerView lvSecondSearchResult = view.findViewById(R.id.lvSecondSearchResult);

        final SearchResultAdapter firstAdapter = new SearchResultAdapter(context, new SearchClickCallback() {
            @Override
            public void onSearchClickCallback(Place place) {
                activity.drawRoute(origin.getLocation(), dest.getLocation());

            }
        });

        final SearchResultAdapter secondAdapter = new SearchResultAdapter(context, new SearchClickCallback() {
            @Override
            public void onSearchClickCallback(Place place) {
                activity.drawRoute(origin.getLocation(), place.getLocation());
            }
        });

        ImageButton btnLocate1 = view.findViewById(R.id.btnLocate1);
        ImageButton btnLocate2 = view.findViewById(R.id.btnLocate2);
        btnLocate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.drawRoute(null, dest.getLocation());
            }
        });

        btnLocate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.drawRoute(origin.getLocation(), null);
            }
        });

        lvFirstSearchResult.setAdapter(firstAdapter);
        lvSecondSearchResult.setAdapter(secondAdapter);

        Bundle args = getArguments();

        if (args != null) {
            LatLng originLocation = args.getParcelable("origin");
            LatLng destLocation = args.getParcelable("dest");
            Geocoder geocoder = new Geocoder(context);
            try {

                if (originLocation != null)
                    origin = new Place(
                            0, geocoder.getFromLocation(originLocation.latitude, originLocation.longitude, 1).get(0).getAddressLine(0),
                            originLocation, null);
                else
                    origin = new Place(0, activity.getResources().getString(R.string.txtCurrentLocation), null, null);

                if (destLocation != null)
                    dest = new Place(
                            0, geocoder.getFromLocation(destLocation.latitude, destLocation.longitude, 1).get(0).getAddressLine(0),
                            destLocation, null);
                else
                    dest = new Place(0, activity.getResources().getString(R.string.txtCurrentLocation), null, null);

                edtOrigin.setText(origin.getName());
                edtDest.setText(dest.getName());
            } catch (Exception e) {
                origin = null;
                dest = null;
            }
        }

        edtOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (notUserTypingChecker == 0)
                    firstAdapter.search(s.toString());
                else
                    notUserTypingChecker--;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtDest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (notUserTypingChecker == 0)
                    secondAdapter.search(s.toString());
                else
                    notUserTypingChecker--;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button btnSwap = view.findViewById(R.id.btnSwap);
        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Place temp = origin;
                origin = dest;
                dest = temp;

                activity.drawRoute(origin.getLocation(), dest.getLocation());
            }
        });

        return view;
    }

    @Override
    public void onRouteChange(LatLng origin, LatLng dest) throws IOException {
        Geocoder geocoder = new Geocoder(context);
        String originName;
        if (origin == null) {
            originName = context.getResources().getString(R.string.txtCurrentLocation);
        } else
            originName = geocoder.getFromLocation(origin.latitude, origin.longitude, 1).get(0).getAddressLine(0);

        String destName;
        if (dest == null) {
            destName = context.getResources().getString(R.string.txtCurrentLocation);
        } else
            destName = geocoder.getFromLocation(dest.latitude, dest.longitude, 1).get(0).getAddressLine(0);

        this.origin = new Place(0, originName, origin, null);
        this.dest = new Place(0, destName, dest, null);
        notUserTypingChecker += 2;
        edtOrigin.setText(originName);
        edtDest.setText(destName);
    }
}
