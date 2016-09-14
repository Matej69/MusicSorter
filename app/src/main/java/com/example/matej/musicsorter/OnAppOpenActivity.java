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


        Toast.makeText(getApplicationContext(),Integer.toString(android.os.Build.VERSION.SDK_INT),Toast.LENGTH_LONG).show();

        //access for graphic objects
        list = (ListView)findViewById(R.id.listView);

        InitSongList();
        SetAddNewListeners();
        //Toast.makeText(getApplicationContext(), "Button is clicked", Toast.LENGTH_LONG).show();


    }


    public void InitSongList(){
        //HERE WILL BE fOR LOOP THat READS imageCATEGORY + categoryName
        // + LAST ONE FOR ADDiNG NEW CATEGORIES

        //testing parser
        MyXMLParser xmlParser = new MyXMLParser();

        songCategoryList = xmlParser.GetSongCategories(getApplicationContext());

        //set last item to be custom View(ImageButton+Text)
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.footer_item,null,false);
        list.addFooterView(view);
        //set adapter for list
        CheckerSongsAdapter adapter = new CheckerSongsAdapter(this,songCategoryList,list);
        list.setAdapter(adapter);
    }

    public void SetAddNewListeners(){
        //set listener for BUTTON at end of the list
        ImageButton addButton = (ImageButton)findViewById(R.id.footerImageButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChooseSongs.class));
            }
        });
        //set listener for TEXTVIEW at end of the list
        TextView addTextView = (TextView)findViewById(R.id.textView2);
        addTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChooseSongs.class));
            }
        });
    }


}



class CheckerSongsAdapter extends ArrayAdapter<SongCategory> {
    ListView itemList;
    ArrayList<SongCategory> songCat;
    Context context;

    public CheckerSongsAdapter(Context _context, ArrayList<SongCategory> _songs, ListView _itemList){
        super(_context, R.layout.mylayout, _songs);
        this.context = _context;
        this.songCat = _songs;
        this.itemList = _itemList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.mylayout,parent,false);

        TextView text1 = (TextView)row.findViewById(R.id.categoryName_id);
        ImageView image = (ImageView)row.findViewById(R.id.emoticonImage_id);

        text1.setText(songCat.get(position).name);
        image.setImageResource(songCat.get(position).imageID);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                context.startActivity(new Intent(context, SongListScreen.class).putExtra("songNamesList", songCat.get(position).songs));

            }
        });
        itemList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String listName = ((TextView)view.findViewById(R.id.categoryName_id)).getText().toString();
                int emoticonID = songCat.get(position).imageID;
                ArrayList<String> listOfSongs = songCat.get(position).songs;

                Intent intentToSend = new Intent(context,ChooseSongs.class);
                intentToSend.putExtra("songListName", listName);
                intentToSend.putExtra("StartActivity_listOfSongs",listOfSongs);
                intentToSend.putExtra("ImageID",emoticonID);
                context.startActivity(intentToSend);
                return true;
            }
        });



                //Toast.makeText(context, "clicked", Toast.LENGTH_LONG).show();


        return row;
    }

}






