package hcmus.student.map.map.search;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;
import hcmus.student.map.database.Place;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    Context context;
    SearchResultAdapter adapter;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null, false);

        SearchView searchView = view.findViewById(R.id.svSearch);
        ListView lvSearchResult = view.findViewById(R.id.lvSearchResult);
        adapter = new SearchResultAdapter(context);
        lvSearchResult.setAdapter(adapter);
        searchView.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        adapter.search(newText);
        return true;
    }
}
