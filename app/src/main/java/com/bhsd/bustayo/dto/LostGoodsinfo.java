package com.bhsd.bustayo.dto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.bhsd.bustayo.R;

import java.util.ArrayList;

public class LostGoodsinfo{

    int lgImage;
    String lgnNum, lgName, lgPlace, lgDate;

    public LostGoodsinfo(int lgImage, String lgnNum, String lgName, String lgPlace, String lgDate) {
        this.lgImage = lgImage;
        this.lgnNum = lgnNum;
        this.lgName = lgName;
        this.lgPlace = lgPlace;
        this.lgDate = lgDate;
    }

    public int getLgImage() {
        return lgImage;
    }

    public void setLgImage(int lgImage) {
        this.lgImage = lgImage;
    }

    public String getLgNum() {
        return lgnNum;
    }

    public void setLgNml(String lgnNml) {
        this.lgnNum = lgnNml;
    }

    public String getLgName() {
        return lgName;
    }

    public void setLgName(String lgName) {
        this.lgName = lgName;
    }

    public String getLgPlace() {
        return lgPlace;
    }

    public void setLgPlace(String lgPlace) {
        this.lgPlace = lgPlace;
    }

    public String getLgDate() {
        return lgDate;
    }

    public void setLgDate(String lgDate) {
        this.lgDate = lgDate;
    }
}
