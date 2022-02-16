package com.example.musicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.net.URI;

public class MusicPlayerApp extends Service {
    MediaPlayer mplayer;
    Context context = this;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int pos = (int) intent.getExtras().get("Number");
        String song = (String) intent.getExtras().get("Song");
        Toast.makeText(this, "Service Playing", Toast.LENGTH_SHORT).show();
        Log.d("Run", "onStartCommand: ");
        Log.d("Run", "Pos" + pos);
        if (pos == 0)
            mplayer = MediaPlayer.create(this, R.raw.song_1);
        else if (pos == 1)
            mplayer = MediaPlayer.create(this, R.raw.song_2);
        else if (pos == 2)
            mplayer = MediaPlayer.create(this, R.raw.song_3);
        else if (pos == 3)
            mplayer = MediaPlayer.create(this, R.raw.song_4);
        else if (pos == 4)
            mplayer = MediaPlayer.create(this, R.raw.song_5);
        else if (pos == 5){
            File file = new File(context.getFilesDir(), "/download.mp3");
            Log.d("Run", "file" + file);
            mplayer = MediaPlayer.create(this, Uri.fromFile(file));
            song = "Downloaded Song";
        }
        mplayer.setLooping(false);
        mplayer.start();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("Channelid1", "Music Playing", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
        Intent inten = new Intent(this, MainActivity.class);
        Log.d("Run", "Notification");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, inten, 0);
        Notification notification=new NotificationCompat.Builder(this,"Channelid1").setContentTitle("Music Player")
                .setContentText("Playing " + song)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
        Log.d("Run", "oncreateCommand: ");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("Run", "onStopCommand: ");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        mplayer.stop();
    }

}
