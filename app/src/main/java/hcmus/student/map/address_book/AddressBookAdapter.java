package hcmus.student.map.address_book;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import hcmus.student.map.R;
import hcmus.student.map.model.Database;
import hcmus.student.map.model.Place;
import hcmus.student.map.utitlies.AddressLine;
import hcmus.student.map.utitlies.OnAddressLineResponse;

public class AddressBookAdapter extends BaseAdapter {
    Database mDatabase;
    Context context;
    List<Place> places;

    public AddressBookAdapter(Context context) {
        this.context = context;
        this.mDatabase = new Database(context);
        this.places = new ArrayList<>();
    }

    public void getUpdate() {
        places = mDatabase.getAllPlaces();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Place getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_place, null, false);
        }

        TextView txtListItemName = convertView.findViewById(R.id.txtListItemName);
        final TextView txtListItemAddressLine = convertView.findViewById(R.id.txtListItemAddressLine);
        txtListItemAddressLine.setText(R.string.txtLoadingAddressLine);
        Place place = getItem(position);

        txtListItemName.setText(place.getName());
        LatLng location = place.getLocation();

        AddressLine addressLine = new AddressLine(new Geocoder(context), new OnAddressLineResponse() {
            @Override
            public void onAddressLineResponse(String addressLine) {
                if (addressLine != null) {
                    txtListItemAddressLine.setText(addressLine);
                } else {
                    txtListItemAddressLine.setText(R.string.txtNullLocation);
                }
            }
        });
        addressLine.execute(location);

        if (place.getAvatar() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(place.getAvatar(), 0, place.getAvatar().length);
            ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);
            ivAvatar.setBackground(new BitmapDrawable(context.getResources(), bmp));
        }
        return convertView;
    }

}
