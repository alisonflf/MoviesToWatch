package br.com.alisonfrancisco.moviestowatch.ui;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
    private AlertDialog alert;

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
                final Movie movie = (Movie) adapterView.getItemAtPosition(i);

                if (!movie.id.isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.msgMovieWatched);

                    builder.setPositiveButton(R.string.msgYes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                            try {
                                markAsWatched(movie.id);
                                refreshListToWatch();
                                Toast.makeText(getActivity(), R.string.msgMovieMarkedWatched, Toast.LENGTH_SHORT).show();
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

                    alert = builder.create();
                    alert.show();
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
        } else {
            final ToWatchListAdapter lstAdp = new ToWatchListAdapter(getContext(), new ArrayList<Movie>());

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
