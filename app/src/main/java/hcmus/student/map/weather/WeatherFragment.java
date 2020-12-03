package hcmus.student.map.weather;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;
import hcmus.student.map.utitlies.AddressLine;
import hcmus.student.map.utitlies.OnAddressLineResponse;
import hcmus.student.map.weather.utilities.DetailWeather;
import hcmus.student.map.weather.utilities.GetWeather;
import hcmus.student.map.weather.utilities.GetWeatherDetailTask;
import hcmus.student.map.weather.utilities.OnDetailWeatherResponse;
import hcmus.student.map.weather.utilities.WeatherAdapter;

public class WeatherFragment extends Fragment implements OnAddressLineResponse, OnDetailWeatherResponse {
    private MainActivity activity;
    private TextView txtPlaceName;
    private TextView txtDescription;
    private ImageView ivWeatherStatus;
    private TextView txtTemperature;
    private TextView txtRain;
    private TextView txtWind;
    private TextView txtHumidity;
    private RecyclerView rvWeather;
    private WeatherAdapter adapter;

    private View container;
    private Context context;
    private LinearLayout weatherForecastContainer;

    public static WeatherFragment newInstance() {
        Bundle args = new Bundle();
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        this.container = view.findViewById(R.id.container);
        this.container.setVisibility(View.INVISIBLE);
        txtPlaceName = view.findViewById(R.id.txtPlaceName);
        ivWeatherStatus = view.findViewById(R.id.ivWeatherStatus);
        txtTemperature = view.findViewById(R.id.txtTemperature);
        txtDescription = view.findViewById(R.id.txtDescription);
        txtRain = view.findViewById(R.id.txtRain);
        txtWind = view.findViewById(R.id.txtWind);
        txtHumidity = view.findViewById(R.id.txtHumidity);
        weatherForecastContainer = view.findViewById(R.id.weatherForecastContainer);

        //Recycle View
        rvWeather = view.findViewById(R.id.rvWeather);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvWeather.setLayoutManager(manager);
        rvWeather.setItemAnimator(new DefaultItemAnimator());
        adapter = new WeatherAdapter(getContext());
        rvWeather.setAdapter(adapter);
        rvWeather.setHasFixedSize(false);

        ImageButton btnLeft = view.findViewById(R.id.btnLeft);
        ImageButton btnRight = view.findViewById(R.id.btnRight);

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int firstVisibleItemIndex = manager.findFirstCompletelyVisibleItemPosition();
                if (firstVisibleItemIndex > 0) {
                    manager.smoothScrollToPosition(rvWeather,null,firstVisibleItemIndex-1);
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalItemCount = rvWeather.getAdapter().getItemCount();
                if (totalItemCount <= 0) return;
                int lastVisibleItemIndex = manager.findLastVisibleItemPosition();

                if (lastVisibleItemIndex >= totalItemCount) return;
                manager.smoothScrollToPosition(rvWeather, null, lastVisibleItemIndex + 1);
            }

        });

        txtPlaceName.setText(R.string.txt_loading_address_line);
        AddressLine addressLine = new AddressLine(new Geocoder(getContext()), this);
        Location location = activity.getLocation();
        addressLine.execute(new LatLng(location.getLatitude(), location.getLongitude()));

        GetWeatherDetailTask getWeatherDetailTask = new GetWeatherDetailTask(this);
        String url = GetWeather.getUrl(context, new LatLng(location.getLatitude(), location.getLongitude()));
        getWeatherDetailTask.execute(url);
        return view;
    }

    @Override
    public void onAddressLineResponse(String addressLine) {
        if (addressLine != null)
            txtPlaceName.setText(addressLine);
        else
            txtPlaceName.setText(R.string.txtNullLocation);
    }

    @Override
    public void onDetailWeatherResponse(DetailWeather detailWeather) {
        if (detailWeather != null) {
            container.setVisibility(View.VISIBLE);
            int imageId = context.getResources().getIdentifier("ic_weather_" + detailWeather.getIcon(), "drawable", context.getPackageName());
            ivWeatherStatus.setImageResource(imageId);
            txtTemperature.setText(String.format(Locale.US, "%.1f℃", detailWeather.getTemperature()));
            txtDescription.setText(detailWeather.getDescription());
            txtRain.setText(String.format(Locale.US, "%.1fmm", detailWeather.getRain()));
            txtWind.setText(String.format(Locale.US, "%.1fmph", detailWeather.getWindSpeed()));
            txtHumidity.setText(String.format(Locale.US, "%.1f%%", detailWeather.getHumidity()));
            weatherForecastContainer.setVisibility(View.VISIBLE);
            adapter.update(detailWeather.getDailyWeathers());
        }
    }
}
