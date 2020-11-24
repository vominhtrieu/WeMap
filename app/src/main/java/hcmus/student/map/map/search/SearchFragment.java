package hcmus.student.map.map.search;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import hcmus.student.map.R;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    final static int DELAY_TYPING = 500;
    Context context;
    SearchResultAdapter adapter;
    String query;
    Handler handler;
    Runnable userStopTypingChecker;
    long lastTimeTyping;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        this.lastTimeTyping = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null, false);

        final SearchView searchView = view.findViewById(R.id.svSearch);
        ListView lvSearchResult = view.findViewById(R.id.lvSearchResult);
        adapter = new SearchResultAdapter(context);
        lvSearchResult.setAdapter(adapter);
        searchView.setOnQueryTextListener(this);
        handler = new Handler();
        userStopTypingChecker = new Runnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - lastTimeTyping > DELAY_TYPING) {
                    adapter.search(query);
                }
            }
        };

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onQueryTextChange(String newText) {
        lastTimeTyping = System.currentTimeMillis();
        query = newText.toLowerCase();
        // Prevent user send too much API request
        handler.removeCallbacks(userStopTypingChecker);
        handler.postDelayed(userStopTypingChecker, DELAY_TYPING);
        return true;
    }
}
