package hcmus.student.map.address_book;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import hcmus.student.map.MainActivity;
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
        places = mDatabase.getPlacesNormal();
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
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_place, null, false);
        }

        final TextView txtName = convertView.findViewById(R.id.txtName);
        final TextView txtAddressLine = convertView.findViewById(R.id.txtAddressLine);
        final Button btnFavorite = convertView.findViewById(R.id.btnFavorite);

        txtAddressLine.setText(R.string.txtLoadingAddressLine);
        final Place place = getItem(position);
        LatLng location = place.getLocation();

        AddressLine addressLine = new AddressLine(new Geocoder(context), new OnAddressLineResponse() {
            @Override
            public void onAddressLineResponse(String addressLine) {
                if (addressLine != null) {
                    txtAddressLine.setText(addressLine);
                } else {
                    txtAddressLine.setText(R.string.txtNullLocation);
                }
            }
        });


        addressLine.execute(location);
        txtName.setText(place.getName());

        if (place.getAvatar() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(place.getAvatar(), 0, place.getAvatar().length);
            ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);
            ivAvatar.setBackground(new BitmapDrawable(context.getResources(), bmp));

        }
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);
        ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure,You wanted to delete an address?");

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mDatabase.deletePlace(place);
                        places.remove(position);
                        notifyDataSetChanged();
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialogBuilder.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_edit);
                final EditText edtNewName = dialog.findViewById(R.id.edtNewName);

                Button btnOK = dialog.findViewById(R.id.btnOK);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        place.setName(edtNewName.getText().toString());
                        mDatabase.editPlace(place);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });


        ImageButton btnLocate = convertView.findViewById(R.id.btn_list_item_locate);
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).locatePlace(place.getLocation());
            }
        });

        if (place.getFavorite().equals("1"))
            btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_red);
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
