package br.com.alisonfrancisco.moviestowatch.ui;

import java.io.IOException;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import br.com.alisonfrancisco.moviestowatch.persistence.MyDataBaseContract;
import br.com.alisonfrancisco.moviestowatch.persistence.MySqlHelper;
import okhttp3.Call;
import okhttp3.Callback;

import br.com.alisonfrancisco.moviestowatch.adapters.MoviesListAdapter;
import br.com.alisonfrancisco.moviestowatch.api.TBMD;
import br.com.alisonfrancisco.moviestowatch.R;
import br.com.alisonfrancisco.moviestowatch.entities.Movie;
import br.com.alisonfrancisco.moviestowatch.entities.Movies;

public class SearchFragment extends Fragment {
    private static final String ARG_API_KEY = "4277f0776300025895ed7999e22fb605";
    static final String STATE_SEARCH = "edtSearchTest";
    private Button buttonSearch = null;
    private EditText searchEdit = null;
    private ListView listView = null;
    private MySqlHelper mySqlHelper;

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
        buttonSearch = (Button) view.findViewById(R.id.buttonSearch);
        searchEdit = (EditText) view.findViewById(R.id.edtSearchQuery);
        listView = (ListView) view.findViewById(R.id.listResult);
        mySqlHelper = new MySqlHelper(getActivity());

        if (savedInstanceState != null) {
            String search = savedInstanceState.getString(STATE_SEARCH);

            if (!search.isEmpty()) {
                searchEdit.setText(search);
                searchMovies();
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = (Movie) adapterView.getItemAtPosition(i);

                if (!movie.id.isEmpty()) {
                    insert(movie);
                }
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(searchEdit)) {
                    searchMovies();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isEmpty(searchEdit)) {
            searchMovies();
        }
    }

    public void insert(Movie pMovie) {
        SQLiteDatabase db = mySqlHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDataBaseContract.Movies.COL_ID, pMovie.id );
        contentValues.put(MyDataBaseContract.Movies.COL_OVERVIEW, pMovie.overview );
        contentValues.put(MyDataBaseContract.Movies.COL_TITLE, pMovie.title );
        contentValues.put(MyDataBaseContract.Movies.COL_POSTER_PATH, pMovie.posterPath );
        contentValues.put(MyDataBaseContract.Movies.COL_WATCHED, "N" );
        db.insert(MyDataBaseContract.Movies.TABLE_NAME, null, contentValues);

        db.close();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STATE_SEARCH, searchEdit.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    public void searchMovies() {
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
                                listView.setAdapter(lstAdp);
                            }
                        });

                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
