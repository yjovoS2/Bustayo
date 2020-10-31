package com.bhsd.bustayo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.adapter.LostGoodsAdapter;
import com.bhsd.bustayo.application.APIManager;
import com.bhsd.bustayo.dto.LostGoodsDetailInfo;
import com.bhsd.bustayo.dto.LostGoodsInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class LostGoodsActivity extends AppCompatActivity {

    Spinner categorize;
    EditText searchLostGoods;
    Button searchButton;
    ArrayList<HashMap<String, String>> array;
    HashMap<String, String> detail_d;
    ArrayList<LostGoodsInfo> lostGoodsInfos;
    ArrayList<LostGoodsDetailInfo> lostGoodsDetailInfos;
    RecyclerView lgRecyclerView;
    LostGoodsAdapter lgAdapter;
    FloatingActionButton goUp;
    int page = 0;

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
        lgRecyclerView = findViewById(R.id.lostList);
        goUp = findViewById(R.id.goUp);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "검색.", Toast.LENGTH_SHORT).show();
            }
        });

        String[] categorizes ={"전체", "귀금속", "의류", "지갑", "전자기기", "컴퓨터", "카드", "휴대폰", "기타"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categorizes);
        categorize.setAdapter(arrayAdapter);

        lostGoodsInfos = new ArrayList<LostGoodsInfo>();
        lostGoodsDetailInfos = new ArrayList<LostGoodsDetailInfo>();

        new Thread() {
            @Override
            public void run() {
                array = APIManager.getAPIArray(APIManager.GET_LOST_GOODS,
                        new String[]{ "", Integer.toString(++page) },
                        new String[]{ "atcId", "lstLctNm", "lstPrdtNm", "lstSbjt", "lstYmd" });
                                        // atcId : 관리ID     IstFilePathImg : 분실물 이미지

                for(HashMap<String,String> data : array) {
                    LostGoodsInfo lostGoods;
                    LostGoodsDetailInfo lostGoodsDetail;
                    String atcId = data.get("atcId");
                    String title = cutString(data.get("lstSbjt"));
                    String goods = cutString(data.get("lstPrdtNm"));
                    String date = data.get("lstYmd");
                    String imageFile = "";
                    detail_d = APIManager.getAPIMap((APIManager.GET_LOST_GOODS_DETAIL),
                            new String[]{ atcId }, new String[]{ "lstFilePathImg", "lstHor", "lstLctNm", "lstPlaceSeNm", "lstSteNm", "prdtClNm", "orgNm", "tel" });
                    imageFile = detail_d.get("lstFilePathImg");

                    String time = date + " " + detail_d.get("lstHor") + "시경";

                    lostGoods = new LostGoodsInfo(imageFile, atcId, title, goods, date);
                    lostGoodsDetail = new LostGoodsDetailInfo(imageFile, atcId, title, detail_d.get("lstLctNm"), time,
                            detail_d.get("prdtClNm"), detail_d.get("lstSteNm"), detail_d.get("orgNm"), detail_d.get("tel"));
                    lostGoodsInfos.add(lostGoods);  lostGoodsDetailInfos.add(lostGoodsDetail);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter();
                    }
                });
            }
        }.start();


        goUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lgRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    void setAdapter(){
        lgAdapter = new LostGoodsAdapter(lostGoodsInfos);
        lgRecyclerView.setHasFixedSize(true);
        lgRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        lgRecyclerView.setAdapter(lgAdapter);

        lgAdapter.setOnListItemSelected(new LostGoodsAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), LostGoodsDetailActivity.class);
                // 값을 넘겨주는 부분
                intent.putExtra("goods_info", (Serializable) lostGoodsDetailInfos.get(position));
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "상세정보.", Toast.LENGTH_SHORT).show();
            }
        });

        lgAdapter.notifyDataSetChanged();
    }

    String cutString(String str) {
        if(str.length() > 10) { // 글자가 10글자가 넘어가면
            str = str.substring(0, 9);  // 10글자로 줄이고
            str += "...";               // ... 붙이기!
        }
        return str;
    }
}
