package com.example.duyphuong.playermp3.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duyphuong.playermp3.Model.Contents;
import com.example.duyphuong.playermp3.R;


import java.util.List;

/**
 * Created by nguye on 11/1/2017.
 */

public class ContentsAdapter extends ArrayAdapter<Contents> {

    Activity context; // màn hình sử dụng giao diện này
    int resource; // layout cho từng dòng hiển thị (items.xml)
    List<Contents> objects; // danh sách nguồn dữ liệu
    public ContentsAdapter(Activity context, int resource, List<Contents> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater  = this.context.getLayoutInflater();
        final View row = layoutInflater.inflate(R.layout.items,parent,false);
        TextView txtContents = (TextView)row.findViewById(R.id.txtContent);
        TextView txtDetails = (TextView) row.findViewById(R.id.txtDetail);
        ImageView imgIcon = (ImageView) row.findViewById(R.id.imgIcon);

        Contents contents = this.objects.get(position); // trả lại contents muốn vẽ
        txtContents.setText(contents.getTxtContent());
        txtDetails.setText(contents.getTxtDetail());
        imgIcon.setImageResource(contents.getImgIcon());
        return row;


    }
}
