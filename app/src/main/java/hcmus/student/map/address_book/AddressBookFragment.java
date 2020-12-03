package hcmus.student.map.address_book;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ListView;

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

        normalAdapter = new NormalAddressAdapter(activity);
        rvAddress.setAdapter(normalAdapter);
        rvAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAddress.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        normalAdapter.getUpdate();
//        addressFavoriteAdapter.getUpdate();
    }

        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}