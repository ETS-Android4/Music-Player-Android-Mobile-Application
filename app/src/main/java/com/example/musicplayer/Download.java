package com.example.musicplayer;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Download extends AppCompatActivity {
    private Button startmusic,stopmusic;
    private BroadcastReceiver receiver = new MyReceiver();
    private TextView downloads;
    private int done = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Status","Entered");
        setContentView(R.layout.downloadmusic);
        downloads = (TextView)findViewById(R.id.downloadstatus);
        if(savedInstanceState != null){
            done = savedInstanceState.getInt("answer");
        }
        if(isNetworkAvailable()){
            Toast.makeText(Download.this, "" +"Connected to The Internet", Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
            String url = intent.getStringExtra("Data");
            Log.d("Run", "Starting Download");
            if (done == 0) {
                new SongDownload(this).execute(url);
            }
            else{
                downloads.setText("The File is Downloaded. Play the Song! ");
            }
        }
        else{
            Toast.makeText(Download.this, "Not Connected to the Internet", Toast.LENGTH_SHORT).show();
            if (done == 1){
                downloads.setText("The File is Downloaded. Play the Song! ");
            }
        }
        startmusic = (Button) findViewById(R.id.start2);
        stopmusic = (Button) findViewById(R.id.stop2);
        Intent startintent = new Intent(this, MusicPlayerApp.class);
        Intent stopintent = new Intent(this, MusicPlayerApp.class);

        startmusic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(MusicPlayerApp.class)) {
                    stopService(stopintent);
                }
                int pos = 5;
                Log.d("Run", "Start Service");
                startintent.putExtra("Number", pos);
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
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        return (networkinfo != null && networkinfo.isConnected());

    }
    public class SongDownload extends AsyncTask<String, Integer, String>{
        private Context mContext;

        public SongDownload(Context context){
            mContext = context;
        }

        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try{
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                    return "Server Returned HTTP" + connection.getResponseCode() + "" + connection.getResponseMessage();
                }
                int fileLength = connection.getContentLength();
                input = connection.getInputStream();
                Log.d("Run", "Starting");
                Log.d("Run","Context "+mContext);
                output = new FileOutputStream(mContext.getFilesDir() + "/download.mp3");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while((count = input.read(data)) != -1){
                    if (isCancelled()){
                        input.close();
                        return null;
                    }
                    total += count;
                    if (fileLength > 0){
                        publishProgress((int)(total * 100/ fileLength));
                        output.write(data,0,count);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                downloads.setText("Invalid URL! Please Enter the URL again");
            }
            finally {
                try {
                    if(output != null)
                        output.close();
                    if(input != null){
                        input.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            downloads.setText("The file is getting Downloaded " + values[0]);
            Log.d("Run", "OnProgressupdate" + values[0]);
             if(values[0] == 100) {
                 downloads.setText("The File is Downloaded. Play the Song! ");
                 done = 1;
             }
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            Log.d("Run", "File Downloaded");
        }
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("answer", done);
    }


}

