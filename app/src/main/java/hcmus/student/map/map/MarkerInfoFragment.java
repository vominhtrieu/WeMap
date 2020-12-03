package hcmus.student.map.map;

import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Formatter;
import java.util.Locale;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;
import hcmus.student.map.utitlies.AddressLine;
import hcmus.student.map.utitlies.OnAddressLineResponse;

public class MarkerInfoFragment extends Fragment implements View.OnClickListener, OnAddressLineResponse {
    private MainActivity activity;
    private LatLng latLng;
    Button btnAdd, btnClose, btnDirection;
    TextView txtPlaceName, txtLat, txtLng;

    public static MarkerInfoFragment newInstance(Marker marker) {
        MarkerInfoFragment fragment = new MarkerInfoFragment();
        Bundle args = new Bundle();
        args.putDouble("lat", marker.getPosition().latitude);
        args.putDouble("lng", marker.getPosition().longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marker_info, container, false);

        txtPlaceName = view.findViewById(R.id.txtPlaceName);
        txtLat = view.findViewById(R.id.txtLat);
        txtLng = view.findViewById(R.id.txtLng);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnDirection = view.findViewById(R.id.btnDirection);
        btnClose = view.findViewById(R.id.btnClose);

        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        Bundle args = getArguments();
        txtPlaceName.setText(R.string.txt_loading_address_line);

        latLng = new LatLng(args.getDouble("lat"), args.getDouble("lng"));
        AddressLine addressLine = new AddressLine(new Geocoder(getContext()), this);
        addressLine.execute(latLng);

        String latStr = getResources().getString(R.string.txt_latitude);
        String lngStr = getResources().getString(R.string.txt_longitude);
        txtLat.setText(formatter.format("%s %.2f", latStr, latLng.latitude).toString());
        sb.setLength(0);
        txtLng.setText(formatter.format("%s %.2f", lngStr, latLng.longitude).toString());

        btnAdd.setOnClickListener(this);
        btnDirection.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDirection:
                activity.drawRoute(null, latLng);
                activity.backToPreviousFragment();
                break;
            case R.id.btnAdd:
                activity.openAddContact(latLng);
                break;
            case R.id.btnClose:
                activity.backToPreviousFragment();
                break;
        }
    }

    @Override
    public void onAddressLineResponse(String addressLine) {
        if (addressLine != null) {
            txtPlaceName.setText(addressLine);
        } else {
            txtPlaceName.setText(R.string.txtUnknownLocation);
        }
    }
}

