package com.example.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Run","Receiver");
        if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())){
            Toast.makeText(context, "Battery Low", Toast.LENGTH_LONG).show();
        }
        if (Intent.ACTION_BATTERY_OKAY.equals(intent.getAction())){
            Toast.makeText(context, "Battery Okay", Toast.LENGTH_LONG).show();
        }
        if (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())){
            Toast.makeText(context, "Power Disconnected", Toast.LENGTH_LONG).show();
        }
    }
}
