package hcmus.student.map.address_book;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;

public class AddressBookFragment extends Fragment {
    private MainActivity activity;
    AddressBookAdapter normalAdapter;

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

        //Change SearchView color
        try {

            int searchMagIconId = svSearch.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
            ImageView searchMagIcon = svSearch.findViewById(searchMagIconId);
            searchMagIcon.setColorFilter(Color.WHITE);

            int searchSrcTextId = svSearch.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView textView = (TextView) svSearch.findViewById(searchSrcTextId);
            textView.setTextColor(Color.WHITE);
            textView.setHintTextColor(Color.WHITE);

            int searchCloseBtnId = svSearch.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
            ImageView searchCloseBtn = svSearch.findViewById(searchCloseBtnId);
            searchCloseBtn.setColorFilter(Color.WHITE);
        } catch (Exception ignored) {

        }

        normalAdapter = new AddressBookAdapter(activity);
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
                normalAdapter.searchForPlaces(newText);
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