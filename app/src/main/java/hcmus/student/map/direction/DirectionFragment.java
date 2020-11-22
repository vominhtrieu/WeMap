package hcmus.student.map.direction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;

import hcmus.student.map.R;

public class DirectionFragment extends Fragment {
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_direction, null, false);
        return layout;
    }
}
