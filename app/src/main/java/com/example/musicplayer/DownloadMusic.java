package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.net.URL;

public class DownloadMusic extends Fragment {

    View view;
    Intent intent;
    private Button downloadmusic;
    private EditText url;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        view = inflater.inflate(R.layout.downloadfragment, container, false);
        downloadmusic = (Button) view.findViewById(R.id.startdownload);
        url = (EditText) view.findViewById(R.id.url);
        downloadmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url_data = url.getText().toString();
                if (url_data.matches("")){
                    Toast.makeText(getActivity(), "Please Enter the URL", Toast.LENGTH_SHORT).show();
                }
                else {
                    intent = new Intent(getActivity(), Download.class);
                    intent.putExtra("Data", url_data);
                    startActivity(intent);
                }
            }
        });
        return view;
    }
}
