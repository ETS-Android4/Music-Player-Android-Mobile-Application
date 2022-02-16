package com.example.musicplayer;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.RequiresApi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PlaySelectedSong extends AppCompatActivity {

    private TextView display;
    private Button startmusic, stopmusic;
    private BroadcastReceiver receiver = new MyReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playmusic);
        Intent intent = getIntent();
        String song = intent.getStringExtra("Song");
        int pos = intent.getIntExtra("Position",0);
        display = (TextView)findViewById(R.id.song);
        String data = "Playing " + song;
        display.setText(data);
        startmusic = (Button)findViewById(R.id.start1);
        stopmusic = (Button) findViewById(R.id.stop1);
        Intent startintent = new Intent(this, MusicPlayerApp.class);
        Intent stopintent = new Intent(this, MusicPlayerApp.class);

        startmusic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(MusicPlayerApp.class)){
                    stopService(stopintent);
                }
                Log.d("Run", "Start Service");
                startintent.putExtra("Number", pos);
                startintent.putExtra("Song", song);
                startForegroundService(startintent);
            }
        });
        stopmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Run", "Stop Service");
                stopService(stopintent);
            }
        });

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(receiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
