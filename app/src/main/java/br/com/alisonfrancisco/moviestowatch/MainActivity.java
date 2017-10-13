package br.com.alisonfrancisco.moviestowatch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.Calendar;

import br.com.alisonfrancisco.moviestowatch.receiver.AlarmReceiver;
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
        setNotificationAlarm();

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

    private void setNotificationAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(MainActivity.this,
                AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
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

}
