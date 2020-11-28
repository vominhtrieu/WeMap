package hcmus.student.map.address_book;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Formatter;

import hcmus.student.map.database.Database;
import hcmus.student.map.database.Place;
import hcmus.student.map.R;

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
        TextView txtListItemLatLng = convertView.findViewById(R.id.txt_list_item_lat_lng);
        Place place = getItem(position);


        txtListItemName.setText(place.getName());
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
//        txtListItemLatLng.setText(formatter.format("(%.2f, %.2f)", place.getLatitude(), place.getLongitude()).toString());
        if(place.getLatitude()==null)
        {
            txtListItemLatLng.setText("");
            txtListItemName.setTypeface(txtListItemName.getTypeface(), Typeface.BOLD);

            txtListItemName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        }
        else
            txtListItemLatLng.setText(formatter.format("(%.2f, %.2f)", place.getLatitude(), place.getLongitude()).toString());
        if (place.getAvatar() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(place.getAvatar(), 0, place.getAvatar().length);
            ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);
            ivAvatar.setBackground(new BitmapDrawable(context.getResources(), bmp));
        }
        else
        {
            ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);
            ivAvatar.setVisibility(View.GONE);
            ImageButton imageButton = convertView.findViewById(R.id.btn_list_item_locate);
            imageButton.setVisibility(View.GONE);
        }

        return convertView;
    }

}
