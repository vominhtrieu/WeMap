package hcmus.student.map.address_book;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;
import hcmus.student.map.model.Database;
import hcmus.student.map.model.Place;

public class AddressBookFragment extends Fragment {
    private MainActivity activity;
    Database mDatabase;
    NormalAddressAdapter normalAdapter;

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
        RecyclerView rvAddress = v.findViewById(R.id.rvAddress);
        SearchView svSearch = v.findViewById(R.id.svSearchContact);
        normalAdapter = new NormalAddressAdapter(activity);
        rvAddress.setAdapter(normalAdapter);
        rvAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAddress.setItemAnimator(new DefaultItemAnimator());


        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                normalAdapter.searchPlaceContact(newText);
                return true;
            }
        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        normalAdapter.getUpdate();
    }
}