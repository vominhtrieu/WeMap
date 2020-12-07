package hcmus.student.map.map;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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
    private View vRouteIndicator;
    
    public static RouteInfoFragment newInstance(String routeDuration, int routeColor) {
        RouteInfoFragment fragment = new RouteInfoFragment();
        Bundle args = new Bundle();
        args.putString("duration", routeDuration);
        args.putInt("color", routeColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = (MainActivity) getActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route_info, container, false);

        txtDuration = view.findViewById(R.id.txtDuration);
        vRouteIndicator = view.findViewById(R.id.vRouteIndicator);
        Bundle args = getArguments();
        String routeDuration = activity.getText(R.string.route_info_duration) + " " + args.getString("duration");
        int routeColor = args.getInt("color");
        txtDuration.setText(routeDuration);
        vRouteIndicator.setBackgroundColor(routeColor);

        return view;
    }
}