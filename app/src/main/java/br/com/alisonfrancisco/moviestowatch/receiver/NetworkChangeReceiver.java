package br.com.alisonfrancisco.moviestowatch.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import br.com.alisonfrancisco.moviestowatch.R;


public class NetworkChangeReceiver extends BroadcastReceiver {

    public static final String ACTION = ".receiver.MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try
        {
            if (isOnline(context)) {
                Toast.makeText(context, R.string.msgConnectionRestored , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, R.string.msgNoConection , Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
