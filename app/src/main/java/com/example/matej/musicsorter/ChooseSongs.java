package com.example.matej.musicsorter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;

public class ChooseSongs extends AppCompatActivity {

    ListView songList;
    ImageButton smileButton;
    ImageButton saveButton;
    EditText categoryTextfield;

    ArrayList<Integer> checkerImageIDs;
    String categoryName;

    MySongAdapter songAdapter;

    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_songs);
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

        //graphic object setup
        smileButton = (ImageButton)findViewById(R.id.smileButton);
        saveButton = (ImageButton)findViewById(R.id.saveButton);
        songList = (ListView)findViewById(R.id.songList_choosing);
        categoryTextfield = (EditText)findViewById(R.id.textfield_categoryName);

        intent = getIntent();
        GetAndSetIntent();

        SetEmoticonButtonListener();
        SongListInit();
        SetSaveButtonListener();


        //songCategory = GetSongCategoryFromInputData();

        //Toast.makeText(getApplicationContext(),  songCategory.songs.get(1).toString(),Toast.LENGTH_LONG).show();


    }


    void GetAndSetIntent(){
        if(intent != null) {
            //intent coming from AddingNewSong
            try {
                int imgID = intent.getExtras().getInt("ImageID");
                smileButton.setImageResource(imgID);
            }catch (Exception e){
            }
            //intent coming when existing category is_choosen || from "choose_icon"
            try{
                categoryName = intent.getExtras().getString("songListName");
                categoryTextfield.setText(categoryName);
            }catch (Exception e){
            }
        }
    }

    ArrayList<String> GetCheckedSongsNames(){
        ArrayList<String> checkedSongNames = new ArrayList<String>();


        for(int i = 0; i < songAdapter.songNames.size(); ++i){
            //View adapView = myAdapter.getView(i,null,null);
            if(songAdapter.imagesID.get(i) == R.drawable.checker_checked)
                checkedSongNames.add(songAdapter.songNames.get(i));
            }

        return checkedSongNames;
    }

    void SetEmoticonButtonListener(){
        smileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> checkedNames = GetCheckedSongsNames();
                String textViewText = categoryTextfield.getText().toString();
                startActivity(new Intent(getApplicationContext(), Choose_icon.class).putExtra("StartActivity_listOfSongs",checkedNames).putExtra("songListName",textViewText));
            }
        });
    }


    void SetSaveButtonListener(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SongCategory songCategory = GetSongCategoryFromInputData();
                    MyXMLParser parser = new MyXMLParser();
                    ArrayList<SongCategory> allSongsCategories = parser.GetSongCategories(getApplicationContext());

                    //first remove all other songCategories with same name(there will be only one or none)
                    Iterator<SongCategory> it = allSongsCategories.iterator();
                    while (it.hasNext()) {
                        SongCategory currentSong = it.next();
                        if (currentSong.name.equalsIgnoreCase(songCategory.name)) {
                            it.remove();
                        }
                    }

                    //check for stupid values

                    if (songCategory.name.equalsIgnoreCase(""))
                        songCategory.name = "where's my name -.-";
                    //songCategory.imageID = R.drawable.emoticon_smile;

                    allSongsCategories.add(songCategory);
                    parser.WriteToSongCategoriesXML(allSongsCategories);
                    startActivity(new Intent(getApplicationContext(), OnAppOpenActivity.class));

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    //Get SongCategory object that will be later written in XML file
    SongCategory GetSongCategoryFromInputData(){
        SongCategory songCategory = new SongCategory();

        songCategory.name = categoryTextfield.getText().toString();
        //if we cant get it over intent, set default value
        try{
            songCategory.imageID = intent.getExtras().getInt("ImageID");
        }catch (Exception e){
            songCategory.imageID = R.drawable.emoticon_happy;
        }

        for(int i = 0; i < songList.getCount(); ++i){
            View view = songList.getAdapter().getView(i, null , songList);
            ImageButton checkerImage = (ImageButton)view.findViewById(R.id.emoticonImage_id);
            if(songAdapter.imagesID.get(i) == R.drawable.checker_checked) {
                //Toast.makeText(getApplicationContext(),checkerImage.getTag().toString(),Toast.LENGTH_LONG).show();
                TextView songName = (TextView) view.findViewById(R.id.categoryName_id);
                songCategory.songs.add(songName.getText().toString());
            }

        }
        return songCategory;
    }

    void RemoveListFromXML(){
        MyXMLParser parser = new MyXMLParser();
        ArrayList<SongCategory> categories = parser.GetSongCategories(this);
        //remove it
        Iterator<SongCategory> it = categories.iterator();
        while(it.hasNext()){
            SongCategory currentSong = it.next();
            if(currentSong.name.equalsIgnoreCase(categoryName)) {
                it.remove();
            }
        }
        //save change
        parser.WriteToSongCategoriesXML(categories);
    }

    //seting up list of songs
    public void SongListInit(){
        //get list of all .mp3 and display it in list
        File rootFile = new File(Environment.getExternalStorageDirectory().getPath().toString()+"/Music");
        //File rootFile = Environment.getExternalStorageDirectory();
        ArrayList<File> SongFiles = GetAllMusicFiles(rootFile);

        //set list footer
        //ste on click listener for footer button
        Button removeButton = (Button)findViewById(R.id.button_removeList);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveListFromXML();
                startActivity(new Intent(getApplicationContext(),OnAppOpenActivity.class));
            }
        });
        // take their names and adapt them to list
        //songList.addFooterView(footerView);
        songAdapter = new MySongAdapter(this,GetNamesFromSongList(SongFiles));
        songList.setAdapter(songAdapter);
    }


    //reading ALL song files from drive..................................................
    public ArrayList<File> GetAllMusicFiles(File root){

        File[] allFiles = root.listFiles();
        ArrayList<File> songfiles = new ArrayList<File>();

        //search through all files and find all .mp3 songs
        for(File file : allFiles){
            if(file.isDirectory()){
                songfiles.addAll(GetAllMusicFiles(file));
            }
            else if (file.getName().endsWith(".mp3")){
                songfiles.add(file);
            }
        }
        return songfiles;
    }

    public ArrayList<String> GetNamesFromSongList(ArrayList<File> songList){
        ArrayList<String> songNames = new ArrayList<String>();
        for(File song : songList){
            songNames.add(song.getName());
        }
        return songNames;
    }
}


// song list adapter!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

class MySongAdapter extends ArrayAdapter<String>{

    Context context;
    ArrayList<String> songNames;
    ArrayList<Integer> imagesID;
    int getViewItemCounter;

    public MySongAdapter(Context _context, ArrayList<String> _songNames){
        super(_context, R.layout.mylayout, _songNames);
        this.context = _context;
        songNames = _songNames;

        //set images to array that came from "OnAppOpenActivity"
        imagesID = new ArrayList<Integer>();
        getViewItemCounter = 0;
    }

    public void FillImageIDArrayWithIDs(int i){
        //adding all UNCHECKED checkers, later if names are equal they become CHECKED
        imagesID.add(R.drawable.checker_unchecked);

        //if there is intent with list of names that we can get, see which checkedSongNames are in list of song names
        try{
            Intent local_intent = ((Activity)context).getIntent();
            ArrayList<String> checkedSongNames = local_intent.getExtras().getStringArrayList("StartActivity_listOfSongs");
                for(String checkedSongName : checkedSongNames){
                    if(songNames.get(i).equalsIgnoreCase(checkedSongName)){
                        imagesID.set(i,R.drawable.checker_checked);
                    }
                }
            //Toast.makeText(context, "TRYING",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            //Toast.makeText(context, e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.mylayout,parent,false);

        TextView text = (TextView)rowView.findViewById(R.id.categoryName_id);
        ImageView image = (ImageView)rowView.findViewById(R.id.emoticonImage_id);

        text.setText(songNames.get(position).toString());

        //must be repeating and set
        image.setTag(position);
        //initial valuse that are set only once
        if(getViewItemCounter < songNames.size()){
            image.setImageResource(R.drawable.checker_unchecked);
            FillImageIDArrayWithIDs(getViewItemCounter);
            getViewItemCounter++;
        }

        //needs to be applied here we don't change src to default value that will be read from XML
        //it is also applied in listeners because 'getView' function will not be called if we click on listItem/rightButton
        image.setImageResource(imagesID.get(position));

        //set listeners..................................................
        /*image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageLocal = (ImageView)v.findViewById(R.id.emoticonImage_id);
                String tagStr = imageLocal.getTag().toString();
                int tagInt = Integer.parseInt(tagStr);

                if(imagesID.get(tagInt) == R.drawable.checker_unchecked){
                    imagesID.set(tagInt, R.drawable.checker_checked);
                }else if(imagesID.get(tagInt) == R.drawable.checker_checked) {
                    imagesID.set(tagInt, R.drawable.checker_unchecked);
                }
                imageLocal.setImageResource(imagesID.get(tagInt));

            }
        });
        */
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageLocal = (ImageView)v.findViewById(R.id.emoticonImage_id);
                String tagStr = imageLocal.getTag().toString();
                int tagInt = Integer.parseInt(tagStr);

                if(imagesID.get(tagInt) == R.drawable.checker_unchecked){
                    imagesID.set(tagInt, R.drawable.checker_checked);
                }else if(imagesID.get(tagInt) == R.drawable.checker_checked) {
                    imagesID.set(tagInt, R.drawable.checker_unchecked);
                }
                imageLocal.setImageResource(imagesID.get(tagInt));
            }
        });

        return rowView;
    }

}
