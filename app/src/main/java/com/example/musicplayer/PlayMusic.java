package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

public class PlayMusic extends Fragment {

    View view;
    String song = "";
    int pos=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragmentplaymusic, container, false);
        ListView listview = (ListView) view.findViewById(R.id.musiclist);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Songs, android.R.layout.simple_list_item_1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    song = "Song 1";
                    pos = 0;
                }
                if (position == 1){
                    song = "Song 2";
                    pos = 1;
                }
                if (position == 2){
                    song = "Song 3";
                    pos = 2;
                }
                if (position == 3){
                    song = "Song 4";
                    pos = 3;
                }
                if (position == 4){
                    song = "Song 5";
                    pos = 4;
                }
                Intent intent = new Intent(view.getContext(), PlaySelectedSong.class);
                intent.putExtra("Song", song);
                intent.putExtra("Position", pos);
                startActivityForResult(intent, 1);
                Log.d("Run", "Selected Song " + pos);
            }
        });
        return view;
    }
}
