package com.example.matej.musicsorter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class SongListScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




       // File root = Environment.getExternalStorageDirectory();
        File root = new File("/storage/sdcard0");
        final ArrayList<File> songObjectList = GetAllMusicFiles(root);


        final ListView songList = (ListView)findViewById(R.id.list_songsNameList);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,R.layout.songlistitem,R.id.text_songNameItem_id,GetNamesFromSongList(songObjectList));
        songList.setAdapter(adapter);
        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView)findViewById(R.id.textView);
                text.setText(GetNamesFromSongList(songObjectList).get(position));
                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("position",position).putExtra("songObjectList",songObjectList));
                //go to new screen and play song

            }
        });

    }






   public ArrayList<File> GetAllMusicFiles(File root){

        ArrayList<File> files = new ArrayList<File>();
        File[] filesInside = root.listFiles();

        for(File file : filesInside){
            if(file.isDirectory()){
                files.addAll(GetAllMusicFiles(file));
            }
            else if (file.getName().endsWith(".mp3")){
                files.add(file);
            }

        }

        return files;
    }

    public ArrayList<String> GetNamesFromSongList(ArrayList<File> songList){
        ArrayList<String> songNames = new ArrayList<String>();
        for(File song : songList){
            songNames.add(song.getName());
        }
        return songNames;
    }






    //ListView songList = (ListView)findViewById(R.id.list_songsNameList);
    //ArrayAdapter<String> adapter = new ArrayAdapter(this,R.layout.content_song_list_screen,)

}
