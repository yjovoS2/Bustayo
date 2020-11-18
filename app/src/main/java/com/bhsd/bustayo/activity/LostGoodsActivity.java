package com.bhsd.bustayo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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

    // Spinner categorize;
    EditText searchLostGoods;
    Button searchButton;
    ArrayList<HashMap<String, String>> array;
    HashMap<String, String> detail_d;
    ArrayList<LostGoodsInfo> lostGoodsInfos;
    ArrayList<LostGoodsDetailInfo> lostGoodsDetailInfos;
    RecyclerView lgRecyclerView;
    LostGoodsAdapter lgAdapter;
    FloatingActionButton goUp;
    View loading_background;
    ProgressBar loading_progressBar;
    int page = 0;
    String searchStr = "";

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

        // categorize = findViewById(R.id.lostGroup);
        searchButton = findViewById(R.id.searchButton);
        searchLostGoods = findViewById(R.id.userLostGoods);
        lgRecyclerView = findViewById(R.id.lostList);
        goUp = findViewById(R.id.goUp);
        loading_background = findViewById(R.id.loading_bg);
        loading_progressBar = findViewById(R.id.loading_progress);


        lgRecyclerView.setHasFixedSize(true);
        lgRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        lgRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(final View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (!v.canScrollVertically(1)) {    // 스크롤이 마지막에 닿았으면?
                    loading_background.setVisibility(View.VISIBLE);
                    loading_progressBar.setVisibility(View.VISIBLE);
                    new Thread() {
                        @Override
                        public void run() {
                            getData();  // api 불러오기 (data 추가!!)
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lgAdapter.notifyDataSetChanged();
                                    loading_background.setVisibility(View.GONE);
                                    loading_progressBar.setVisibility(View.GONE);
                                }
                            });

                            interrupt();
                        }
                    }.start();
                }
            }
        });

        // 검색버튼 클릭
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 0;   // 새롭게 검색버튼이 눌렸으니 페이지를 0페이지로
                loading_background.setVisibility(View.VISIBLE);
                loading_progressBar.setVisibility(View.VISIBLE);
                new Thread() {
                    @Override
                    public void run() {
                        searchStr = searchLostGoods.getText().toString();  // 입력된 검색어 저장
                        getData();  // 입력된 검색어에 대해 API 불러오기

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setAdapter();
                                loading_background.setVisibility(View.GONE);
                                loading_progressBar.setVisibility(View.GONE);
                            }
                        });

                        interrupt();
                    }
                }.start();
            }
        });

        /* api에 카테고리로 검색이 없어서 일단 주석
        String[] categorizes ={"전체", "귀금속", "의류", "지갑", "전자기기", "컴퓨터", "카드", "휴대폰", "기타"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categorizes);
        categorize.setAdapter(arrayAdapter);
        */

        new Thread() {
            @Override
            public void run() {
                loading_background.setVisibility(View.VISIBLE);
                loading_progressBar.setVisibility(View.VISIBLE);
                getData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter();
                        loading_background.setVisibility(View.GONE);
                        loading_progressBar.setVisibility(View.GONE);
                    }
                });

                interrupt();
            }
        }.start();


        goUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lgRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    void getData() {
        if(page == 0) {
            lostGoodsInfos = new ArrayList<>();
            lostGoodsDetailInfos = new ArrayList<>();
        }

        array = APIManager.getAPIArray(APIManager.GET_LOST_GOODS,
                new String[]{ searchStr, Integer.toString(++page) },
                new String[]{ "atcId", "lstLctNm", "lstPrdtNm", "lstSbjt", "lstYmd" });
        // atcId : 관리ID     IstFilePathImg : 분실물 이미지

        for(HashMap<String,String> data : array) {
            LostGoodsInfo lostGoods;
            LostGoodsDetailInfo lostGoodsDetail;
            String atcId = data.get("atcId");
            String title = data.get("lstSbjt");
            String goods = data.get("lstPrdtNm");
            String date = data.get("lstYmd");
            String imageFile = "";
            detail_d = APIManager.getAPIMap((APIManager.GET_LOST_GOODS_DETAIL),
                    new String[]{ atcId }, new String[]{ "lstFilePathImg", "lstHor", "lstLctNm", "lstPlaceSeNm", "lstSteNm", "prdtClNm", "orgNm", "tel" });
            imageFile = detail_d.get("lstFilePathImg");

            String time = date + " " + detail_d.get("lstHor") + "시경";

            // data
            lostGoods = new LostGoodsInfo(imageFile, atcId, cutString(title), cutString(goods), date);

            // 상세화면 data
            lostGoodsDetail = new LostGoodsDetailInfo(imageFile, atcId, title, detail_d.get("lstLctNm"), time,
                    detail_d.get("prdtClNm"), detail_d.get("lstSteNm"), detail_d.get("orgNm"), detail_d.get("tel"));
            lostGoodsInfos.add(lostGoods);
            lostGoodsDetailInfos.add(lostGoodsDetail);
        }
    }

    void setAdapter(){
        lgAdapter = new LostGoodsAdapter(lostGoodsInfos);lgRecyclerView.setAdapter(lgAdapter);

        lgAdapter.setOnListItemSelected(new LostGoodsAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), LostGoodsDetailActivity.class);
                // 값을 넘겨주는 부분
                intent.putExtra("goods_info", (Serializable) lostGoodsDetailInfos.get(position));
                startActivity(intent);
            }
        });

        lgRecyclerView.setAdapter(lgAdapter);
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
