package br.com.alisonfrancisco.moviestowatch.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.alisonfrancisco.moviestowatch.R;
import br.com.alisonfrancisco.moviestowatch.entities.Movie;

/**
 * Created by AlisonFrancisco on 11/10/2017.
 */

public class ToWatchListAdapter extends ArrayAdapter<Movie> {

    public ToWatchListAdapter(Context context, List<Movie> movies) {
        super(context,0,movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie obj = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.adp_towatch, parent, false);
        }

        ImageView img = (ImageView)convertView.findViewById(R.id.img);
        TextView txtTitle = (TextView)convertView.findViewById(R.id.txtTitle);
        TextView txtOverview = (TextView)convertView.findViewById(R.id.txtOverview);

        String urlImg = "https://image.tmdb.org/t/p/w500" + obj.posterPath;

        txtTitle.setText(obj.title);
        txtOverview.setText(obj.overview);

        Picasso.with(getContext())
                .load(urlImg)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(img);

        return convertView;
    }

}
