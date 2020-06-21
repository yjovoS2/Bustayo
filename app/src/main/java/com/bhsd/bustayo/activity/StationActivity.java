package com.bhsd.bustayo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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

        Log.d("lyj", "\n [ stationNm ]\n arsId : " + arsId + "  stationNm : " + stationNm);

        new Thread() {
            @Override
            public void run() {
                busList = APIManager.getAPIArray(APIManager.GET_STATION_BY_UID_ITEM, new String[]{ arsId }, new String[]{"adirection","busRouteId","rtNm","congestion","routeType","arrmsg1","arrmsg2"});

                findViewById(R.id.refresh_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        busList = APIManager.getAPIArray(APIManager.GET_STATION_BY_UID_ITEM, new String[]{ arsId }, new String[]{"adirection","busRouteId","rtNm","congestion","routeType","arrmsg1","arrmsg2"});
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setBusListAdapter(busList);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setBusListAdapter(busList);

                        setToolbar(stationNm);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
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
                getColor(R.color.bus_yellow);
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

    void setBusListAdapter(ArrayList<HashMap<String,String>> item) {
        ArrayList<CurrentBusInfo> currentBusInfo = new ArrayList<>();
        for(int i = 0; i < item.size(); i++) {
            int busColor = getTypeColor(Integer.parseInt(item.get(i).get("routeType")));
            int busCongestion = getCongestionColor(Integer.parseInt(item.get(i).get("congestion")));
            String busNum = item.get(i).get("rtNm");
            String busDestination = item.get(i).get("adirection") + "방면";
            String currentLocation1 = item.get(i).get("arrmsg1");
            String currentLocation2 = item.get(i).get("arrmsg2");

            // (int busColor, int busCongestion, int busNum,
            // String busDestination, String currentLocation1, String currentLocation2)

            CurrentBusInfo it = new CurrentBusInfo(busColor,busCongestion,busNum,busDestination,currentLocation1,currentLocation2,false);
            currentBusInfo.add(it);
        }
        adapter = new CurrentBusRecyclerViewAdapter(currentBusInfo, false);
        recyclerView.setAdapter(adapter);
    }

    public void clickAlarm(View v) {
        TextView time = v.getRootView().findViewById(R.id.bus_time).findViewById(R.id.bus_first);
        String s = time.getText().toString();
        Log.d(s, s);
        if(s.equals("운행종료")) {
            Toast.makeText(StationActivity.this, "해당 노선은 운행을 종료했습니다 ㅜㅜ!", Toast.LENGTH_LONG).show();
            return;
        }

        final String[] data = { "1 정거장", "2 정거장", "3 정거장" };
        final StringBuilder msg = new StringBuilder();
        AlertDialog.Builder dialog = new AlertDialog.Builder(StationActivity.this);

        dialog.setTitle("승차알림");
        dialog.setIcon(R.drawable.ic_alarm);

        dialog.setSingleChoiceItems(data, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                msg.delete(0,msg.length());
                msg.append(data[which]).append(" 뒤에 알람을 울립니다.");
            }
        });

        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 아이콘 색상 변경ㅇ & 상단바에 뜨게!
                Toast.makeText(StationActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
        dialog.setNegativeButton("취소", null);
        dialog.show();
    }

    void setToolbar(String bus_number_text) {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(bus_number_text);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_go_back);

        Window window = StationActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.bus_blue));   // 상태바 색상

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.station_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.bookmark:
                Toast.makeText(this,"즐겨찾기눌림~!",Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}