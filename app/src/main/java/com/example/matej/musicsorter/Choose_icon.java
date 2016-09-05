package com.example.matej.musicsorter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Choose_icon extends AppCompatActivity {

    ImageView selectedImage;
    Button doneButton;
    GridView iconsGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_icon);
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

        //setup graphic objects
        selectedImage = (ImageView)findViewById(R.id.image_showCurrentEmoticon);
        doneButton = (Button)findViewById(R.id.button_iconPickingDone);
        iconsGrid = (GridView)findViewById(R.id.gridList_emoticonList);
        //setup aditional init stuff
        selectedImage.setTag(R.drawable.emoticon_smile);

        ArrayList<EmoticonGridItem> emoticonList = new  ArrayList<EmoticonGridItem>(){{
            add(new EmoticonGridItem(R.drawable.emoticon_ninja));
            add(new EmoticonGridItem(R.drawable.emoticon_rock));
            add(new EmoticonGridItem(R.drawable.emoticon_code));
            add(new EmoticonGridItem(R.drawable.emoticon_cat));
            add(new EmoticonGridItem(R.drawable.emoticon_dog));
            add(new EmoticonGridItem(R.drawable.emoticon_heart));
            add(new EmoticonGridItem(R.drawable.emoticon_rap));
            add(new EmoticonGridItem(R.drawable.emoticon_sad));
            add(new EmoticonGridItem(R.drawable.emoticon_skull));
            add(new EmoticonGridItem(R.drawable.emoticon_smile));
            add(new EmoticonGridItem(R.drawable.emoticon_star));
        }};


        myGridAdapter adapter = new myGridAdapter(getApplicationContext(),emoticonList,selectedImage);
        iconsGrid.setAdapter(adapter);

        OnDoneClickListener();

    }



    void OnDoneClickListener(){
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get list of items that were checked
                int currentImgID = Integer.parseInt(selectedImage.getTag().toString());
                ArrayList<String> checkededSongsHolder = getIntent().getExtras().getStringArrayList("StartActivity_listOfSongs");
                //send checked items to next Activity
                Intent Activityintent = new Intent(getApplicationContext(),ChooseSongs.class);
                Activityintent.putExtra("StartActivity_listOfSongs",checkededSongsHolder);
                Activityintent.putExtra("ImageID", currentImgID);
                startActivity(Activityintent);
            }
        });
    }

}





//Adapter and OnClicktem Lstener**************************************************************

class EmoticonGridItem{
    public int imageID;
    public EmoticonGridItem(int _imageID){
        imageID = _imageID;
    }
}

class myGridAdapter extends ArrayAdapter<EmoticonGridItem> {

    ArrayList<EmoticonGridItem> emoticonObject;
    Context context;

    ImageView selectedEmoticon;

    public myGridAdapter(Context _context, ArrayList<EmoticonGridItem> _emoticonObject, ImageView _selectedEmoticon){
        super(_context,R.layout.gridviewitem,_emoticonObject);
        context = _context;
        emoticonObject = _emoticonObject;
        selectedEmoticon = _selectedEmoticon;
    }

    @Override
    public View getView(int pos,View gridView,ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.gridviewitem, null);

        ImageButton icon = (ImageButton)itemView.findViewById(R.id.grid_emoticonItem);
        icon.setImageResource(emoticonObject.get(pos).imageID);
        icon.setTag(emoticonObject.get(pos).imageID);

        //OnItemClick send data to global variable that will save image source
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton local_icon =(ImageButton)v.findViewById(R.id.grid_emoticonItem);
                int imgID = Integer.parseInt(local_icon.getTag().toString());
                selectedEmoticon.setImageResource(imgID);
                selectedEmoticon.setTag(imgID);
            }
        });

        return  itemView;
    }






}













