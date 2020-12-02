package hcmus.student.map.address_book;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import hcmus.student.map.R;
import hcmus.student.map.model.Database;
import hcmus.student.map.model.Place;

public class AddressFavoriteAdapter extends BaseAdapter {
    Database mDatabase;
    Context context;
    List<Place> placesFav;
    AddressBookAdapter updateAdapter;


    public AddressFavoriteAdapter(Context context) {
        this.context = context;
        placesFav = new ArrayList<>();
        this.mDatabase = new Database(context);
    }

    public void setUpdateAdapter(AddressBookAdapter updateAdapter) {
        this.updateAdapter = updateAdapter;
    }

    public void getUpdate() {
        placesFav = mDatabase.getPlacesFavorite();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return placesFav.size();
    }

    @Override
    public Place getItem(int position) {
        return placesFav.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.row_place, null);
        TextView txtListItemName = convertView.findViewById(R.id.txtName);
        TextView txtListItemLatLng = convertView.findViewById(R.id.txtAddressLine);
        final Place place = getItem(position);

        txtListItemName.setText(place.getName());
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        LatLng location = place.getLocation();
        txtListItemLatLng.setText(formatter.format("(%.2f, %.2f)", location.latitude, location.longitude).toString());
        if (place.getAvatar() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(place.getAvatar(), 0, place.getAvatar().length);
            ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);
            ivAvatar.setBackground(new BitmapDrawable(context.getResources(), bmp));
        }

        final Button btnFavorite = convertView.findViewById(R.id.btnFavorite);
        btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_red);

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (place.getFavorite().equals("1")) {
                    LatLng location = place.getLocation();
                    mDatabase.removeFavorite(location.latitude, location.longitude);
                    btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite);
                    getUpdate();
                    updateAdapter.getUpdate();
                }
            }
        });
        return convertView;
    }
}