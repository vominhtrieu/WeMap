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

import java.io.IOException;
import java.util.ArrayList;

import hcmus.student.map.R;
import hcmus.student.map.database.Database;
import hcmus.student.map.database.Place;

public class AddressBookAdapter extends BaseAdapter {
    Database mDatabase;
    Context context;
    ArrayList<Place> places;

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
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.row_place, null);
        TextView txtListItemName = convertView.findViewById(R.id.txt_list_item_name);
        TextView txtListItemAddressLine = convertView.findViewById(R.id.txt_list_item_address_line);
        Place place = getItem(position);

        txtListItemName.setText(place.getName());
        Geocoder geocoder = new Geocoder(context);
        try {
            String addressLine = geocoder.getFromLocation(place.getLatitude(), place.getLongitude(), 1).get(0).getAddressLine(0);
            txtListItemAddressLine.setText(addressLine);
        } catch (IOException e) {
            txtListItemAddressLine.setText("");
        }
        if (place.getAvatar() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(place.getAvatar(), 0, place.getAvatar().length);
            ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);
            ivAvatar.setBackground(new BitmapDrawable(context.getResources(), bmp));
        }
        return convertView;
    }
}
