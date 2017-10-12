package br.com.alisonfrancisco.moviestowatch.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.alisonfrancisco.moviestowatch.R;
import br.com.alisonfrancisco.moviestowatch.adapters.ToWatchListAdapter;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_watch, container, false);
        listView = (ListView) view.findViewById(R.id.listToWatch);
        mySqlHelper = new MySqlHelper(getActivity());

        refreshListToWatch();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = (Movie) adapterView.getItemAtPosition(i);

                if (!movie.id.isEmpty()) {
                    markAsWatched(movie.id);
                    refreshListToWatch();
                }
            }
        });

        return view;
    }

    public void refreshListToWatch(){
        Movies moviesList = new Movies();
        moviesList.results = query().results;

        if (moviesList.results != null) {
            final ToWatchListAdapter lstAdp = new ToWatchListAdapter(getContext(), moviesList.results);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(lstAdp);
                }
            });
        }
    }

    public void markAsWatched(String pId) {
        SQLiteDatabase db = mySqlHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDataBaseContract.Movies.COL_WATCHED, "S");
        db.update(MyDataBaseContract.Movies.TABLE_NAME,
                contentValues,
                " _id = ? ",
                new String[]{pId});

        db.close();
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
                " watched = 'N'",
                null, null, null, null);

        if (c != null && c.moveToFirst()) {
            //recupera índices das colunas
            int colIdIndex = c.getColumnIndex(MyDataBaseContract.Movies.COL_ID);
            int colTitleIndex = c.getColumnIndex(MyDataBaseContract.Movies.COL_TITLE);
            int colOverviewIndex = c.getColumnIndex(MyDataBaseContract.Movies.COL_OVERVIEW);
            int colPosterPathindex = c.getColumnIndex(MyDataBaseContract.Movies.COL_POSTER_PATH);

            do {
                Movie movie = new Movie();

                movie.id = c.getString(colIdIndex);
                movie.title = c.getString(colTitleIndex);
                movie.overview = c.getString(colOverviewIndex);
                movie.posterPath = c.getString(colPosterPathindex);

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
