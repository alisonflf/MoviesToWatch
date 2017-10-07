package br.com.alisonfrancisco.moviestowatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.alisonfrancisco.moviestowatch.ui.SearchFragment;
import br.com.alisonfrancisco.moviestowatch.ui.ToWatchFragment;
import br.com.alisonfrancisco.moviestowatch.ui.WatchedFragment;

public class MainActivity extends AppCompatActivity {

    private SearchFragment searchFragment;
    private ToWatchFragment toWatchFragment;
    private WatchedFragment watchedFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    changeFragment(searchFragment);
                    return true;
                case R.id.navigation_to_watch:
                    changeFragment(toWatchFragment);
                    return true;
                case R.id.navigation_watched:
                    changeFragment(watchedFragment);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.content) == null) {
            searchFragment = new SearchFragment();
            toWatchFragment = new ToWatchFragment();
            watchedFragment = new WatchedFragment();

            fragmentManager.beginTransaction().add(R.id.content, toWatchFragment).commit();
        } else {
            buildFragments(fragmentManager);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void buildFragments(FragmentManager pfragmentManager){
        if (pfragmentManager.findFragmentById(R.id.content) instanceof SearchFragment) {
            searchFragment = (SearchFragment) pfragmentManager.findFragmentById(R.id.content);
            toWatchFragment = new ToWatchFragment();
            watchedFragment = new WatchedFragment();
        } else if (pfragmentManager.findFragmentById(R.id.content) instanceof ToWatchFragment) {
            searchFragment = new SearchFragment();
            toWatchFragment = (ToWatchFragment) pfragmentManager.findFragmentById(R.id.content);
            watchedFragment = new WatchedFragment();
        } else if (pfragmentManager.findFragmentById(R.id.content) instanceof WatchedFragment) {
            searchFragment = new SearchFragment();
            toWatchFragment = new ToWatchFragment();
            watchedFragment = (WatchedFragment) pfragmentManager.findFragmentById(R.id.content);
        }
    }

    private void changeFragment(Fragment pFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, pFragment).commit();
    }

    public static boolean isConnected(Context ctx){
        ConnectivityManager connectivityManager= (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        } else {
            return false;
        }
    }

    public class ConnectivityChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (!isConnected(context)) {
                Toast.makeText(context, R.string.msgNoConection, Toast.LENGTH_LONG);
            }
        }
    }
}
