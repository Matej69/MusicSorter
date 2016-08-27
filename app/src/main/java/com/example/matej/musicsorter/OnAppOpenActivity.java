package com.example.matej.musicsorter;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class OnAppOpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_app_open);
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

        //Writen by me......

        InitSongList();

    }

    
    public void InitSongList(){
        ArrayList<SongItem> songs = new ArrayList<SongItem>();

        //HERE WILL BE fOR LOOP THat READS imageCATEGORY + categoryName
        // + LAST ONE FOR ADDiNG NEW CATEGORIES
        songs.add(new SongItem("song 1",R.drawable.checker_checked));
        songs.add(new SongItem("song 3",R.drawable.checker_unchecked));
        songs.add(new SongItem("song 3",R.drawable.checker_checked));

        ListView list = (ListView)findViewById(R.id.listView);
        CheckerSongsAdapter adapter = new CheckerSongsAdapter(this,songs);
        list.setAdapter(adapter);
    }

}



//******** CLASSES ********
class SongItem{
    String name;
    int imageID;
    SongItem(String _name, int _imageID){
        this.name = _name;
        this.imageID = _imageID;
    }
}

class CheckerSongsAdapter extends ArrayAdapter<SongItem> {
    ArrayList<SongItem> songs;
    Context context;

    public CheckerSongsAdapter(Context _context, ArrayList<SongItem> _songs){
        super(_context,R.layout.mylayout,_songs);
        this.context = _context;
        this.songs = _songs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.mylayout,parent,false);

        TextView text1 = (TextView)row.findViewById(R.id.textView_id);
        ImageView image = (ImageView)row.findViewById(R.id.checkerImage_id);
        text1.setText(songs.get(position).name);
        image.setImageResource(songs.get(position).imageID);



        return row;
    }



}




