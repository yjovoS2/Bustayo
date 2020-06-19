package com.bhsd.bustayo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.SimpleArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.application.Common;

import java.util.ArrayList;

public class StationListActivity extends AppCompatActivity {

    private String key = "a9hQklCDHMmI23KG3suYrx0VtU7OOMgN%2B1SbLmIclORV%2FD%2F5QTRxFtmrjHzv4IEh8GiXMgiryKrlu7KKyAstKg%3D%3D";
    private String url = "http://ws.bus.go.kr/api/rest/";
    private ApiManager apiManager = new ApiManager(Common.SERVICE_KEY, url);

    private String busNumber;
    private String busId;
    private int busType;
    private RecyclerView stationListRCV;
    private StationListAdapter stationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);
        Log.d("ljh", "Station List Activity onCreate");

        // 선택된 Bus의 정보를 받아오기 위한 intent
        Intent inIntent = getIntent();
        busNumber = inIntent.getStringExtra("busRouteNm");

        stationListRCV = findViewById(R.id.station_list_recyclerview);
        setStationListAdapter();    // recyclerview에 대한 adapter설정

        new Thread() {
            @Override
            public void run() {
                SimpleArrayMap<String,String> busInfo = apiManager.getApiMap("busRouteInfo/getBusRouteList?serviceKey=", "&strSrch=", busNumber, new String[]{"busRouteId","routeType"});
                busId = busInfo.get("busRouteId");
                busType = Integer.parseInt(busInfo.get("routeType"));
                ArrayList<SimpleArrayMap<String,String>> stationInfo = apiManager.getApiArray("busRouteInfo/getStaionByRoute?serviceKey=", "&busRouteId=", busInfo.get("busRouteId"), new String[]{"arsId", "stationNm"});

                setStationAdapter(stationInfo);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setToolbar(busNumber);
                        stationListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }

    /* adapter & item 설정! */
    void setStationListAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stationListRCV.setLayoutManager(linearLayoutManager);

        stationListAdapter = new StationListAdapter();
        stationListRCV.setAdapter(stationListAdapter);
    }

    void setStationAdapter(ArrayList<SimpleArrayMap<String,String>> item) {
        int previous, next;
        for(int i = 0; i < item.size(); i++) {
            if(i == 0) {
                previous = getColor(R.color.invisible);
            } else {
                previous = Color.DKGRAY;
            }

            if(i == item.size() -1) {
                next = getColor(R.color.invisible);
            } else {
                next = Color.DKGRAY;
            }
            StationListItem it = new StationListItem(item.get(i).get("stationNm"), item.get(i).get("arsId"), previous, next);
            Log.d("stationid", item.get(i).get("arsId"));
            stationListAdapter.addItem(it);
        }
    }

    /* toolbar 설정! */
    void setToolbar(String title) {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);    // 커스터마이징
        actionBar.setDisplayHomeAsUpEnabled(true);  // 홈버튼 보이게
        actionBar.setTitle(title);
        Drawable back_icon = getDrawable(R.drawable.ic_go_back).mutate();
        back_icon.setTint(Color.WHITE);
        actionBar.setHomeAsUpIndicator(back_icon); // 홈버튼 아이콘

        Window window = StationListActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        actionBarColor(actionBar, window);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    // busType에 따른 toolbar와 statusbar 색상 설정
    void actionBarColor(ActionBar actionBar, Window window) {
        int color = 0;
        switch (busType) {
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
                color = getColor(R.color.import_error);
        }
        actionBar.setBackgroundDrawable(new ColorDrawable(color));
        window.setStatusBarColor(color);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.station_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.bus_info:
                Intent intent = new Intent(StationListActivity.this, BusInfoActivity.class);
                intent.putExtra("busRouteId", busId);
                intent.putExtra("routeType", busType);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
