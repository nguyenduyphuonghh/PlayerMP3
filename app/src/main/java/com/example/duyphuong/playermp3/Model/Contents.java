package com.example.duyphuong.playermp3.Model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by nguye on 11/1/2017.
 */

public class Contents implements Serializable {
    private String txtContent;
    private String txtDetail;
    private int imgIcon;

    public Contents() {
    }

    public Contents(String txtContent, String txtDetail, int imgIcon) {
        this.txtContent = txtContent;
        this.txtDetail = txtDetail;
        this.imgIcon = imgIcon;
    }

    public String getTxtContent() {
        return txtContent;
    }

    public void setTxtContent(String txtContent) {
        this.txtContent = txtContent;
    }

    public String getTxtDetail() {
        return txtDetail;
    }

    public void setTxtDetail(String txtDetail) {
        this.txtDetail = txtDetail;
    }

    public int getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(int imgIcon) {
        this.imgIcon = imgIcon;
    }
}
