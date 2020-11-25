package hcmus.student.map.map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RouteInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RouteInfoFragment extends Fragment {
    private MainActivity activity;
    private TextView txtDuration;

    public static RouteInfoFragment newInstance(String routeDuration) {
        RouteInfoFragment fragment = new RouteInfoFragment();
        Bundle args = new Bundle();
        args.putString("duration", routeDuration);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route_info, container, false);

        txtDuration = view.findViewById(R.id.txtDuration);
        Bundle args = getArguments();
        String routeDuration = activity.getText(R.string.routeinfo_duration) + " " + args.getString("duration");
        txtDuration.setText(routeDuration);

        return view;
    }
}