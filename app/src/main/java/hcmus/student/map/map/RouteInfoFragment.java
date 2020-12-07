package hcmus.student.map.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route_info, container, false);
        TextView txtDuration = view.findViewById(R.id.txtDuration);
        View vRouteIndicator = view.findViewById(R.id.vRouteIndicator);
        Bundle args = getArguments();
        String routeDuration = activity.getText(R.string.route_info_duration) + " " + args.getString("duration");
        int routeColor = args.getInt("color");
        txtDuration.setText(routeDuration);
        vRouteIndicator.setBackgroundColor(routeColor);

        return view;
    }
}