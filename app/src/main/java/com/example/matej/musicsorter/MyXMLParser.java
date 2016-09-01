package com.example.matej.musicsorter;

import android.content.Context;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Matej on 30/08/2016.
 */
public class MyXMLParser {

    String filePath;
    StringBuilder XMLtext;

    MyXMLParser(){
        filePath = "/storage/sdcard0/myXml.xml";
        XMLtext = new StringBuilder();
    }

    String StartTag(String tag){
        return new String("\n<"+tag+">");
    }
    String EndTag(String tag){
        return new String("</"+tag+">");
    }

    ArrayList<SongCategory> GetSongCategories(Context con){

        FileInputStream fileStream = null;
        SongCategory songCatToBeAdded = null;
        ArrayList<SongCategory> songCategories = new ArrayList<SongCategory>();

        try {
            fileStream = new FileInputStream(filePath);

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
            fileStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        //closing file
        finally {
            try {
                if (fileStream != null)
                    fileStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return songCategories;
    }


   void WriteToSongCategoriesXML( ArrayList<SongCategory> songCategories){

       //take data from variables and create one string that will be written to XMLfile


       XMLtext.append(StartTag("CategoryList"));
       for(SongCategory songCat : songCategories){
           XMLtext.append(StartTag("CategoryItem"));
           XMLtext.append(StartTag("Name")+songCat.name+EndTag("Name"));
           XMLtext.append(StartTag("ImageID") + songCat.imageID + EndTag("ImageID"));
           for(String songName : songCat.songs){
               XMLtext.append(StartTag("Song") + songName + EndTag("Song"));
           }
           XMLtext.append(EndTag("CategoryItem"));
       }
       XMLtext.append(EndTag("CategoryList"));

       //writing to XML file
       FileOutputStream fileStream = null;
       try {
           fileStream = new FileOutputStream(filePath);
           fileStream.write(XMLtext.toString().getBytes());
       }catch (IOException e){
           e.printStackTrace();
       }
       finally {
           try{
               if(fileStream != null) {
                   fileStream.close();
                   fileStream.flush();
               }
           }catch(IOException e){
               e.printStackTrace();
           }
       }

    }





}
