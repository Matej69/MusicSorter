package com.example.matej.musicsorter;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity{

    ArrayList<File> songObjectList;
    int position;
    MediaPlayer musicPlayer;
    Thread thread_updateSongBar;

    //references to graphic objects
    TextView songName;
    Button button_prev;
    Button button_pause;
    Button button_next;
    SeekBar songProgressBar;

    enum changeDir{PREVIOUS, NEXT};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //references to graphic objects
        songName = (TextView)findViewById(R.id.text_songName);
        button_prev = (Button)findViewById(R.id.button_previous);
        button_pause = (Button)findViewById(R.id.button_pause);
        button_next = (Button)findViewById(R.id.button_next);
        songProgressBar = (SeekBar)findViewById(R.id.seekBar);

        //receiving data
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songObjectList = (ArrayList)bundle.getParcelableArrayList("songObjectList");
        position = bundle.getInt("position");

        //MedialPlayer init
        Uri uri = Uri.parse(songObjectList.get(position).toString());
        musicPlayer = MediaPlayer.create(getApplicationContext(),uri);
        musicPlayer.start();

        songName.setText(songObjectList.get(position).getName());




        //thread for updating progress bar
        thread_updateSongBar = new Thread(){
            int a;
            @Override
            public void run(){
                int currentPos = 0;
                while(songProgressBar.getProgress() <= songProgressBar.getMax()) {
                    try{
                        songProgressBar.setMax(musicPlayer.getDuration());

                        currentPos = musicPlayer.getCurrentPosition();
                        songProgressBar.setProgress(currentPos);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };

        thread_updateSongBar.start();





        //Graphic object on click and other events setup
        button_pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(musicPlayer.isPlaying()){
                    musicPlayer.pause();
                    button_pause.setText("|>");
                }
                else{
                    musicPlayer.start();
                    button_pause.setText("||");
                }
            }
        });
        button_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPlayer.stop();
                ChangeSongTo(changeDir.PREVIOUS);
                musicPlayer.start();
            }
        });
        button_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                musicPlayer.stop();
                ChangeSongTo(changeDir.NEXT);
                musicPlayer.start();
            }
        });

        songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean fingerDown;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               // if(fingerDown)
               //     musicPlayer.seekTo(seekBar.getProgress());
                if(seekBar.getProgress() >= seekBar.getMax()){
                    musicPlayer.stop();
                    ChangeSongTo(changeDir.NEXT);
                    musicPlayer.start();
                }

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
               // fingerDown = true;
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               // fingerDown = false;
                musicPlayer.seekTo(seekBar.getProgress());
            }
        });


    }

    //called when song needs to be changed
    public void ChangeSongTo(changeDir _dir){
        button_pause.setText("||");
        switch (_dir){
            case PREVIOUS   : position = (position - 1 < 0) ? songObjectList.size() - 1 : position - 1; break;
            case NEXT       : position = (position + 1 > songObjectList.size() - 1) ? 0 : position + 1; break;
        }
        Uri _uri = Uri.parse(songObjectList.get(position).toString());
        musicPlayer = MediaPlayer.create(getApplicationContext(),_uri);
        songName.setText(songObjectList.get(position).getName());

    };




}
