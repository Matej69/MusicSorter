package com.example.matej.musicsorter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class ChooseSongs extends AppCompatActivity {

    ListView songList;
    ImageButton smileButton;
    ImageButton saveButton;
    EditText categoryTextfield;

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
            try {
                int imgID = intent.getExtras().getInt("ImageID");
                smileButton.setImageResource(imgID);
            }catch (Exception e){

            }
        }
    }

    void SetEmoticonButtonListener(){
        smileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Choose_icon.class));
            }
        });
    }


    void SetSaveButtonListener(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongCategory songCategory = GetSongCategoryFromInputData();
                MyXMLParser parser = new MyXMLParser();
                ArrayList<SongCategory> allSongsCategories = parser.GetSongCategories(getApplicationContext());

                allSongsCategories.add(songCategory);
                parser.WriteToSongCategoriesXML(allSongsCategories);
                //1.) save category to XML
                //2.) go to "OnAppOpenActivity"
                startActivity(new Intent(getApplicationContext(),OnAppOpenActivity.class));
            }
        });
    }

    //Get SongCategory object that will be later written in XML file
    SongCategory GetSongCategoryFromInputData(){
        SongCategory songCategory = new SongCategory();

        songCategory.name = categoryTextfield.getText().toString();
        songCategory.imageID = intent.getExtras().getInt("ImageID");;

        for(int i = 0; i < songList.getCount(); ++i){
            //we need to parse our row into view object********************************************************************************************************************************************************************
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


    //seting up list of songs
    public void SongListInit(){
        //get list of all .mp3 and display it in list
        File rootFile = new File("/storage/sdcard0");
        ArrayList<File> SongFiles = GetAllMusicFiles(rootFile);
        // take their names and adapt them to list
        songAdapter = new MySongAdapter(getApplicationContext(),GetNamesFromSongList(SongFiles));
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





class MySongAdapter extends ArrayAdapter<String>{

    Context context;
    ArrayList<String> songNames;
    ArrayList<Integer> imagesID;
    int getViewItemCounter;

    public MySongAdapter(Context _context, ArrayList<String> _songNames){
        super(_context, R.layout.mylayout, _songNames);
        context = _context;
        songNames = _songNames;

        imagesID = new ArrayList<Integer>();
        getViewItemCounter = 0;
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
            imagesID.add(R.drawable.checker_unchecked);
            getViewItemCounter++;
        }

        //needs to be applied here we don't change src to default value that will be read from XML
        //it is also applied in listeners because 'getView' function will not be called if we click on listItem/rightButton
        image.setImageResource(imagesID.get(position));

        //set listeners..................................................
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageLocal = (ImageView)v.findViewById(R.id.emoticonImage_id);
                String tagStr = imageLocal.getTag().toString();
                Toast.makeText(context,tagStr,Toast.LENGTH_SHORT).show();
                int tagInt = Integer.parseInt(tagStr);

                if(imagesID.get(tagInt) == R.drawable.checker_unchecked){
                    imagesID.set(tagInt, R.drawable.checker_checked);
                }else if(imagesID.get(tagInt) == R.drawable.checker_checked) {
                    imagesID.set(tagInt, R.drawable.checker_unchecked);
                }
                imageLocal.setImageResource(imagesID.get(tagInt));

            }
        });
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
