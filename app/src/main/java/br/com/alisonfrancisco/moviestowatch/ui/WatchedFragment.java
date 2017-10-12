package br.com.alisonfrancisco.moviestowatch.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.alisonfrancisco.moviestowatch.R;
import br.com.alisonfrancisco.moviestowatch.adapters.WatchedListAdapter;
import br.com.alisonfrancisco.moviestowatch.entities.Movie;
import br.com.alisonfrancisco.moviestowatch.entities.Movies;
import br.com.alisonfrancisco.moviestowatch.persistence.MyDataBaseContract;
import br.com.alisonfrancisco.moviestowatch.persistence.MySqlHelper;


public class WatchedFragment extends Fragment {

    private MySqlHelper mySqlHelper;
    private ListView listView = null;

    public WatchedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watched, container, false);
        listView = (ListView) view.findViewById(R.id.listWatched);
        mySqlHelper = new MySqlHelper(getActivity());

        refreshListWatched();

        return view;
    }

    private void refreshListWatched() {
        Movies moviesList = new Movies();
        moviesList.results = query().results;

        if (moviesList.results != null) {
            final WatchedListAdapter lstAdp = new WatchedListAdapter(getContext(), moviesList.results);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(lstAdp);
                }
            });
        }
    }

    public Movies query() {
        Movies moviesList = new Movies();
        ArrayList<Movie> lista = new ArrayList<>();

        SQLiteDatabase db = mySqlHelper.getWritableDatabase();

        Cursor c = db.query(MyDataBaseContract.Movies.TABLE_NAME,
                new String[]{MyDataBaseContract.Movies.COL_ID,
                        MyDataBaseContract.Movies.COL_TITLE,
                        MyDataBaseContract.Movies.COL_OVERVIEW,
                        MyDataBaseContract.Movies.COL_POSTER_PATH,
                        MyDataBaseContract.Movies.COL_WATCHED},
                " watched = 'S'",
                null, null, null, null);

        if (c != null && c.moveToFirst()) {
            int colIdIndex = c.getColumnIndex(MyDataBaseContract.Movies.COL_ID);
            int colTitleIndex = c.getColumnIndex(MyDataBaseContract.Movies.COL_TITLE);
            int colOverviewIndex = c.getColumnIndex(MyDataBaseContract.Movies.COL_OVERVIEW);
            int colPosterPathIndex = c.getColumnIndex(MyDataBaseContract.Movies.COL_POSTER_PATH);

            do {
                Movie movie = new Movie();

                movie.id = c.getString(colIdIndex);
                movie.title = c.getString(colTitleIndex);
                movie.overview = c.getString(colOverviewIndex);
                movie.posterPath = c.getString(colPosterPathIndex);

                lista.add(movie);

            } while(c.moveToNext());

            moviesList.results = lista;
            c.close();
        }

        return moviesList;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
