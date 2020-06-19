package com.bhsd.bustayo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class LostGoodsinfo extends AppCompatActivity {

    ArrayList<String> lostcategorize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_goodsinfo);

        lostcategorize = new ArrayList<String>();


    }

    public void insertEmail(){
//        String[] categrize ={"naver.com", "daum.net", "empal.com", "nate.com", "dreamwiz.com", "hanmail.net"};
//
//
//        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categrize);
//        lostcategorize.setAdapter
    }
}
