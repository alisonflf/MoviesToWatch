package br.com.alisonfrancisco.moviestowatch.ui;

import java.io.IOException;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
    private ListView listView = null;
    private MySqlHelper mySqlHelper;
    private AlertDialog alerta;
    private SearchView searchView;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        listView = (ListView) view.findViewById(R.id.listResult);
        mySqlHelper = new MySqlHelper(getActivity());

        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.clearFocus();

        if (savedInstanceState != null) {
            String search = savedInstanceState.getString(STATE_SEARCH);

            if (search != null && !search.isEmpty()) {
                searchMovies(search);
            }
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMovies(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Movie movie = (Movie) adapterView.getItemAtPosition(i);

                if (!movie.id.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.msgWatchLaterQuestion);

                    builder.setPositiveButton(R.string.msgYes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                            try {
                                insert(movie);
                                Toast.makeText(getActivity(), R.string.msgMovieAdded, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), R.string.msgErroRecording, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    builder.setNegativeButton(R.string.msgNo, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            //
                        }
                    });

                    alerta = builder.create();
                    alerta.show();
                }
            }
        });

        listView.requestFocus();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!searchView.getQuery().toString().isEmpty()){
            searchMovies(searchView.getQuery().toString());
        }

//        if (!isEmpty(searchEdit)) {
//            searchMovies();
//        }
    }

    public void insert(Movie pMovie) throws SQLiteConstraintException {
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
        savedInstanceState.putString(STATE_SEARCH, searchView.getQuery().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @javax.annotation.ParametersAreNonnullByDefault
    public void searchMovies(String pSearchString) {
        TBMD tbmdAPI = new TBMD(ARG_API_KEY);
        try {
            tbmdAPI.search(pSearchString).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("e", e.getMessage());
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
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
                        } catch (Exception e) {
                            Log.d("API Class", "exception: " + e.getMessage());
                        }
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
