package com.bhsd.bustayo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class StationListActivity extends AppCompatActivity {

    String key = "a9hQklCDHMmI23KG3suYrx0VtU7OOMgN%2B1SbLmIclORV%2FD%2F5QTRxFtmrjHzv4IEh8GiXMgiryKrlu7KKyAstKg%3D%3D";
    String url = "http://ws.bus.go.kr/api/rest/";

    String busNumber = "7612";
    String busId;
    int busType;
    RecyclerView stationListRCV;
    StationListAdapter bus_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stationListRCV = findViewById(R.id.station_list_recyclerview);

        setBusAdapter();

        new Thread() {
            @Override
            public void run() {
                SimpleArrayMap<String,String> busInfo = getApiMap("busRouteInfo/getBusRouteList?serviceKey=", "&strSrch=", busNumber, new String[]{"busRouteId","routeType"});
                busId = busInfo.get("busRouteId");
                busType = Integer.parseInt(busInfo.get("routeType"));
                ArrayList<SimpleArrayMap<String,String>> stationInfo = getApiArray("busRouteInfo/getStaionByRoute?serviceKey=", "&busRouteId=", busInfo.get("busRouteId"), new String[]{"station", "stationNm"});

                setStationAdapter(stationInfo);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setToolbar(busNumber);
                        bus_adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
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
            StationListItem it = new StationListItem(item.get(i).get("stationNm"), previous, next);
            bus_adapter.addItem(it);
        }
    }

    SimpleArrayMap<String, String> getApiMap(String search_url, String search_tag, String search_content, String[] tags) {
        String queryUrl = url + search_url + key + search_tag + search_content;
        SimpleArrayMap<String,String> return_value = new SimpleArrayMap<>();

        try {
            URL url = new URL(queryUrl);
            InputStream input_stream = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(input_stream, StandardCharsets.UTF_8));

            String tag;

            parser.next();
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tag = parser.getName(); // get tag name
                    for (String s : tags) {
                        if (tag.equals(s)) {
                            parser.next();
                            return_value.put(s, parser.getText());
                        }
                    }
                }
                eventType = parser.next();
            }
            return return_value;
        } catch (Exception e) {
            //e.toString();
        }
        return null;
    }

    ArrayList<SimpleArrayMap<String,String>> getApiArray(String search_url, String search_tag, String search_content, String[] tags) {
        String queryUrl = url + search_url + key + search_tag + search_content;
        ArrayList<SimpleArrayMap<String,String>> return_value = new ArrayList<>();

        try {
            URL url = new URL(queryUrl);
            InputStream input_stream = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(input_stream, StandardCharsets.UTF_8));

            String tag;

            parser.next();

            SimpleArrayMap<String,String> item = new SimpleArrayMap<>();
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tag = parser.getName(); // get tag name
                    for (String s : tags) {
                        if (tag.equals(s)) {
                            parser.next();
                            item.put(s, parser.getText());
                        }
                    }
                }
                if(eventType == XmlPullParser.END_TAG) {
                    tag = parser.getName();
                    if(tag.equals("itemList")) {
                        return_value.add(item);
                        item = new SimpleArrayMap<>();
                    }
                }
                eventType = parser.next();
            }
            return return_value;
        } catch (Exception e) {
            //e.toString();
        }
        return null;
    }

    void setBusAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stationListRCV.setLayoutManager(linearLayoutManager);

        bus_adapter = new StationListAdapter();
        stationListRCV.setAdapter(bus_adapter);
    }

    void setToolbar(String title) {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);    // 커스터마이징
        actionBar.setDisplayHomeAsUpEnabled(true);  // 홈버튼 보이게
        actionBar.setTitle(title);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher_foreground); // 홈버튼 아이콘

        Window window = StationListActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        actionBarColor(actionBar, window);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    void actionBarColor(ActionBar actionBar, Window window) {
        int color;
        switch (busType) {
            case 1:
                color = Color.rgb(123, 82, 0);
                break;
            case 2:
            case 4:
                color = Color.GREEN;
                break;
            case 3:
                color = Color.BLUE;
                break;
            case 5:
                color = Color.YELLOW;
                break;
            case 6:
            case 0:
                color = Color.RED;
                break;
            case 7:
            case 8:
            case 9:
            default:
                color = Color.GRAY;
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
                intent.putExtra("stationNm", busNumber);
                intent.putExtra("routeType", busType);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
