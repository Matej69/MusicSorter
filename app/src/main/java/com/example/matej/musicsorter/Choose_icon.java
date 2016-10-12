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

        //setup graphic objects
        selectedImage = (ImageView)findViewById(R.id.image_showCurrentEmoticon);
        doneButton = (Button)findViewById(R.id.button_iconPickingDone);
        iconsGrid = (GridView)findViewById(R.id.gridList_emoticonList);
        //setup aditional init stuff
        selectedImage.setTag(R.drawable.emoticon_happy);

        //getting all emoticons, this should done in another file and better formated but sometimes, I live a gangsta life
        ArrayList<EmoticonGridItem> emoticonList = new  ArrayList<EmoticonGridItem>(){{
            add(new EmoticonGridItem(R.drawable.emoticon_agenda));      add(new EmoticonGridItem(R.drawable.emoticon_bread_pretzel));   add(new EmoticonGridItem(R.drawable.emoticon_compass));
            add(new EmoticonGridItem(R.drawable.emoticon_alarm));       add(new EmoticonGridItem(R.drawable.emoticon_breakfast));       add(new EmoticonGridItem(R.drawable.emoticon_dratini));
            add(new EmoticonGridItem(R.drawable.emoticon_alarm1));      add(new EmoticonGridItem(R.drawable.emoticon_briefcase));       add(new EmoticonGridItem(R.drawable.emoticon_dollarbill));
            add(new EmoticonGridItem(R.drawable.emoticon_announcement));add(new EmoticonGridItem(R.drawable.emoticon_bullbasaur));      add(new EmoticonGridItem(R.drawable.emoticon_eevee));
            add(new EmoticonGridItem(R.drawable.emoticon_archive));     add(new EmoticonGridItem(R.drawable.emoticon_burn));            add(new EmoticonGridItem(R.drawable.emoticon_exclamation));
            add(new EmoticonGridItem(R.drawable.emoticon_attachment));  add(new EmoticonGridItem(R.drawable.emoticon_businessman));     add(new EmoticonGridItem(R.drawable.emoticon_eye));
            add(new EmoticonGridItem(R.drawable.emoticon_auction));     add(new EmoticonGridItem(R.drawable.emoticon_businesswoman));   add(new EmoticonGridItem(R.drawable.emoticon_flask));
            add(new EmoticonGridItem(R.drawable.emoticon_bank));        add(new EmoticonGridItem(R.drawable.emoticon_cake_chocolate));  add(new EmoticonGridItem(R.drawable.emoticon_gamepad));
            add(new EmoticonGridItem(R.drawable.emoticon_battery));     add(new EmoticonGridItem(R.drawable.emoticon_calculator));      add(new EmoticonGridItem(R.drawable.emoticon_gift));
            add(new EmoticonGridItem(R.drawable.emoticon_battery8));    add(new EmoticonGridItem(R.drawable.emoticon_cherry));          add(new EmoticonGridItem(R.drawable.emoticon_glad));
            add(new EmoticonGridItem(R.drawable.emoticon_bellsprout));  add(new EmoticonGridItem(R.drawable.emoticon_coffee));          add(new EmoticonGridItem(R.drawable.emoticon_glass));
            add(new EmoticonGridItem(R.drawable.emoticon_binoculars));  add(new EmoticonGridItem(R.drawable.emoticon_coffee1));         add(new EmoticonGridItem(R.drawable.emoticon_glasses));
            add(new EmoticonGridItem(R.drawable.emoticon_blushing));    add(new EmoticonGridItem(R.drawable.emoticon_coins));           add(new EmoticonGridItem(R.drawable.emoticon_goal));
            add(new EmoticonGridItem(R.drawable.emoticon_bookmark1));   add(new EmoticonGridItem(R.drawable.emoticon_compactdisc));     add(new EmoticonGridItem(R.drawable.emoticon_growth));
            add(new EmoticonGridItem(R.drawable.emoticon_happy));       add(new EmoticonGridItem(R.drawable.emoticon_locked2));         add(new EmoticonGridItem(R.drawable.emoticon_mountain));
            add(new EmoticonGridItem(R.drawable.emoticon_heart));       add(new EmoticonGridItem(R.drawable.emoticon_magnifyingglass)); add(new EmoticonGridItem(R.drawable.emoticon_pikachu));
            add(new EmoticonGridItem(R.drawable.emoticon_home));        add(new EmoticonGridItem(R.drawable.emoticon_mankey));          add(new EmoticonGridItem(R.drawable.emoticon_psyduck));
            add(new EmoticonGridItem(R.drawable.emoticon_horse));       add(new EmoticonGridItem(R.drawable.emoticon_meowth));          add(new EmoticonGridItem(R.drawable.emoticon_rocket));
            add(new EmoticonGridItem(R.drawable.emoticon_hourglass));   add(new EmoticonGridItem(R.drawable.emoticon_microphone));      add(new EmoticonGridItem(R.drawable.emoticon_roller));
            add(new EmoticonGridItem(R.drawable.emoticon_hourglass2));  add(new EmoticonGridItem(R.drawable.emoticon_microscope));      add(new EmoticonGridItem(R.drawable.emoticon_save));
            add(new EmoticonGridItem(R.drawable.emoticon_lifebuoy));    add(new EmoticonGridItem(R.drawable.emoticon_moneybag));        add(new EmoticonGridItem(R.drawable.emoticon_snorlax));
            add(new EmoticonGridItem(R.drawable.emoticon_lightbulb));   add(new EmoticonGridItem(R.drawable.emoticon_mortarboard));     add(new EmoticonGridItem(R.drawable.emoticon_store));
            add(new EmoticonGridItem(R.drawable.emoticon_suitcase));    add(new EmoticonGridItem(R.drawable.emoticon_trophy));          add(new EmoticonGridItem(R.drawable.emoticon_weird));
            add(new EmoticonGridItem(R.drawable.emoticon_target));      add(new EmoticonGridItem(R.drawable.emoticon_ufo));             add(new EmoticonGridItem(R.drawable.emoticon_worldwide));
            add(new EmoticonGridItem(R.drawable.emoticon_telescope));   add(new EmoticonGridItem(R.drawable.emoticon_unbelievable));    add(new EmoticonGridItem(R.drawable.emoticon_zubat));
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
                String songListName = getIntent().getExtras().getString("songListName").toString();
                //send checked items to next Activity
                Intent Activityintent = new Intent(getApplicationContext(),ChooseSongs.class);
                Activityintent.putExtra("StartActivity_listOfSongs",checkededSongsHolder);
                Activityintent.putExtra("ImageID", currentImgID);
                Activityintent.putExtra("songListName", songListName);
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













