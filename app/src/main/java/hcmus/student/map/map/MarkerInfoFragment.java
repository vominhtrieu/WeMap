package hcmus.student.map.map;

import android.content.Context;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Formatter;
import java.util.Locale;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;
import hcmus.student.map.utitlies.AddressLine;
import hcmus.student.map.utitlies.OnAddressLineResponse;
import hcmus.student.map.weather.WeatherAsynTask;
import hcmus.student.map.weather.utilities.DetailWeather;
import hcmus.student.map.weather.utilities.GetWeather;
import hcmus.student.map.weather.utilities.OnWeatherResponse;

public class MarkerInfoFragment extends Fragment implements View.OnClickListener, OnAddressLineResponse, OnWeatherResponse {
    private MainActivity activity;
    private LatLng latLng;
    private Context context;
    Button btnAdd, btnClose, btnDirection;
    TextView txtPlaceName, txtLat, txtLng,txtTemperature,txtHumidity,txtWind;
    private ImageView ivIcon;

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
        context=getContext();
        Bundle args = getArguments();
        latLng = new LatLng(args.getDouble("lat"), args.getDouble("lng"));
        String url = GetWeather.getUrl(activity, latLng);
        WeatherAsynTask weatherAsynTask = new WeatherAsynTask(this);
        weatherAsynTask.execute(url);
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
        txtTemperature=view.findViewById(R.id.txtTemperature);
        txtHumidity=view.findViewById(R.id.txtHumidity);
        txtWind=view.findViewById(R.id.txtWind);
        ivIcon=view.findViewById(R.id.ivIcon);


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
                activity.drawRoute(null, latLng, "driving");
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

    @Override
    public void onWeatherResponse(DetailWeather detailWeather) {
        if (detailWeather != null) {
//            Log.d("Tem",String.valueOf());
            int imageId = context.getResources().getIdentifier("ic_weather_" + detailWeather.getIcon(), "drawable", context.getPackageName());
            ivIcon.setImageResource(imageId);
            ivIcon.setColorFilter(Color.BLACK);
            StringBuilder sb1 = new StringBuilder();
            Formatter formatter1 = new Formatter(sb1, Locale.US);
            txtTemperature.setText(formatter1.format("%s %.2f°", "Temperature: ",detailWeather.getTemperature()).toString());
            StringBuilder sb2 = new StringBuilder();
            Formatter formatter2 = new Formatter(sb2, Locale.US);
            txtWind.setText(formatter2.format("%s %.2fmph", "Wind Speed: ",detailWeather.getWindSpeed()).toString());
            StringBuilder sb3 = new StringBuilder();
            Formatter formatter3 = new Formatter(sb3, Locale.US);
            txtHumidity.setText(formatter3.format("%s %.2f%%", "Humidity: ",detailWeather.getHumidity()).toString());
        } else {
            txtTemperature.setText(R.string.txtUnknownLocation);
            txtHumidity.setText(R.string.txtUnknownLocation);
            txtWind.setText(R.string.txtUnknownLocation);
        }

    }
}

