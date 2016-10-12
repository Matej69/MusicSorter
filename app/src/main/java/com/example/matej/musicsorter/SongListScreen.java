package com.example.matej.musicsorter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class SongListScreen extends AppCompatActivity {

    ArrayList<String> songNamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_screen);


        //list of songs(Strings) that we got from "OnAppOpenActivity" by clicking on song category
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songNamesList = (ArrayList)bundle.getParcelableArrayList("songNamesList");

       // File root = Environment.getExternalStorageDirectory();
                //File root = new File(Environment.getExternalStorageDirectory().getPath().toString()+"/Music");
                //ArrayList<File> allMusicFiles = GetAllMusicFiles(root);
        File rootFileSD0 = new File(Environment.getExternalStorageDirectory().getPath().toString());   // TABLET -> storage/sdcard0/  :: MOB -> storage/emulated/0
        File rootFileSD1 = new File("storage/sdcard1/");

        ArrayList<File> SongFiles = GetAllMusicFiles(rootFileSD0);
        try{
            SongFiles.addAll(GetAllMusicFiles(rootFileSD1));
        }catch (Exception e){
            //Toast.makeText(this, "SD1 CARD IS NOT FOUND",Toast.LENGTH_LONG).show();
        }
        //if SD card is found, read songs from SD as well
        /*boolean isSDFound = android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(isSDFound) {
            Toast.makeText(getApplicationContext(),"00000",Toast.LENGTH_SHORT).show();
            File rootSD = Environment.getExternalStorageDirectory();
            allMusicFiles.addAll(GetAllMusicFiles(rootSD));
        }
        */
        final ArrayList<File> songObjectList = GetRightsSongsFromAllSongs(SongFiles);

        final ListView songList = (ListView)findViewById(R.id.list_songsNameList);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,R.layout.songlistitem,R.id.text_songNameItem_id,GetNamesFromSongList(songObjectList));
        songList.setAdapter(adapter);
        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("position",position).putExtra("songObjectList",songObjectList));
                //go to new screen and play song
            }
        });
    }


//-------------------------------------------------------------------------------------------------------------------------------
//FUNCTION FOR GETTING MUSIC FROM ALL FILES IN DEVICE, REMOVING 2 SAME FILES IN FINAL LIST, GEING NAMES ARRAY FROM FILES......
// -------------------------------------------------------------------------------------------------------------------------------

    boolean IsFileUniqueInList(String thisFileName, ArrayList<File> fileList){
        for (File file : fileList){
            //Toast.makeText(getApplicationContext(),"is "+file.getName() +" = "+thisFileName,Toast.LENGTH_SHORT).show();
            if(thisFileName.equalsIgnoreCase(file.getName().toString())) {
                return false;
            }
        }
        return true;
    }

   public ArrayList<File> GetAllMusicFiles(File root){

       File[] allFiles = root.listFiles();
       ArrayList<File> songfiles = new ArrayList<File>();

       ArrayList<File> neededSongFiles = new ArrayList<File>();

       //search through all files and find all .mp3 songs
       for(File file : allFiles){
           if(file.isDirectory()){
               try {
                   songfiles.addAll(GetAllMusicFiles(file));
               }catch (Exception e){
               }
           }
           else if (file.getName().endsWith(".mp3") || file.getName().endsWith(".mp4") || file.getName().endsWith(".wav") || file.getName().endsWith(".aac")){
              if(IsFileUniqueInList(file.getName(), songfiles))
                   songfiles.add(file);
           }
       }

        return songfiles;
    }

    public ArrayList<File> GetRightsSongsFromAllSongs(ArrayList<File> allMusicFiles){
        ArrayList<File> finalMusicFileList = new ArrayList<File>();

        for(File songfile : allMusicFiles){
            for(String songName : songNamesList)
                if(songfile.getName().equalsIgnoreCase(songName) && IsFileUniqueInList(songName,finalMusicFileList)){
                    finalMusicFileList.add(songfile);
                }
        }
        return finalMusicFileList;
    }

    public ArrayList<String> GetNamesFromSongList(ArrayList<File> songList){
        ArrayList<String> songNames = new ArrayList<String>();
        for(File song : songList){
            songNames.add(song.getName());
        }
        return songNames;
    }


}
