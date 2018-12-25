package com.example.duyphuong.playermp3.Adapter;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.duyphuong.playermp3.Activity.MainActivity;
import com.example.duyphuong.playermp3.Model.SongModel;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOError;
import java.util.ArrayList;

public class SongsManager{
    // duong dan SdCard
    public static String MEDIA_PATH = new String("/sdcard/Download/");
    private ArrayList<SongModel> arrSong = new ArrayList<SongModel>();
    // getPlayList lay ra tat ca bai hat luu tren sdcard
    public ArrayList<SongModel> getPlayList() {
        try {
            File MyMusic = new File(String.valueOf(MEDIA_PATH));
            if (MyMusic.listFiles(new fileExtensionFilter()).length > 0) {
                for (File file : MyMusic.listFiles(new fileExtensionFilter())) {
                    SongModel song = new SongModel();
                    song.title = file.getName().substring(0,(file.getName().length() - 4));
                    song.path = file.getPath();

                    // them song vao songlist
                    arrSong.add(song);
                }
            } return arrSong;
        } catch (Exception e) {
            Log.i("404: File not found","" + e.toString());
        } return null;
    }

    // fileExtensionFilter loc ra cac file ket thuc la *.mp3 hoac *.MP3


    class fileExtensionFilter implements FilenameFilter
    {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".mp3") || name.endsWith(".MP3");
        }
    }
}
