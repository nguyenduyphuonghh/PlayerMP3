package com.example.duyphuong.playermp3.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.duyphuong.playermp3.Adapter.ContentsAdapter;
import com.example.duyphuong.playermp3.Model.Contents;
import com.example.duyphuong.playermp3.R;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    ListView lvIcon;
    ArrayList<Contents> dsContents;
    ContentsAdapter contentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        addControls();
    }
    private void addControls() {
        lvIcon = (ListView) findViewById(R.id.lv_about_us);
        dsContents = new ArrayList<>();

        dsContents.add(new Contents("Player Mp3","Made by NDP",R.drawable.icon));
        dsContents.add(new Contents("Lập trình di động nâng cao","Mục tiêu: Miễn thi :))",android.R.drawable.star_on));
        dsContents.add(new Contents("Build Version","Version 1.0",android.R.drawable.ic_dialog_info));
        dsContents.add(new Contents("Email","nguyenduyphuong.hou@gmail.com",android.R.drawable.ic_dialog_email));
        dsContents.add(new Contents("Share","Share to your friends",android.R.drawable.ic_menu_share));
        dsContents.add(new Contents("Rate","Give your rate and feedback",android.R.drawable.ic_menu_preferences));
        dsContents.add(new Contents("More","More apps from developer",android.R.drawable.ic_menu_more));
        dsContents.add(new Contents("Copyright © 2018 Hou","All rights reserved",android.R.drawable.ic_secure));

        contentsAdapter = new ContentsAdapter(AboutActivity.this,R.layout.items,dsContents);
        lvIcon.setAdapter(contentsAdapter);

        lvIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(AboutActivity.this,"Cảm ơn!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
