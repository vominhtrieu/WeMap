package hcmus.student.map.map.search;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hcmus.student.map.R;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    Context context;
    SearchResultAdapter adapter;
    SearchClickCallback delegate;

    public static SearchFragment newInstance(SearchClickCallback delegate) {
        Bundle args = new Bundle();
        args.putParcelable("delegate", (Parcelable) delegate);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
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

        final SearchView searchView = view.findViewById(R.id.svSearch);
        RecyclerView lvSearchResult = view.findViewById(R.id.lvSearchResult);
        adapter = new SearchResultAdapter(context, delegate);
        lvSearchResult.setAdapter(adapter);
        lvSearchResult.setLayoutManager(new LinearLayoutManager(context));
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
        adapter.search(newText);
        return true;
    }
}
