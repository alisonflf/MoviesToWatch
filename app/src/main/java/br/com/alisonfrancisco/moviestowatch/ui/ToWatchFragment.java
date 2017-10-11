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
import java.util.List;

import br.com.alisonfrancisco.moviestowatch.R;
import br.com.alisonfrancisco.moviestowatch.adapters.MoviesListAdapter;
import br.com.alisonfrancisco.moviestowatch.entities.Movie;
import br.com.alisonfrancisco.moviestowatch.entities.Movies;
import br.com.alisonfrancisco.moviestowatch.persistence.MyDataBaseContract;
import br.com.alisonfrancisco.moviestowatch.persistence.MySqlHelper;


public class ToWatchFragment extends Fragment {

    private MySqlHelper mySqlHelper;
    private ListView listView = null;

    public ToWatchFragment() {
        // Required empty public constructor
    }

    public static ToWatchFragment newInstance(String param1, String param2) {
        ToWatchFragment fragment = new ToWatchFragment();
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
        Movies movieList = new Movies();
        View view = inflater.inflate(R.layout.fragment_to_watch, container, false);
        listView = (ListView) view.findViewById(R.id.listToWatch);
        mySqlHelper = new MySqlHelper(getActivity());

        final MoviesListAdapter lstAdp = new MoviesListAdapter(getContext(), query().results);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(lstAdp);
            }
        });

        return view;
    }

    public Movies query() {
        Movies moviesList = new Movies();
        ArrayList<Movie> lista = new ArrayList<>();

        SQLiteDatabase db = mySqlHelper.getWritableDatabase();

        Cursor c = db.query(MyDataBaseContract.Movies.TABLE_NAME,
                new String[]{MyDataBaseContract.Movies.COL_ID,
                        MyDataBaseContract.Movies.COL_TITLE,
                        MyDataBaseContract.Movies.COL_OVERVIEW,
                        MyDataBaseContract.Movies.COL_WATCHED},
                " watched = 'N'",
                null, null, null, null);

        if (c != null && c.moveToFirst()) {
            //recupera índices das colunas
            int colIdIndex = c.getColumnIndex(MyDataBaseContract.Movies.COL_ID);
            int colTitleIndex = c.getColumnIndex(MyDataBaseContract.Movies.COL_TITLE);
            int colOverviewIndex = c.getColumnIndex(MyDataBaseContract.Movies.COL_OVERVIEW);

            do {
                Movie movie = new Movie();

                movie.id = c.getString(colIdIndex);
                movie.title = c.getString(colTitleIndex);
                movie.overview = c.getString(colOverviewIndex);

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
