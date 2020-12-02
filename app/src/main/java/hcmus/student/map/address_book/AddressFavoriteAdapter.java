package hcmus.student.map.address_book;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.List;

import hcmus.student.map.R;
import hcmus.student.map.model.Database;
import hcmus.student.map.model.Place;

public class AddressFavoriteAdapter extends BaseAdapter {
    Database mDatabase;
    Context context;
    List<Place> places;


    public AddressFavoriteAdapter(Context context) {
        this.context = context;
        this.mDatabase = new Database(context);
        this.places = new ArrayList<>();
    }
    public void upDateContacts(List<Place>places)
    {

        for (int i = 0; i <places.size(); i++) {

            if(places.get(i).getLocation()==null)
            {
                places.remove(i);
                i--;
            }

        }
    }
    Comparator<Place> compareById = new Comparator<Place>() {
        @Override
        public int compare(Place o1, Place o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }


    };
    public void sortPlaces(List<Place>places)
    {
        Collections.sort(places,compareById);
    }

    public List<Place> groupContacts(List<Place>places)
    {

        if(places.size()==0)
            return places;
        List<Place>tmp = new ArrayList<>();
        sortPlaces(places);

        char c = Character.toUpperCase(places.get(0).getName().charAt(0));
        Place place = new Place(Character.toString(c),null, null,"");
        tmp.add(place);

        for(int i=0;i<places.size();i++)
        {
            if(Character.toUpperCase(places.get(i).getName().charAt(0))!=c)
            {
                c = Character.toUpperCase(places.get(i).getName().charAt(0));

                place = new Place(Character.toString(c),null, null,"");
                tmp.add(place);
            }

            tmp.add(places.get(i));
        }

        return tmp;
    }
    public void getUpdate() {
        places = mDatabase.getPlacesFavorite();
        places = groupContacts(places);
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

        if (place.getAvatar() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(place.getAvatar(), 0, place.getAvatar().length);
            ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);
            ivAvatar.setBackground(new BitmapDrawable(context.getResources(), bmp));
        }

        final Button btnFavorite = convertView.findViewById(R.id.btnFavorite);

        if (place.getFavorite().equals("1"))
            btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_red);

        if(place.getLocation()==null)
        {
            ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);
            ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);
            ImageButton btnLocate = convertView.findViewById(R.id.btn_list_item_locate);

            txtListItemName.setTypeface(txtListItemName.getTypeface(), Typeface.BOLD);
            txtListItemName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

            ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);
            ivAvatar.setVisibility(View.GONE);
            txtListItemLatLng.setVisibility(View.GONE);

            btnFavorite.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            btnLocate.setVisibility(View.GONE);

        }
        else
            txtListItemLatLng.setText(formatter.format("(%.2f, %.2f)", location.latitude, location.longitude).toString());
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (place.getFavorite().equals("0")) {
                    place.setFavorite("1");
                    LatLng location = place.getLocation();
                    mDatabase.addFavorite(location.latitude, location.longitude);
                    btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_red);
                } else {
                    place.setFavorite("0");
                    LatLng location = place.getLocation();
                    mDatabase.removeFavorite(location.latitude, location.longitude);
                    btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite);
                }
            }
        });
        return convertView;
    }
}
