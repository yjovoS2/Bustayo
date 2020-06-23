package com.bhsd.bustayo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.bhsd.bustayo.dto.CurrentBusInfo;
import com.bhsd.bustayo.adapter.CurrentBusRecyclerViewAdapter;
import com.bhsd.bustayo.R;
import com.bhsd.bustayo.application.APIManager;

import java.util.ArrayList;
import java.util.HashMap;

public class StationActivity extends AppCompatActivity {
    //
    CurrentBusRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<HashMap<String,String>> busList;
    String arsId;
    String stationNm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        Log.d("ljh", "StationActivity onCreate");

        recyclerView = findViewById(R.id.bus_list_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent inIntent = getIntent();
        arsId = inIntent.getStringExtra("arsId");
        stationNm = inIntent.getStringExtra("stationNm");

        new Thread() {
            @Override
            public void run() {
                busList = APIManager.getAPIArray(APIManager.GET_STATION_BY_UID_ITEM, new String[]{ arsId }, new String[]{"adirection","busRouteId","rtNm","congestion","routeType","arrmsg1","arrmsg2"});

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter(busList);

                        setToolbar(stationNm);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();

        /* 새로고침 버튼 */
        findViewById(R.id.refresh_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        busList = APIManager.getAPIArray(APIManager.GET_STATION_BY_UID_ITEM, new String[]{ arsId }, new String[]{"adirection","busRouteId","rtNm","congestion","routeType","arrmsg1","arrmsg2"});
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setAdapter(busList);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }.start();
            }
        });
    }

    void setAdapter(ArrayList<HashMap<String,String>> list) {
        ArrayList<CurrentBusInfo> currentBusInfo = new ArrayList<>();
        for(HashMap<String, String> item : list) {
            int busColor = getTypeColor(Integer.parseInt(item.get("routeType")));
            int busCongestion = getCongestionColor(Integer.parseInt(item.get("congestion")));
            String busNum = item.get("rtNm");
            String busDestination = item.get("adirection") + "방면";
            String currentLocation1 = item.get("arrmsg1");
            String currentLocation2 = item.get("arrmsg2");

            // (int busColor, int busCongestion, int busNum,
            // String busDestination, String currentLocation1, String currentLocation2)

            CurrentBusInfo it = new CurrentBusInfo(busColor,busCongestion,busNum,busDestination,currentLocation1,currentLocation2,false);
            currentBusInfo.add(it);
        }
        adapter = new CurrentBusRecyclerViewAdapter(currentBusInfo, false);

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
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    int getCongestionColor(int congestion) {
        int color = getColor(R.color.import_error);
        switch(congestion) {
            case 3: // 여유
                color = getColor(R.color.busy_empty);
            case 4: // 보통
                color = getColor(R.color.busy_half);
            case 5: // 혼잡
            case 6:
                color = getColor(R.color.busy_full);
            case 0: // 데이터 없음
            default:
        }
        return color;
    }

    int getTypeColor(int type) {
        int color = getColor(R.color.import_error);
        switch (type) {
            case 1: // 공항
                color = getColor(R.color.bus_skyblue);
                break;
            case 2: // 마을
            case 4: // 지선
                color = getColor(R.color.bus_green);
                break;
            case 3: // 간선
                color = getColor(R.color.bus_blue);
                break;
            case 5: // 순환
                color = getColor(R.color.bus_yellow);
                break;
            case 6: // 광역
            case 0: // 공용
                color = getColor(R.color.bus_red);
                break;
            case 7: // 인천
            case 8: // 경기
            case 9: // 폐지
            default:
        }
        return color;
    }
}