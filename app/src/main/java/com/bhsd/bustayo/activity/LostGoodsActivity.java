package com.bhsd.bustayo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.adapter.LostGoodsAdapter;
import com.bhsd.bustayo.dto.LostGoodsInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class LostGoodsActivity extends AppCompatActivity {

    Spinner categorize;
    EditText searchLostGoods;
    Button searchButton;
    ArrayList<LostGoodsInfo> lostGoodsInfos;
    RecyclerView lgRecyclerView;
    FloatingActionButton goUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_goodsinfo);

        findViewById(R.id.searchGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        categorize = findViewById(R.id.lostGroup);
        searchButton = findViewById(R.id.searchButton);
        searchLostGoods = findViewById(R.id.userLostGoods);
        lgRecyclerView = findViewById(R.id.LostList);
        goUp = findViewById(R.id.goUp);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "검색.", Toast.LENGTH_SHORT).show();
            }
        });


        //하드코딩
        String[] categorizes ={"전체", "귀금속", "의류", "지갑", "전자기기", "컴퓨터", "카드", "휴대폰", "기타"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categorizes);
        categorize.setAdapter(arrayAdapter);

        lostGoodsInfos = new ArrayList<LostGoodsInfo>();

        insertData();       //하드코딩한 부분 - 메소드는 그대로 쓰면서 내용만 바꿀예정
        LostGoodsAdapter lgAdapter = new LostGoodsAdapter(lostGoodsInfos);
        lgRecyclerView.setHasFixedSize(true);
        lgRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        lgRecyclerView.setAdapter(lgAdapter);

        lgAdapter.setOnListItemSelected(new LostGoodsAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position) {
            //Intent intent = new Intent(getApplicationContext(), StationActivity.class);
//                값을 넘겨주는 부분
//                intent.putExtra("stationNm", stationName.getText().toString());
//                intent.putExtra("arsId", stationId);
//                startActivity(intent);
                Toast.makeText(getApplicationContext(), "상세정보.", Toast.LENGTH_SHORT).show();
            }
        });

        goUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lgRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    public void insertData(){
        lostGoodsInfos.add(new LostGoodsInfo(R.drawable.ic_bus,"L2020062100000665","스마트폰","2번 버스","2020-06-20"));
        lostGoodsInfos.add(new LostGoodsInfo(R.drawable.ic_bus,"L2020062100000666","지갑","601번 버스","2020-06-19"));
        lostGoodsInfos.add(new LostGoodsInfo(R.drawable.ic_bus,"L2020062100000667","마스크","6716번 버스","2020-06-19"));
        lostGoodsInfos.add(new LostGoodsInfo(R.drawable.ic_bus,"L2020062100000668","정신머리","7022번 버스","2020-06-19"));
        lostGoodsInfos.add(new LostGoodsInfo(R.drawable.ic_bus,"L2020062100000669","노트북","571번 버스","2020-06-18"));
        lostGoodsInfos.add(new LostGoodsInfo(R.drawable.ic_bus,"L2020062100000670","틴트","273번 버스","2020-06-18"));
        lostGoodsInfos.add(new LostGoodsInfo(R.drawable.ic_bus,"L2020062100000671","스마트폰","2번 버스","2020-06-18"));
        lostGoodsInfos.add(new LostGoodsInfo(R.drawable.ic_bus,"L2020062100000672","지갑","601번 버스","2020-06-18"));
        lostGoodsInfos.add(new LostGoodsInfo(R.drawable.ic_bus,"L2020062100000673","마스크","6716번 버스","2020-06-18"));
        lostGoodsInfos.add(new LostGoodsInfo(R.drawable.ic_bus,"L2020062100000674","정신머리","7022번 버스","2020-06-17"));
        lostGoodsInfos.add(new LostGoodsInfo(R.drawable.ic_bus,"L2020062100000675","노트북","571번 버스","2020-06-17"));
        lostGoodsInfos.add(new LostGoodsInfo(R.drawable.ic_bus,"L2020062100000676","틴트","273번 버스","2020-06-17"));
    }
}
