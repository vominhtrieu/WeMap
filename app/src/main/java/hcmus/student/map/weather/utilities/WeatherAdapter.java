package hcmus.student.map.weather.utilities;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hcmus.student.map.R;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    List<DailyWeather> dailyWeatherList;
    Calendar calendar;
    Context context;

    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView txtDayOfWeek, txtMinTemp, txtMaxTemp;
        ImageView ivWeatherStatus;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDayOfWeek = itemView.findViewById(R.id.txtDayOfWeek);
            ivWeatherStatus = itemView.findViewById(R.id.ivWeatherStatus);
            txtMinTemp = itemView.findViewById(R.id.txtMinTemp);
            txtMaxTemp = itemView.findViewById(R.id.txtMaxTemp);
        }
    }

    public WeatherAdapter(Context context) {
        dailyWeatherList = new ArrayList<>();
        calendar = Calendar.getInstance();
        this.context = context;
    }

    public void update(List<DailyWeather> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
        this.calendar = Calendar.getInstance();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_status, parent, false);
        return new WeatherViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        DailyWeather dailyWeather = dailyWeatherList.get(position);
        position += 1;

        calendar.add(Calendar.DATE, position);

        Date date = calendar.getTime();

        calendar.add(Calendar.DATE, -position);

        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }

        String dayOfWeek = new SimpleDateFormat("EE", locale).format(date.getTime());

        holder.txtDayOfWeek.setText(dayOfWeek);
        holder.ivWeatherStatus.setImageBitmap(dailyWeather.getIcon());
        holder.txtMinTemp.setText(String.format(Locale.US, "%.1f°", dailyWeather.getMinTemp()));
        holder.txtMaxTemp.setText(String.format(Locale.US, "%.1f°", dailyWeather.getMaxTemp()));
    }

    @Override
    public int getItemCount() {
        return dailyWeatherList.size();
    }
}
