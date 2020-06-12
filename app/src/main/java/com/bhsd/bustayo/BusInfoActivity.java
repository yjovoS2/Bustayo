package com.bhsd.bustayo;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BusInfoActivity extends AppCompatActivity {

    BusInfo busInfo;
    String busId;
    int busType;
    TextView bus_service_section;
    TextView bus_service_time_weekday, bus_service_time_weekend;
    TextView bus_interval;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businfo);

        Intent inIntent = getIntent();
        busId = inIntent.getStringExtra("busRouteId");
        busType = inIntent.getIntExtra("routeType", 0);

        new Thread() {
            @Override
            public void run() {
                busInfo = new BusInfo(busId);
                bus_service_section = findViewById(R.id.bus_service_section_content);
                bus_service_time_weekday = findViewById(R.id.weekday_time);
                bus_service_time_weekend = findViewById(R.id.weekend_time);
                bus_interval = findViewById(R.id.bus_interval);
                final String time = busInfo.getBusInfoItem("beginTm") + " ~ " + busInfo.getBusInfoItem("lastTm");

                setBusServiceSection(busInfo.getBusInfoItem("edStationNm") + " ~ " + busInfo.getBusInfoItem("stStationNm"));
                setBusServiceInterval(busInfo.getBusInfoItem("term") + " 분");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setBusServiceTime(time, time);
                        setToolbar(busInfo.getBusInfoItem("busRouteNm"));
                    }
                });
            }
        }.start();
    }

    void setBusServiceSection(String content) { // 운행구간
        bus_service_section.setText(content);
    }

    void setBusServiceTime(String weekday, String weekend) {
        bus_service_time_weekday.setText(weekday);
        bus_service_time_weekend.setText(weekend);
    }

    void setBusServiceInterval(String content) {
        bus_interval.setText(content);
    }

    void setToolbar(String title) {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);

        Window window = BusInfoActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        actionBarColor(actionBar, window);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}