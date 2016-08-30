package com.example.matej.musicsorter;

import android.content.Context;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Matej on 30/08/2016.
 */
public class MyXMLParser {

    MyXMLParser(){}

    ArrayList<SongCategory> GetSongCategories( InputStream fileStream, Context con){

        SongCategory songCatToBeAdded = null;
        ArrayList<SongCategory> songCategories = new ArrayList<SongCategory>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(fileStream, null);

            int readingState = parser.getEventType();
            String tagRead = null;
            String textRead = null;
            while(readingState != XmlPullParser.END_DOCUMENT)
            {
                //Toast.makeText(con,tagRead,Toast.LENGTH_SHORT).show();
                switch(readingState)
                {
                    case XmlPullParser.START_TAG :
                    {
                        tagRead = parser.getName();
                        if(tagRead.equalsIgnoreCase("CategoryItem")){
                            songCatToBeAdded = new SongCategory();
                        }
                    }break;
                    //XML parser can read "  " for string and stuff, with this we are sure that text is actually string
                    case XmlPullParser.TEXT :
                    {
                        textRead = parser.getText();
                        boolean textCanBeParsed = textRead.matches(".*[a-zA-Z0-9]+.*");
                        if(textCanBeParsed && textRead!=null)
                        {
                             if (tagRead.equalsIgnoreCase("Name")) {
                                 songCatToBeAdded.name = textRead.toString();
                            } else if (tagRead.equalsIgnoreCase("ImageID")) {

                                 songCatToBeAdded.imageID = Integer.parseInt(textRead);
                            } else if (tagRead.equalsIgnoreCase("Song")) {
                                 songCatToBeAdded.songs.add(textRead);
                            }
                        }
                    }break;
                    case XmlPullParser.END_TAG :
                    {
                        tagRead = parser.getName();
                        if(tagRead.equalsIgnoreCase("CategoryItem")) {
                            songCategories.add(songCatToBeAdded);
                        }
                    }break;
                    default:break;
                }
                //Toast.makeText(con, tagRead, Toast.LENGTH_SHORT).show();
                readingState = parser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return songCategories;
    }


}
