package com.example.matej.musicsorter;


import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.Inflater;
import android.widget.Toast;

import org.w3c.dom.Text;

public class OnAppOpenActivity extends AppCompatActivity {

    ListView list;
    ArrayList<SongCategory> songCategoryList;

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


        //access for graphic objects
        list = (ListView)findViewById(R.id.listView);

        InitSongList();
        SetOnClickListListener();
        //Toast.makeText(getApplicationContext(), "Button is clicked", Toast.LENGTH_LONG).show();


    }


    public void InitSongList(){
        //HERE WILL BE fOR LOOP THat READS imageCATEGORY + categoryName
        // + LAST ONE FOR ADDiNG NEW CATEGORIES

        //testing parser
        MyXMLParser xmlParser = new MyXMLParser();

            //songs.add("journey.mp3"); songs.add("leavin.mp3");
            //songList.add(new SongCategory("Ljubav u prelomu", R.drawable.emoticon_heart, songs));
            //songList.add(new SongCategory("ninja i ja",R.drawable.emoticon_ninja,songs));

        songCategoryList = xmlParser.GetSongCategories(getApplicationContext());

        //set adapter for list
        CheckerSongsAdapter adapter = new CheckerSongsAdapter(this,songCategoryList);
        list.setAdapter(adapter);
        //set last item to be custom View(ImageButton+Text)
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.footer_item,null,false);
        list.addFooterView(view);
        //set last item Button.OnClickEvent
        ImageButton addButton = (ImageButton)view.findViewById(R.id.footerImageButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChooseSongs.class));
            }
        });

    }

    public void SetOnClickListListener(){
        list.setClickable(true);
        list.setFocusable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_LONG).show();

                ImageView emoticon = (ImageView)view.findViewById(R.id.emoticonImage_id);
                TextView name = (TextView)view.findViewById(R.id.categoryName_id);

                //for chosing existing category
                startActivity(new Intent(getApplicationContext(),SongListScreen.class).putExtra("songNamesList",songCategoryList.get(position).songs));

                // 1.)get list of all songs under category name of TextView(name).getText() from XML file;
                // 2.)get list of all songs that exist on device as strings
                // send 1.) and 2.) to "Choose_songs" and display it there
            }
        });
    }


}



class CheckerSongsAdapter extends ArrayAdapter<SongCategory> {
    ArrayList<SongCategory> songCat;
    Context context;

    public CheckerSongsAdapter(Context _context, ArrayList<SongCategory> _songs){
        super(_context, R.layout.mylayout, _songs);
        this.context = _context;
        this.songCat = _songs;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.mylayout,parent,false);

        TextView text1 = (TextView)row.findViewById(R.id.categoryName_id);
        ImageView image = (ImageView)row.findViewById(R.id.emoticonImage_id);

        text1.setText(songCat.get(position).name);
        image.setImageResource(songCat.get(position).imageID);

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SongListScreen.class).putExtra("songNamesList", songCat.get(position).songs));
            }
        });
                //Toast.makeText(context, "clicked", Toast.LENGTH_LONG).show();


        return row;
    }

}






