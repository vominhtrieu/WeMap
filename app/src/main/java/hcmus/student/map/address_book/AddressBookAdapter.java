package hcmus.student.map.address_book;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;
import hcmus.student.map.model.Database;
import hcmus.student.map.model.Place;
import hcmus.student.map.utitlies.AddressChangeCallback;
import hcmus.student.map.utitlies.AddressLine;
import hcmus.student.map.utitlies.AddressProvider;
import hcmus.student.map.utitlies.OnAddressLineResponse;

public class AddressBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AddressChangeCallback {
    Context context;

    AddressProvider mAddressProvider;
    List<Place> places;
    Database mDatabase;


    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class AlphabetViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtAlphabet;

        public AlphabetViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAlphabet = itemView.findViewById(R.id.txtAlphabet);
        }
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        protected ImageView ivAvatar;
        protected TextView txtName;
        protected TextView txtAddressLine;
        protected ImageButton btnFavorite;
        protected ImageButton btnMore;
        protected ImageButton btnLocate;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            txtName = itemView.findViewById(R.id.txtName);
            txtAddressLine = itemView.findViewById(R.id.txtAddressLine);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnMore = itemView.findViewById(R.id.btnMore);
            btnLocate = itemView.findViewById(R.id.btn_list_item_locate);
        }
    }

    public AddressBookAdapter(Context context) {
        this.context = context;
        this.mAddressProvider = ((MainActivity) context).getAddressProvider();
        //this.mDatabase = new Database(context);
        this.places = new ArrayList<>();
        ((MainActivity) context).registerAddressChange(this);
        mDatabase = new Database(context);
        getUpdate();
    }

    public List<Place> groupAddress(List<Place> places) {
        if (places.size() == 0)
            return places;

        int index = 0;

        List<Place> tmp = new ArrayList<>();
        if (places.get(0).getFavorite().equals("1")) {
            tmp.add(new Place(0, null, null, null, null));
            while (index < places.size() && places.get(index).getFavorite().equals("1")) {
                tmp.add(places.get(index));
                index++;
            }
        }

        while (index < places.size()) {
            char c = Character.toUpperCase(places.get(index).getName().charAt(0));
            Place place = new Place(0, Character.toString(c), null, null, null);
            tmp.add(place);

            while (index < places.size() && Character.toUpperCase(places.get(index).getName().charAt(0)) == c) {
                tmp.add(places.get(index));
                index++;
            }
        }

        return tmp;
    }

    public void getUpdate() {
        places = mAddressProvider.getPlaces();
        places = groupAddress(places);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if (places.get(position).getLocation() == null) {
            if (places.get(position).getName() == null)
                return 0;
            return 1;
        }
        return 2;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        switch (viewType) {
            case 1:
                view = layoutInflater.inflate(R.layout.row_alphabet, parent, false);
                return new AlphabetViewHolder(view);
            case 2:
                view = layoutInflater.inflate(R.layout.row_place, parent, false);
                return new PlaceViewHolder(view);
            default:
                view = layoutInflater.inflate(R.layout.row_favorite_label, parent, false);
                return new FavoriteViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1)
            onBindAlphabetViewHolder((AlphabetViewHolder) holder, position);
        else if (holder.getItemViewType() == 2)
            onBindPlaceViewHolder((PlaceViewHolder) holder, position);

    }

    public void onBindAlphabetViewHolder(@NonNull AlphabetViewHolder holder, int position) {
        final Place place = places.get(position);
        holder.txtAlphabet.setText(place.getName());
    }

    public void onBindPlaceViewHolder(@NonNull PlaceViewHolder holder, final int position) {
        final Place place = places.get(position);
        final LatLng location = place.getLocation();

        final ImageView ivAvatar = holder.ivAvatar;
        final TextView txtAddressLine = holder.txtAddressLine;
        final TextView txtName = holder.txtName;
        final ImageButton btnFavorite = holder.btnFavorite;
        final ImageButton btnMore = holder.btnMore;
        final ImageButton btnLocate = holder.btnLocate;

        txtName.setText(place.getName());
        txtAddressLine.setText(R.string.txt_loading_address_line);

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

        if (place.getAvatar() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(place.getAvatar(), 0, place.getAvatar().length);
            ivAvatar.setBackground(new BitmapDrawable(context.getResources(), bmp));
        } else {
            ivAvatar.setBackgroundResource(R.drawable.ic_image);
        }


        final PopupMenu popupMenu = new PopupMenu(context, btnMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_address, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemEdit:
                        ((MainActivity) context).editPlaces(place);
                        break;
                    case R.id.itemDelete:
                        showDeleteDialog(position);
                        break;
                    case R.id.itemCancel:
                        break;
                }
                return false;
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });

        if (place.getFavorite().equals("1"))
            btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_red);
        else
            btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place.getFavorite().equals("0")) {
                    mAddressProvider.addFavorite(place.getId());
                    btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_red);
                } else {
                    mAddressProvider.removeFavorite(place.getId());
                    btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite);
                }
                getUpdate();
            }
        });

        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).locatePlace(place.getLocation());
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void searchForPlaces(String data) {
        places = mDatabase.searchForPlaces(data);
        places = groupAddress(places);
        notifyDataSetChanged();

    }

    private void showDeleteDialog(final int position) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(R.string.txtDeleteConfirm);
        final Place place = places.get(position);
        alertDialogBuilder.setPositiveButton(R.string.txtYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                mAddressProvider.deletePlace(place.getId());
            }
        }).setNegativeButton(R.string.txtNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.show();
    }

    @Override
    public void onAddressInsert(Place place) {
        getUpdate();
    }

    @Override
    public void onAddressUpdate(Place place) {
        getUpdate();
    }

    @Override
    public void onAddressDelete(int placeId) {
        getUpdate();
    }

}