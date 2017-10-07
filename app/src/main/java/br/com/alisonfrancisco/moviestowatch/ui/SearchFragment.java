package br.com.alisonfrancisco.moviestowatch.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;

import br.com.alisonfrancisco.moviestowatch.adapters.MoviesListAdapter;
import br.com.alisonfrancisco.moviestowatch.api.TBMD;
import br.com.alisonfrancisco.moviestowatch.R;
import br.com.alisonfrancisco.moviestowatch.entities.Movies;
import okhttp3.Call;
import okhttp3.Callback;

public class SearchFragment extends Fragment {
    private static final String ARG_API_KEY = "4277f0776300025895ed7999e22fb605";
    private Button buttonSearch = null;
    private EditText searchEdit = null;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.listResult);
        buttonSearch = (Button) view.findViewById(R.id.buttonSearch);
        searchEdit = (EditText) view.findViewById(R.id.edtSearchQuery);

        if (!TextUtils.isEmpty(searchEdit.getText())) {
            searchMovies(listView);
        }

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMovies(listView);
            }
        });

        return view;
    }

    public void searchMovies(final ListView pListView) {
        TBMD tbmdAPI = new TBMD(ARG_API_KEY);
        try {
            //TODO buscar
            tbmdAPI.search(searchEdit.getText().toString()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("e", e.getMessage());
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        String body = response.body().string();
                        Movies movies = gson.fromJson(body, Movies.class);

                        final MoviesListAdapter lstAdp = new MoviesListAdapter(getContext(), movies.results);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pListView.setAdapter(lstAdp);
                            }
                        });

                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
