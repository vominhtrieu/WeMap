package hcmus.student.map.address_book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;

public class AddressBookFragment extends Fragment {
    private MainActivity activity;
    AddressBookAdapter normalAdapter;
    AddressFavoriteAdapter addressFavoriteAdapter;

    public static AddressBookFragment newInstance() {
        AddressBookFragment fragment = new AddressBookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_address_book, container, false);
        ListView lvAddress = v.findViewById(R.id.lvAddress);
        ListView lvFavorite = v.findViewById(R.id.lvFavorite);

        addressFavoriteAdapter = new AddressFavoriteAdapter(activity);
        normalAdapter = new AddressBookAdapter(activity);

        addressFavoriteAdapter.setUpdateAdapter(normalAdapter);
        normalAdapter.setUpdateAdapter(addressFavoriteAdapter);

        lvAddress.setAdapter(normalAdapter);
        lvFavorite.setAdapter(addressFavoriteAdapter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        normalAdapter.getUpdate();
        addressFavoriteAdapter.getUpdate();
    }
}