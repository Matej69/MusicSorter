package com.example.matej.musicsorter;

import java.util.ArrayList;

/**
 * Created by Matej on 30/08/2016.
 */
public class SongCategory {
    public String name;
    public int imageID;
    public ArrayList<String> songs;

    public void InitArrayList(){
        songs = new ArrayList<String>();
    }

    SongCategory(String _name, int _imageID, ArrayList<String> _songs){
        this.name = _name;
        this.imageID = _imageID;
        if(_songs != null)
            this.songs = _songs;
    }

    SongCategory(){
        InitArrayList();
    }

}
