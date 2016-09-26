package com.android.msahakyan.fma.util;

/**
 * Created by msahakyan on 03/08/16.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.msahakyan.fma.R;

public class ConnectivityUtil {

    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void notifyNoConnection(Context context) {
        Toast.makeText(context, R.string.no_connection, Toast.LENGTH_SHORT).show();
    }

    public static boolean isConnectedToWiFi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiConnection != null && wifiConnection.isConnected();
    }

    public static AlertDialog showConnectionDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_connection_title)
            .setMessage(R.string.dialog_connection_message)
            .setCancelable(true)
            .setNegativeButton(R.string.dialog_connection_button_negative, (dialog, which) -> {
                /* do nothing*/
            })
            .setPositiveButton(R.string.dialog_connection_button_positive, (dialog, which) -> {
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            });
        return builder.create();
    }

}
