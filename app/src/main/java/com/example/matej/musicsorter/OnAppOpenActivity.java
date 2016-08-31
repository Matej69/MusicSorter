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


        SetOnClickListListener();
        InitSongList();
        //Toast.makeText(getApplicationContext(), "Button is clicked", Toast.LENGTH_LONG).show();


    }


    public void InitSongList(){
        ArrayList<SongCategory> songs = new ArrayList<SongCategory>();

        //HERE WILL BE fOR LOOP THat READS imageCATEGORY + categoryName
        // + LAST ONE FOR ADDiNG NEW CATEGORIES
        songs.add(new SongCategory("song 13333", R.drawable.emoticon_ninja,null));
        songs.add(new SongCategory("song 3", R.drawable.emoticon_rock,null));
        songs.add(new SongCategory("song 3",R.drawable.emoticon_code,null));

        //testing parser
        MyXMLParser xmlParser = new MyXMLParser();
        ArrayList<SongCategory> testSongs = null;

        //We are gonna read and save data to file that is not part of program -> exter/itern file ::: must not be in raw folder **************************************
        /*InputStream inputStream = getResources().openRawResource(getResources().getIdentifier("songscategory","raw", getPackageName()));
        testSongs = xmlParser.GetSongCategories(inputStream,getApplicationContext());
        Toast.makeText(getApplicationContext(), testSongs.get(0).songs.get(0), Toast.LENGTH_SHORT).show();
        */


        //String filePath = "/storage/sdcard0/testingMe.txt";

       /* String data = "<CategoryList>\n" +
                "        <CategoryItem>\n" +
                "            <Name>PrvoIme1</Name>\n" +
                "            <ImageID>1</ImageID>\n" +
                "            <Song>Pjesma mojeg konja one</Song>\n" +
                "            <Song>Yugioh theme</Song>\n" +
                "        </CategoryItem>\n" +
                "        <CategoryItem>\n" +
                "            <Name>Prvo Ime2</Name>\n" +
                "            <ImageID>2</ImageID>\n" +
                "            <Song>Pjesma mojeg konja2</Song>\n" +
                "        </CategoryItem>\n" +
                "</CategoryList>";
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream(filePath);
            fo.write(data.getBytes());
            fo.flush();
            fo.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        FileInputStream  is = null;
        String txt = "";
        String line = null;
        try {
            is = new FileInputStream(filePath);
        }catch (IOException e){
            e.printStackTrace();
        }
        */

        testSongs = xmlParser.GetSongCategories(getApplicationContext());
        xmlParser.WriteToSongCategoriesXML(testSongs);
        testSongs = xmlParser.GetSongCategories(getApplicationContext());

        Toast.makeText(getApplicationContext(),testSongs.get(0).songs.get(1),Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG).show();



        CheckerSongsAdapter adapter = new CheckerSongsAdapter(this,songs);
        list.setAdapter(adapter);
    }

    public void SetOnClickListListener(){
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView emoticon = (ImageView)view.findViewById(R.id.emoticonImage_id);
                TextView name = (TextView)view.findViewById(R.id.categoryName_id);

                // 1.)get list of all songs under category name of TextView(name).getText() from XML file;
                // 2.)get list of all songs that exist on device as strings
                // send 1.) and 2.) to "Choose_songs" and display it there
                Toast.makeText(getApplicationContext(),name.getText(),Toast.LENGTH_LONG).show();
            }
        });
    }




}



//******** CLASSES ********
/*class SongCategory{
    String name;
    int imageID;
    ArrayList<String> songs;
    SongCategory(String _name, int _imageID, ArrayList<String> _songs){
        this.name = _name;
        this.imageID = _imageID;
    }
}
*/

class CheckerSongsAdapter extends ArrayAdapter<SongCategory> {
    ArrayList<SongCategory> songs;
    Context context;

    public CheckerSongsAdapter(Context _context, ArrayList<SongCategory> _songs){
        super(_context, R.layout.mylayout, _songs);
        this.context = _context;
        this.songs = _songs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.mylayout,parent,false);

        TextView text1 = (TextView)row.findViewById(R.id.categoryName_id);
        ImageView image = (ImageView)row.findViewById(R.id.emoticonImage_id);
        text1.setText(songs.get(position).name);
        image.setImageResource(songs.get(position).imageID);

        return row;
    }

}






