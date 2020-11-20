package com.bhsd.bustayo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.adapter.CurrentBusRecyclerViewAdapter;
import com.bhsd.bustayo.application.APIManager;
import com.bhsd.bustayo.dto.CurrentBusInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class StationActivity extends AppCompatActivity {
    private final static String LOG_NAME = "StationActivity";
    //
    ArrayList<CurrentBusInfo> currentBusInfo = new ArrayList<>();
    CurrentBusRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<HashMap<String,String>> busList;
    String arsId;
    String stationNm;
    Activity activity;

    // 자동새로고침을 위한 것들
    Handler handler = new Handler();
    Runnable runnable;
    int delay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        Intent inIntent = getIntent();
        arsId = inIntent.getStringExtra("arsId");
        stationNm = inIntent.getStringExtra("stationNm");
        activity = StationActivity.this;

        setRecyclerView();

        // SharedPreferences에 저장된 refresh 값 가져오기!!
        delay = getSharedPreferences("setting", 0).getInt("refresh", 0);

        new Thread() {
            @Override
            public void run() {
                setDefaultData();
                interrupt();
            }
        }.start();

        /* 새로고침 버튼 클릭~! */
        findViewById(R.id.refresh_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        interrupt();
                    }
                }.start();
            }
        });
    }

    void setRecyclerView() {
        recyclerView = findViewById(R.id.bus_list_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CurrentBusRecyclerViewAdapter(currentBusInfo, false, activity);
        adapter.setArsId(arsId);

        /* 아이템 클릭 이벤트 */
        adapter.setOnListItemSelected(new CurrentBusRecyclerViewAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position) {
                Intent intent = new Intent(StationActivity.this, StationListActivity.class);
                String busNum = adapter.getItem(position).getBusNum()+"";
                intent.putExtra("busRouteNm", busNum);
                StationActivity.this.startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    // 기본적인 데이터 불러오고 툴바설정~~!~!
    void setDefaultData() {
        busList = APIManager.getAPIArray(APIManager.GET_STATION_BY_UID_ITEM, new String[]{ arsId },
                new String[]{"adirection","busRouteId","rtNm","congestion","routeType","arrmsg1","arrmsg2"});

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setData();
                setToolbar(stationNm);
            }
        });
    }

    void setToolbar(String bus_number_text) {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(bus_number_text);
        Drawable icon = getDrawable(R.drawable.ic_go_back).mutate();
        icon.setTint(Color.WHITE);
        actionBar.setHomeAsUpIndicator(icon);

        Window window = StationActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.station));   // 상태바 색상
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // API 데이터 불러오기~!~!
    void getData() {
        new Thread() {
            @Override
            public void run() {
                busList = APIManager.getAPIArray(APIManager.GET_STATION_BY_UID_ITEM, new String[]{ arsId },
                        new String[]{"adirection","busRouteId","rtNm","congestion","routeType","arrmsg1","arrmsg2"});
                interrupt();
            }
        }.start();
    }

    // adapter에 등록되어있는 ArrayList의 data를 설정!!!
    void setData() {
        for(int i = 0; i < busList.size(); i++) {
            HashMap<String, String> item = busList.get(i);
            String busRouteId = item.get("busRouteId");
            String busColor = item.get("routeType");
            String busCongestion = item.get("congestion");
            String busNum = item.get("rtNm");
            String busDestination = item.get("adirection") + "방면";
            String currentLocation1 = item.get("arrmsg1");
            String currentLocation2 = item.get("arrmsg2");

            CurrentBusInfo it = new CurrentBusInfo(busRouteId, busColor,busCongestion,busNum,busDestination,currentLocation1,currentLocation2,
                    false, arsId);

            // currentBusInfo와 busList의 사이즈가 다르다면??? => 아직 아무런 데이터도 없다...
            if(currentBusInfo.size() != busList.size()) {
                currentBusInfo.add(it);     // 그냥 데이터만 넣기!!
            } else {    // 아니면 (currentBusInfo와 busList의 사이즈가 같으면???)
                currentBusInfo.set(i, it);  // 해당 위치 데이터를 새로운 데이터로 변경!!
            }
            adapter.notifyItemChanged(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                Log.e("yj", LOG_NAME + "  " + (delay / 1000) + " second later");

                getData();
                setData();

                adapter.notifyDataSetChanged();
            }
        }, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}