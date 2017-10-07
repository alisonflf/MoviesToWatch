package br.com.alisonfrancisco.moviestowatch.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.alisonfrancisco.moviestowatch.R;


public class WatchedFragment extends Fragment {

    public WatchedFragment() {
        // Required empty public constructor
    }

    public static WatchedFragment newInstance() {
        WatchedFragment fragment = new WatchedFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watched, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
