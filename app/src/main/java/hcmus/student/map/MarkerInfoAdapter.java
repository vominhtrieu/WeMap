package hcmus.student.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.Marker;

public class MarkerInfoAdapter extends Fragment implements View.OnClickListener {
    private MapsActivity parent;
    private Marker mMarker;
    Button btnAdd, btnClose;
    TextView txtLat, txtLng;

    public MarkerInfoAdapter(Marker marker) {
        this.mMarker = marker;
    }

    public MarkerInfoAdapter() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.marker_info, null);
        this.parent = (MapsActivity) getActivity();
        txtLat = layout.findViewById(R.id.txtLat);
        txtLng = layout.findViewById(R.id.txtLng);
        btnAdd = layout.findViewById(R.id.btnAdd);
        btnClose = layout.findViewById(R.id.btnClose);

        txtLat.setText(Double.toString(mMarker.getPosition().latitude));
        txtLng.setText(Double.toString(mMarker.getPosition().longitude));
        btnAdd.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        return layout;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = new AddContacts();
                transaction.replace(R.id.frameMarkerInfo, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.btnClose:
                ((MapsActivity)getActivity()).closeMarkerInfo();
                break;
        }
    }
}

