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
    AddressBookAdapter adapter;
    AddressFavoriteAdapter adapter1;

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
        adapter = new AddressBookAdapter(activity);
        lvAddress.setAdapter(adapter);

        ListView lvFavorite = v.findViewById(R.id.lvFavorite);
        adapter1 = new AddressFavoriteAdapter(activity);
        lvFavorite.setAdapter(adapter1);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.getUpdate();
        adapter1.getUpdate();
    }
}