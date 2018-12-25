package com.example.duyphuong.playermp3.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.example.duyphuong.playermp3.Adapter.ListSongAdapter;
import com.example.duyphuong.playermp3.R;

import java.util.function.ToLongBiFunction;

public class ActivityListSongs extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private ListView lvListSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_songs);
        loadControls();
        loadEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void loadEvents() {

    }

    private void loadControls() {
        lvListSong = findViewById(R.id.lvListsong);

        toolbar = findViewById(R.id.toolbarListSongs);
            toolbar.setTitle("Danh sach nhac");
            toolbar.setNavigationIcon(R.drawable.back);
            setSupportActionBar(toolbar);

        ListSongAdapter listSongAdapter = new ListSongAdapter(this,MainActivity.arrSong);
        lvListSong.setAdapter(listSongAdapter);
        lvListSong.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("id", position);

        setResult(RESULT_OK, intent);
        finish();
    }
}
