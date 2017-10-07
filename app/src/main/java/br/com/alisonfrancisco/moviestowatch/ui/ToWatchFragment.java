package br.com.alisonfrancisco.moviestowatch.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.alisonfrancisco.moviestowatch.R;

public class ToWatchFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_to_watch, container, false);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
